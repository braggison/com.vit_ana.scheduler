package com.vit_ana.scheduler.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "exchanges")
public class ExchangeRequest extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "exchange_status")
    private ExchangeStatus status;

    @OneToOne
    @JoinColumn(name = "id_appointment_requestor")
    private Appointment requestor;

    @OneToOne
    @JoinColumn(name = "id_appointment_requested")
    private Appointment requested;


    public ExchangeRequest() {
    	super(UUID.randomUUID());
    }

    public ExchangeRequest(Appointment requestor, Appointment requested, ExchangeStatus status) {
    	super(UUID.randomUUID());
        this.status = status;
        this.requestor = requestor;
        this.requested = requested;
    }

    public ExchangeStatus getStatus() {
        return status;
    }

    public void setStatus(ExchangeStatus status) {
        this.status = status;
    }

    public Appointment getRequestor() {
        return requestor;
    }

    public void setRequestor(Appointment requestor) {
        this.requestor = requestor;
    }

    public Appointment getRequested() {
        return requested;
    }

    public void setRequested(Appointment requested) {
        this.requested = requested;
    }
}
