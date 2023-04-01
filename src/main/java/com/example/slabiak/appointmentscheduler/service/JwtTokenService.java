package com.example.slabiak.appointmentscheduler.service;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.UUID;

import com.example.slabiak.appointmentscheduler.entity.Appointment;

public interface JwtTokenService {
    String generateAppointmentRejectionToken(Appointment appointment);

    String generateAcceptRejectionToken(Appointment appointment);

    boolean validateToken(String token);

    UUID getAppointmentIdFromToken(String token);

    UUID getCustomerIdFromToken(String token);

    UUID getProviderIdFromToken(String token);

    ////
    Date convertLocalDateTimeToDate(OffsetDateTime offsetDateTime);
}
