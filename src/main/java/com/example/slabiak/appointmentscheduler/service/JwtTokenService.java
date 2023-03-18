package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.Appointment;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public interface JwtTokenService {
    String generateAppointmentRejectionToken(Appointment appointment);

    String generateAcceptRejectionToken(Appointment appointment);

    boolean validateToken(String token);

    UUID getAppointmentIdFromToken(String token);

    UUID getCustomerIdFromToken(String token);

    UUID getProviderIdFromToken(String token);

    ////
    Date convertLocalDateTimeToDate(LocalDateTime localDateTime);
}
