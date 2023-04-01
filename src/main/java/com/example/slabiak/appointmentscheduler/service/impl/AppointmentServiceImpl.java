package com.example.slabiak.appointmentscheduler.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.slabiak.appointmentscheduler.dao.AppointmentRepository;
import com.example.slabiak.appointmentscheduler.dao.ChatMessageRepository;
import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.AppointmentStatus;
import com.example.slabiak.appointmentscheduler.entity.ChatMessage;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.entity.user.provider.Provider;
import com.example.slabiak.appointmentscheduler.exception.AppointmentNotFoundException;
import com.example.slabiak.appointmentscheduler.model.DayPlan;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;
import com.example.slabiak.appointmentscheduler.service.UserService;
import com.example.slabiak.appointmentscheduler.service.WorkService;

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
        return appointmentRepository.findByProviderIdWithStartInPeroid(providerId, 
        		day.atStartOfDay().atOffset(ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000)), 
        		day.atStartOfDay().plusDays(1).atOffset(ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000)));
    }

    @Override
    public List<Appointment> getAppointmentsByCustomerAtDay(UUID providerId, LocalDate day) {
        return appointmentRepository.findByCustomerIdWithStartInPeroid(
        		providerId, 
        		day.atStartOfDay().atOffset(ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000)), 
        		day.atStartOfDay().plusDays(1).atOffset(ZoneOffset.ofTotalSeconds(TimeZone.getDefault().getRawOffset()/1000)));
    }

    @Override
    public List<TimePeroid> getAvailableHours(UUID providerId, UUID customerId, UUID workId, OffsetDateTime datetime) {
        Provider p = userService.getProviderById(providerId);
        WorkingPlan workingPlan = p.getWorkingPlan();
        DayPlan selectedDay = workingPlan.getDay(datetime.getDayOfWeek().toString().toLowerCase());

        List<Appointment> providerAppointments = getAppointmentsByProviderAtDay(providerId, datetime.toLocalDate());
        List<Appointment> customerAppointments = getAppointmentsByCustomerAtDay(customerId, datetime.toLocalDate());

        List<TimePeroid> availablePeroids = selectedDay.getTimePeroidsWithBreaksExcluded();
        availablePeroids = excludeAppointmentsFromTimePeroids(availablePeroids, providerAppointments);

        availablePeroids = excludeAppointmentsFromTimePeroids(availablePeroids, customerAppointments);
        return calculateAvailableHours(availablePeroids, workService.getWorkById(workId));
    }

    @Override
    public void createNewAppointment(UUID workId, UUID providerId, UUID customerId, OffsetDateTime start) {
        if (isAvailable(workId, providerId, customerId, start)) {
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
    public List<TimePeroid> calculateAvailableHours(List<TimePeroid> availableTimePeroids, Work work) {
        ArrayList<TimePeroid> availableHours = new ArrayList<>();
        for (TimePeroid peroid : availableTimePeroids) {
            TimePeroid workPeroid = new TimePeroid(peroid.getStart(), peroid.getStart().plusMinutes(work.getDuration()));
            while (workPeroid.getEnd().isBefore(peroid.getEnd()) || workPeroid.getEnd().equals(peroid.getEnd())) {
                availableHours.add(new TimePeroid(workPeroid.getStart(), workPeroid.getStart().plusMinutes(work.getDuration())));
                workPeroid.setStart(workPeroid.getStart().plusMinutes(work.getDuration()));
                workPeroid.setEnd(workPeroid.getEnd().plusMinutes(work.getDuration()));
            }
        }
        return availableHours;
    }

    @Override
    public List<TimePeroid> excludeAppointmentsFromTimePeroids(List<TimePeroid> peroids, List<Appointment> appointments) {

        List<TimePeroid> toAdd = new ArrayList<>();
        Collections.sort(appointments);
        for (Appointment appointment : appointments) {
            for (TimePeroid peroid : peroids) {
                if ((appointment.getStart().toOffsetTime().isBefore(peroid.getStart()) 
                		|| appointment.getStart().toOffsetTime().equals(peroid.getStart())) 
                		&& appointment.getEnd().toOffsetTime().isAfter(peroid.getStart()) 
                		&& appointment.getEnd().toOffsetTime().isBefore(peroid.getEnd())) {
                    peroid.setStart(appointment.getEnd().toOffsetTime());
                }
                if (appointment.getStart().toOffsetTime().isAfter(peroid.getStart()) 
                		&& appointment.getStart().toOffsetTime().isBefore(peroid.getEnd()) 
                		&& appointment.getEnd().toOffsetTime().isAfter(peroid.getEnd()) 
                		|| appointment.getEnd().toOffsetTime().equals(peroid.getEnd())) {
                    peroid.setEnd(appointment.getStart().toOffsetTime());
                }
                if (appointment.getStart().toOffsetTime().isAfter(peroid.getStart()) 
                		&& appointment.getEnd().toOffsetTime().isBefore(peroid.getEnd())) {
                    toAdd.add(new TimePeroid(peroid.getStart(), appointment.getStart().toOffsetTime()));
                    peroid.setStart(appointment.getEnd().toOffsetTime());
                }
            }
        }
        peroids.addAll(toAdd);
        Collections.sort(peroids);
        return peroids;
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
        Appointment appointment = appointmentRepository.getOne(appointmentId);
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
        TimePeroid timePeroid = new TimePeroid(start.toOffsetTime(), start.toOffsetTime().plusMinutes(work.getDuration()));
        return getAvailableHours(providerId, customerId, workId, start).contains(timePeroid);
    }

    @Override
    public List<Appointment> getConfirmedAppointmentsByCustomerId(UUID customerId) {
        return appointmentRepository.findConfirmedByCustomerId(customerId);
    }
}
