package com.vit_ana.scheduler.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import com.vit_ana.scheduler.entity.Appointment;
import com.vit_ana.scheduler.entity.AppointmentStatus;
import com.vit_ana.scheduler.entity.ChatMessage;
import com.vit_ana.scheduler.entity.Work;
import com.vit_ana.scheduler.model.TimePeriod;

public interface AppointmentService {
	void createNewAppointment(UUID workId, UUID providerId, UUID customerId, OffsetDateTime start);

	void updateAppointment(Appointment appointment);

	void updateUserAppointmentsStatuses(UUID userId);

	void updateAllAppointmentsStatuses();

	void updateAppointmentsStatusesWithExpiredExchangeRequest();

	void deleteAppointmentById(UUID appointmentId);

	Appointment getAppointmentByIdWithAuthorization(UUID id);

	Appointment getAppointmentById(UUID id);

	List<Appointment> getAllAppointments();

	List<Appointment> getAppointmentByCustomerId(UUID customerId);

	List<Appointment> getAppointmentByProviderId(UUID providerId);

	List<Appointment> getAppointmentsByProviderAtDay(UUID providerId, LocalDate day);

	List<Appointment> getAppointmentsByWorkAndStatusAtDay(UUID workId, AppointmentStatus status, LocalDate day);

	List<Appointment> getAppointmentsByCustomerAtDay(UUID providerId, LocalDate day);

	List<Appointment> getConfirmedAppointmentsByCustomerId(UUID customerId);

	List<Appointment> getCanceledAppointmentsByCustomerIdForCurrentMonth(UUID userId);

	List<TimePeriod> getAvailableHours(UUID providerId, UUID customerId, UUID workId, OffsetDateTime datetime);

	List<TimePeriod> calculateAvailableHours(List<TimePeriod> availableTimePeriods, Work work);

	List<TimePeriod> excludeAppointmentsFromTimePeriods(List<TimePeriod> periods, List<Appointment> appointments);

	String getCancelNotAllowedReason(UUID userId, UUID appointmentId);

	void cancelUserAppointmentById(UUID appointmentId, UUID userId);

	boolean isCustomerAllowedToRejectAppointment(UUID customerId, UUID appointmentId);

	boolean requestAppointmentRejection(UUID appointmentId, UUID customerId);

	boolean requestAppointmentRejection(String token);

	boolean isProviderAllowedToAcceptRejection(UUID providerId, UUID appointmentId);

	boolean acceptRejection(UUID appointmentId, UUID providerId);

	boolean acceptRejection(String token);

	void addMessageToAppointmentChat(UUID appointmentId, UUID authorId, ChatMessage chatMessage);

	int getNumberOfCanceledAppointmentsForUser(UUID userId);

	int getNumberOfScheduledAppointmentsForUser(UUID userId);

	boolean isAvailable(UUID workId, UUID providerId, UUID customerId, OffsetDateTime start);
}
