package com.vit_ana.scheduler.model;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

public class AppointmentRegisterForm {

    private UUID workId;
    private UUID providerId;
    private UUID customerId;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private OffsetDateTime start;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private OffsetDateTime end;

    public AppointmentRegisterForm() {
    }

    public AppointmentRegisterForm(UUID workId, UUID providerId, OffsetDateTime start, OffsetDateTime end) {
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

    public OffsetDateTime getStart() {
        return start;
    }

    public void setStart(OffsetDateTime start) {
        this.start = start;
    }

    public OffsetDateTime getEnd() {
        return end;
    }

    public void setEnd(OffsetDateTime end) {
        this.end = end;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }
}
