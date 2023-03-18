package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.Appointment;

import java.util.List;
import java.util.UUID;

public interface ExchangeService {

    boolean checkIfEligibleForExchange(UUID userId, UUID appointmentId);

    List<Appointment> getEligibleAppointmentsForExchange(UUID appointmentId);

    boolean checkIfExchangeIsPossible(UUID oldAppointmentId, UUID newAppointmentId, UUID userId);

    boolean acceptExchange(UUID exchangeId, UUID userId);

    boolean rejectExchange(UUID exchangeId, UUID userId);

    boolean requestExchange(UUID oldAppointmentId, UUID newAppointmentId, UUID userId);
}
