package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.Invoice;
import com.example.slabiak.appointmentscheduler.security.CustomUserDetails;

import java.io.File;
import java.util.List;
import java.util.UUID;

public interface InvoiceService {
    void createNewInvoice(Invoice invoice);

    Invoice getInvoiceByAppointmentId(UUID appointmentId);

    Invoice getInvoiceById(UUID invoiceId);

    List<Invoice> getAllInvoices();

    void changeInvoiceStatusToPaid(UUID invoiceId);

    void issueInvoicesForConfirmedAppointments();

    String generateInvoiceNumber();

    File generatePdfForInvoice(UUID invoiceId);

    boolean isUserAllowedToDownloadInvoice(CustomUserDetails user, Invoice invoice);
}

