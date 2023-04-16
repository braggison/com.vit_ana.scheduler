package com.vit_ana.scheduler.service;

public interface ScheduledTasksService {
    void updateAllAppointmentsStatuses();

    void issueInvoicesForCurrentMonth();
}
