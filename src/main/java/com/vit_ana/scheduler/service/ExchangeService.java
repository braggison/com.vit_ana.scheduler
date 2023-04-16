package com.vit_ana.scheduler.service;

import java.util.List;
import java.util.UUID;

import com.vit_ana.scheduler.entity.Appointment;

public interface ExchangeService {

    boolean checkIfEligibleForExchange(UUID userId, UUID appointmentId);

    List<Appointment> getEligibleAppointmentsForExchange(UUID appointmentId);

    boolean checkIfExchangeIsPossible(UUID oldAppointmentId, UUID newAppointmentId, UUID userId);

    boolean acceptExchange(UUID exchangeId, UUID userId);

    boolean rejectExchange(UUID exchangeId, UUID userId);

    boolean requestExchange(UUID oldAppointmentId, UUID newAppointmentId, UUID userId);
}
