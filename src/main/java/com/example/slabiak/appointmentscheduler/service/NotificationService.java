package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.*;
import com.example.slabiak.appointmentscheduler.entity.user.User;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    void newNotification(String title, String message, String url, User user);

    void markAsRead(UUID notificationId, UUID userId);

    void markAllAsRead(UUID userId);

    Notification getNotificationById(UUID notificationId);

    List<Notification> getAll(UUID userId);

    List<Notification> getUnreadNotifications(UUID userId);

    void newAppointmentFinishedNotification(Appointment appointment, boolean sendEmail);

    void newAppointmentRejectionRequestedNotification(Appointment appointment, boolean sendEmail);

    void newNewAppointmentScheduledNotification(Appointment appointment, boolean sendEmail);

    void newAppointmentCanceledByCustomerNotification(Appointment appointment, boolean sendEmail);

    void newAppointmentCanceledByProviderNotification(Appointment appointment, boolean sendEmail);

    void newAppointmentRejectionAcceptedNotification(Appointment appointment, boolean sendEmail);

    void newChatMessageNotification(ChatMessage chatMessage, boolean sendEmail);

    void newInvoice(Invoice invoice, boolean sendEmail);

    void newExchangeRequestedNotification(Appointment oldAppointment, Appointment newAppointment, boolean sendEmail);

    void newExchangeAcceptedNotification(ExchangeRequest exchangeRequest, boolean sendEmail);

    void newExchangeRejectedNotification(ExchangeRequest exchangeRequest, boolean sendEmail);
}
