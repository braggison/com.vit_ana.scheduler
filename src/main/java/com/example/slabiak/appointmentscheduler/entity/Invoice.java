package com.example.slabiak.appointmentscheduler.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

// statuses: issued,paid

@Entity
@Table(name = "invoices")
public class Invoice extends BaseEntity {

    @Column(name = "number")
    private String number;

    @Column(name = "status")
    private String status;

    @Column(name = "total_amount")
    private double totalAmount;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @Column(name = "issued")
    private LocalDateTime issued;

    @OneToMany(mappedBy = "invoice")
    private List<Appointment> appointments;

    public Invoice() {
    	super(UUID.randomUUID());
    }

    public Invoice(String number, String status, LocalDateTime issued, List<Appointment> appointments2) {
    	super(UUID.randomUUID());
        this.number = number;
        this.status = status;
        this.issued = issued;
        this.appointments = new ArrayList<>();
        for (Appointment a : appointments2) {
            this.appointments.add(a);
            a.setInvoice(this);
            totalAmount += a.getWork().getPrice();
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getIssued() {
        return issued;
    }

    public void setIssued(LocalDateTime issued) {
        this.issued = issued;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }
}
