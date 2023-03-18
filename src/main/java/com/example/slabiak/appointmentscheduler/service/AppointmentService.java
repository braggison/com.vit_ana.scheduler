package com.example.slabiak.appointmentscheduler.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.ChatMessage;
import com.example.slabiak.appointmentscheduler.entity.Work;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;

public interface AppointmentService {
	void createNewAppointment(UUID workId, UUID providerId, UUID customerId, LocalDateTime start);

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

	List<Appointment> getAppointmentsByCustomerAtDay(UUID providerId, LocalDate day);

	List<Appointment> getConfirmedAppointmentsByCustomerId(UUID customerId);

	List<Appointment> getCanceledAppointmentsByCustomerIdForCurrentMonth(UUID userId);

	List<TimePeroid> getAvailableHours(UUID providerId, UUID customerId, UUID workId, LocalDate date);

	List<TimePeroid> calculateAvailableHours(List<TimePeroid> availableTimePeroids, Work work);

	List<TimePeroid> excludeAppointmentsFromTimePeroids(List<TimePeroid> peroids, List<Appointment> appointments);

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

	boolean isAvailable(UUID workId, UUID providerId, UUID customerId, LocalDateTime start);
}
