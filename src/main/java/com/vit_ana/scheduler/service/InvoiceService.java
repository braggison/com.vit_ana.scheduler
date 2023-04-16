package com.vit_ana.scheduler.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import com.vit_ana.scheduler.entity.Invoice;
import com.vit_ana.scheduler.security.CustomUserDetails;

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

