package com.vit_ana.scheduler.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.vit_ana.scheduler.dao.AppointmentRepository;
import com.vit_ana.scheduler.dao.ChatMessageRepository;
import com.vit_ana.scheduler.entity.Appointment;
import com.vit_ana.scheduler.entity.AppointmentStatus;
import com.vit_ana.scheduler.entity.ChatMessage;
import com.vit_ana.scheduler.entity.Work;
import com.vit_ana.scheduler.entity.WorkingPlan;
import com.vit_ana.scheduler.entity.user.User;
import com.vit_ana.scheduler.entity.user.provider.Provider;
import com.vit_ana.scheduler.exception.AppointmentNotFoundException;
import com.vit_ana.scheduler.model.DayPlan;
import com.vit_ana.scheduler.model.TimePeriod;
import com.vit_ana.scheduler.service.AppointmentService;
import com.vit_ana.scheduler.service.NotificationService;
import com.vit_ana.scheduler.service.UserService;
import com.vit_ana.scheduler.service.WorkService;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final int NUMBER_OF_ALLOWED_CANCELATIONS_PER_MONTH = 1;
    private final AppointmentRepository appointmentRepository;
    private final UserService userService;
    private final WorkService workService;
    private final ChatMessageRepository chatMessageRepository;
    private final NotificationService notificationService;
    private final JwtTokenServiceImpl jwtTokenService;
    private final MessageSource messageSource;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, UserService userService, WorkService workService, ChatMessageRepository chatMessageRepository, NotificationService notificationService, JwtTokenServiceImpl jwtTokenService, MessageSource messageSource) {
        this.appointmentRepository = appointmentRepository;
        this.userService = userService;
        this.workService = workService;
        this.chatMessageRepository = chatMessageRepository;
        this.notificationService = notificationService;
        this.jwtTokenService = jwtTokenService;
		this.messageSource = messageSource;
    }

    @Override
    public void updateAppointment(Appointment appointment) {
        appointmentRepository.save(appointment);
    }

    @Override
    @PostAuthorize("returnObject.provider.id == principal.id or returnObject.customer.id == principal.id or hasRole('ADMIN') ")
    public Appointment getAppointmentByIdWithAuthorization(UUID id) {
        return getAppointmentById(id);
    }

    @Override
    public Appointment getAppointmentById(UUID id) {
        return appointmentRepository.findById(id)
                .orElseThrow(AppointmentNotFoundException::new);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public void deleteAppointmentById(UUID id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    @PreAuthorize("#customerId == principal.id")
    public List<Appointment> getAppointmentByCustomerId(UUID customerId) {
        return appointmentRepository.findByCustomerId(customerId);
    }

    @Override
    @PreAuthorize("#providerId == principal.id")
    public List<Appointment> getAppointmentByProviderId(UUID providerId) {
        return appointmentRepository.findByProviderId(providerId);
    }

    @Override
    public List<Appointment> getAppointmentsByProviderAtDay(UUID providerId, LocalDate day) {
        return appointmentRepository.findByProviderIdWithStartInPeriod(providerId, 
        		day.atStartOfDay().atOffset(ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000)), 
        		day.atStartOfDay().plusDays(1).atOffset(ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000)));
    }

    @Override
    public List<Appointment> getAppointmentsByWorkAndStatusAtDay(UUID workId, AppointmentStatus status, LocalDate day) {
        return appointmentRepository.findByWorkIdAndStatusWithStartInPeriod(workId, status,
        		day.atStartOfDay().atOffset(ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000)), 
        		day.atStartOfDay().plusDays(1).atOffset(ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000)));
    }

    @Override
    public List<Appointment> getAppointmentsByCustomerAtDay(UUID providerId, LocalDate day) {
        return appointmentRepository.findByCustomerIdWithStartInPeriod(
        		providerId, 
        		day.atStartOfDay().atOffset(ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000)), 
        		day.atStartOfDay().plusDays(1).atOffset(ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000)));
    }

    @Override
    public List<TimePeriod> getAvailableHours(UUID providerId, UUID customerId, UUID workId, OffsetDateTime datetime) {
    	Work work = workService.getWorkById(workId);
        Provider p = userService.getProviderById(providerId);
        WorkingPlan workingPlan = p.getWorkingPlan();
        DayPlan selectedDay = workingPlan.getDay(datetime.getDayOfWeek().toString().toLowerCase());

        List<TimePeriod> availablePeriods;
        if (work.getIsUseSlots() && !Objects.equals(providerId, customerId)) {
        	List<Appointment> availableAppointments = getAppointmentsByWorkAndStatusAtDay(workId, AppointmentStatus.AVAILABLE, datetime.toLocalDate());
        	availablePeriods = availableAppointments.stream()
        			.map(a -> new TimePeriod(a.getStart().toOffsetTime(), a.getEnd().toOffsetTime(), a.getId()))
        			.toList();
        } else {
	        availablePeriods = selectedDay.getTimePeriodsWithBreaksExcluded();

	        List<Appointment> providerAppointments = getAppointmentsByProviderAtDay(providerId, datetime.toLocalDate());

	        availablePeriods = excludeAppointmentsFromTimePeriods(availablePeriods, providerAppointments);

	        if (!Objects.equals(providerId, customerId)) {
	        	List<Appointment> customerAppointments = getAppointmentsByCustomerAtDay(customerId, datetime.toLocalDate());

	        	availablePeriods = excludeAppointmentsFromTimePeriods(availablePeriods, customerAppointments);
	        }

	        availablePeriods = calculateAvailableHours(availablePeriods, work);
        }
        return availablePeriods;
    }

    @Override
    public void createNewAppointment(UUID workId, UUID providerId, UUID customerId, OffsetDateTime start) {
    	if (customerId == null){
            Appointment appointment = new Appointment();
            appointment.setStatus(AppointmentStatus.AVAILABLE);
            appointment.setProvider(userService.getProviderById(providerId));
            Work work = workService.getWorkById(workId);
            appointment.setWork(work);
            appointment.setStart(start);
            appointment.setEnd(start.plusMinutes(work.getDuration()));
            appointmentRepository.save(appointment);
    	} else if (isAvailable(workId, providerId, customerId, start)) {
            Appointment appointment = new Appointment();
            appointment.setStatus(AppointmentStatus.SCHEDULED);
            appointment.setCustomer(userService.getCustomerById(customerId));
            appointment.setProvider(userService.getProviderById(providerId));
            Work work = workService.getWorkById(workId);
            appointment.setWork(work);
            appointment.setStart(start);
            appointment.setEnd(start.plusMinutes(work.getDuration()));
            appointmentRepository.save(appointment);
            notificationService.newNewAppointmentScheduledNotification(appointment, true);
        } else {
            throw new RuntimeException();
        }

    }

    @Override
    public void takeAvailableAppointment(UUID appointmentId, UUID customerId) {
    	if (appointmentId != null && customerId != null) {
    		Appointment appointment = getAppointmentById(appointmentId);
    		if (appointment != null && AppointmentStatus.AVAILABLE.equals(appointment.getStatus())) {
    			appointment.setStatus(AppointmentStatus.SCHEDULED);
    			appointment.setCustomer(userService.getCustomerById(customerId));
    			updateAppointment(appointment);
                notificationService.newNewAppointmentScheduledNotification(appointment, true);
    		} else {
    			throw new RuntimeException();
    		}
    	} else {
            throw new RuntimeException();
        }

    }

    @Override
    public void addMessageToAppointmentChat(UUID appointmentId, UUID authorId, ChatMessage chatMessage) {
        Appointment appointment = getAppointmentByIdWithAuthorization(appointmentId);
        if (appointment.getProvider().getId().equals(authorId) || appointment.getCustomer().getId().equals(authorId)) {
            chatMessage.setAuthor(userService.getUserById(authorId));
            chatMessage.setAppointment(appointment);
            chatMessage.setCreatedAt(LocalDateTime.now());
            chatMessageRepository.save(chatMessage);
            notificationService.newChatMessageNotification(chatMessage, true);
        } else {
            throw new org.springframework.security.access.AccessDeniedException(messageSource.getMessage("access.Unauthorized", null, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public List<TimePeriod> calculateAvailableHours(List<TimePeriod> availableTimePeriods, Work work) {
        ArrayList<TimePeriod> availableHours = new ArrayList<>();
        for (TimePeriod period : availableTimePeriods) {
            TimePeriod workPeriod = new TimePeriod(period.getStart(), period.getStart().plusMinutes(work.getDuration()));
            while (workPeriod.getEnd().isBefore(period.getEnd()) || workPeriod.getEnd().equals(period.getEnd())) {
                availableHours.add(new TimePeriod(workPeriod.getStart(), workPeriod.getStart().plusMinutes(work.getDuration())));
                workPeriod.setStart(workPeriod.getStart().plusMinutes(work.getDuration()));
                workPeriod.setEnd(workPeriod.getEnd().plusMinutes(work.getDuration()));
            }
        }
        return availableHours;
    }

    @Override
    public List<TimePeriod> excludeAppointmentsFromTimePeriods(List<TimePeriod> periods, List<Appointment> appointments) {

        List<TimePeriod> toAdd = new ArrayList<>();
        Collections.sort(appointments);
        for (Appointment appointment : appointments) {
            for (TimePeriod period : periods) {
                if ((appointment.getStart().toOffsetTime().isBefore(period.getStart()) 
                		|| appointment.getStart().toOffsetTime().equals(period.getStart())) 
                		&& appointment.getEnd().toOffsetTime().isAfter(period.getStart()) 
                		&& appointment.getEnd().toOffsetTime().isBefore(period.getEnd())) {
                    period.setStart(appointment.getEnd().toOffsetTime());
                }
                if (appointment.getStart().toOffsetTime().isAfter(period.getStart()) 
                		&& appointment.getStart().toOffsetTime().isBefore(period.getEnd()) 
                		&& appointment.getEnd().toOffsetTime().isAfter(period.getEnd()) 
                		|| appointment.getEnd().toOffsetTime().equals(period.getEnd())) {
                    period.setEnd(appointment.getStart().toOffsetTime());
                }
                if (appointment.getStart().toOffsetTime().isAfter(period.getStart()) 
                		&& appointment.getEnd().toOffsetTime().isBefore(period.getEnd())) {
                    toAdd.add(new TimePeriod(period.getStart(), appointment.getStart().toOffsetTime()));
                    period.setStart(appointment.getEnd().toOffsetTime());
                }
            }
        }
        periods.addAll(toAdd);
        Collections.sort(periods);
        return periods;
    }

    @Override
    public List<Appointment> getCanceledAppointmentsByCustomerIdForCurrentMonth(UUID customerId) {
        return appointmentRepository.findByCustomerIdCanceledAfterDate(customerId, OffsetDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).truncatedTo(ChronoUnit.DAYS));
    }

    @Override
    public void updateUserAppointmentsStatuses(UUID userId) {
        for (Appointment appointment : appointmentRepository.findScheduledByUserIdWithEndBeforeDate(OffsetDateTime.now(), userId)) {
            appointment.setStatus(AppointmentStatus.FINISHED);
            updateAppointment(appointment);
        }

        for (Appointment appointment : appointmentRepository.findFinishedByUserIdWithEndBeforeDate(OffsetDateTime.now().minusDays(1), userId)) {

            appointment.setStatus(AppointmentStatus.INVOICED);
            updateAppointment(appointment);
        }
    }

    @Override
    public void updateAllAppointmentsStatuses() {
        appointmentRepository.findScheduledWithEndBeforeDate(OffsetDateTime.now())
                .forEach(appointment -> {
                    appointment.setStatus(AppointmentStatus.FINISHED);
                    updateAppointment(appointment);
                    if (OffsetDateTime.now().minusDays(1).isBefore(appointment.getEnd())) {
                        notificationService.newAppointmentFinishedNotification(appointment, true);
                    }
                });

        appointmentRepository.findFinishedWithEndBeforeDate(OffsetDateTime.now().minusDays(1))
                .forEach(appointment -> {
                    appointment.setStatus(AppointmentStatus.CONFIRMED);
                    updateAppointment(appointment);
                });
    }

    @Override
    public void updateAppointmentsStatusesWithExpiredExchangeRequest() {
        appointmentRepository.findExchangeRequestedWithStartBefore(OffsetDateTime.now().plusDays(1))
                .forEach(appointment -> {
                    appointment.setStatus(AppointmentStatus.SCHEDULED);
                    updateAppointment(appointment);
                });
    }

    @Override
    public void cancelUserAppointmentById(UUID appointmentId, UUID userId) {
        Appointment appointment = appointmentRepository.getReferenceById(appointmentId);
        if (appointment.getCustomer().getId().equals(userId) || appointment.getProvider().getId().equals(userId)) {
            appointment.setStatus(AppointmentStatus.CANCELED);
            User canceler = userService.getUserById(userId);
            appointment.setCanceler(canceler);
            appointment.setCanceledAt(OffsetDateTime.now());
            appointmentRepository.save(appointment);
            if (canceler.equals(appointment.getCustomer())) {
                notificationService.newAppointmentCanceledByCustomerNotification(appointment, true);
            } else if (canceler.equals(appointment.getProvider())) {
                notificationService.newAppointmentCanceledByProviderNotification(appointment, true);
            }
        } else {
            throw new org.springframework.security.access.AccessDeniedException(messageSource.getMessage("access.Unauthorized", null, LocaleContextHolder.getLocale()));
        }


    }


    @Override
    public boolean isCustomerAllowedToRejectAppointment(UUID userId, UUID appointmentId) {
        User user = userService.getUserById(userId);
        Appointment appointment = getAppointmentByIdWithAuthorization(appointmentId);

        return appointment.getCustomer().equals(user) && appointment.getStatus().equals(AppointmentStatus.FINISHED) && !OffsetDateTime.now().isAfter(appointment.getEnd().plusDays(1));
    }

    @Override
    public boolean requestAppointmentRejection(UUID appointmentId, UUID customerId) {
        if (isCustomerAllowedToRejectAppointment(customerId, appointmentId)) {
            Appointment appointment = getAppointmentByIdWithAuthorization(appointmentId);
            appointment.setStatus(AppointmentStatus.REJECTION_REQUESTED);
            notificationService.newAppointmentRejectionRequestedNotification(appointment, true);
            updateAppointment(appointment);
            return true;
        } else {
            return false;
        }

    }


    @Override
    public boolean requestAppointmentRejection(String token) {
        if (jwtTokenService.validateToken(token)) {
            UUID appointmentId = jwtTokenService.getAppointmentIdFromToken(token);
            UUID customerId = jwtTokenService.getCustomerIdFromToken(token);
            return requestAppointmentRejection(appointmentId, customerId);
        }
        return false;
    }


    @Override
    public boolean isProviderAllowedToAcceptRejection(UUID providerId, UUID appointmentId) {
        User user = userService.getUserById(providerId);
        Appointment appointment = getAppointmentByIdWithAuthorization(appointmentId);

        return appointment.getProvider().equals(user) && appointment.getStatus().equals(AppointmentStatus.REJECTION_REQUESTED);
    }

    @Override
    public boolean acceptRejection(UUID appointmentId, UUID customerId) {
        if (isProviderAllowedToAcceptRejection(customerId, appointmentId)) {
            Appointment appointment = getAppointmentByIdWithAuthorization(appointmentId);
            appointment.setStatus(AppointmentStatus.REJECTED);
            updateAppointment(appointment);
            notificationService.newAppointmentRejectionAcceptedNotification(appointment, true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean acceptRejection(String token) {
        if (jwtTokenService.validateToken(token)) {
            UUID appointmentId = jwtTokenService.getAppointmentIdFromToken(token);
            UUID providerId = jwtTokenService.getProviderIdFromToken(token);
            return acceptRejection(appointmentId, providerId);
        }
        return false;
    }

    @Override
    public String getCancelNotAllowedReason(UUID userId, UUID appointmentId) {
        User user = userService.getUserById(userId);
        Appointment appointment = getAppointmentByIdWithAuthorization(appointmentId);

        if (user.hasRole("ROLE_ADMIN")) {
            return "Only customer or provider can cancel appointments";
        }

        if (appointment.getProvider().equals(user)) {
            if (!appointment.getStatus().equals(AppointmentStatus.SCHEDULED)) {
                return "Only appoinmtents with scheduled status can be cancelled.";
            } else {
                return null;
            }
        }

        if (appointment.getCustomer().equals(user)) {
            if (!appointment.getStatus().equals(AppointmentStatus.SCHEDULED)) {
                return "Only appoinmtents with scheduled status can be cancelled.";
            } else if (OffsetDateTime.now().plusDays(1).isAfter(appointment.getStart())) {
                return "Appointments which will be in less than 24 hours cannot be canceled.";
            } else if (!appointment.getWork().getEditable()) {
                return "This type of appointment can be canceled only by Provider.";
            } else if (getCanceledAppointmentsByCustomerIdForCurrentMonth(userId).size() >= NUMBER_OF_ALLOWED_CANCELATIONS_PER_MONTH) {
                return "You can't cancel this appointment because you exceeded maximum number of cancellations in this month.";
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public int getNumberOfCanceledAppointmentsForUser(UUID userId) {
        return appointmentRepository.findCanceledByUser(userId).size();
    }

    @Override
    public int getNumberOfScheduledAppointmentsForUser(UUID userId) {
        return appointmentRepository.findScheduledByUserId(userId).size();
    }

    @Override
    public boolean isAvailable(UUID workId, UUID providerId, UUID customerId, OffsetDateTime start) {
        if (!workService.isWorkForCustomer(workId, customerId)) {
            return false;
        }
        Work work = workService.getWorkById(workId);
        TimePeriod timePeriod = new TimePeriod(start.toOffsetTime(), start.toOffsetTime().plusMinutes(work.getDuration()));
        return getAvailableHours(providerId, customerId, workId, start).contains(timePeriod);
    }

    @Override
    public List<Appointment> getConfirmedAppointmentsByCustomerId(UUID customerId) {
        return appointmentRepository.findConfirmedByCustomerId(customerId);
    }
}
