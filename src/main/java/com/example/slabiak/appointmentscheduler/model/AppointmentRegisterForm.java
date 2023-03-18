package com.example.slabiak.appointmentscheduler.model;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public class AppointmentRegisterForm {

    private UUID workId;
    private UUID providerId;
    private UUID customerId;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime start;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime end;

    public AppointmentRegisterForm() {
    }

    public AppointmentRegisterForm(UUID workId, UUID providerId, LocalDateTime start, LocalDateTime end) {
        this.workId = workId;
        this.providerId = providerId;
        this.start = start;
        this.end = end;
    }

    public UUID getWorkId() {
        return workId;
    }

    public void setWorkId(UUID workId) {
        this.workId = workId;
    }

    public UUID getProviderId() {
        return providerId;
    }

    public void setProviderId(UUID providerId) {
        this.providerId = providerId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }
}
