package com.example.slabiak.appointmentscheduler.controller;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.model.AppointmentRegisterForm;
import com.example.slabiak.appointmentscheduler.security.CustomUserDetails;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import com.example.slabiak.appointmentscheduler.service.NotificationService;
import com.google.common.collect.Lists;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class AjaxController {

    private final AppointmentService appointmentService;
    private final NotificationService notificationService;

    public AjaxController(AppointmentService appointmentService, NotificationService notificationService) {
        this.appointmentService = appointmentService;
        this.notificationService = notificationService;
    }


    @GetMapping("/user/{userId}/appointments")
    public List<Appointment> getAppointmentsForUser(@PathVariable("userId") UUID userId, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser.hasRole("ROLE_CUSTOMER")) {
            return appointmentService.getAppointmentByCustomerId(userId);
        } else if (currentUser.hasRole("ROLE_PROVIDER"))
            return appointmentService.getAppointmentByProviderId(userId);
        else if (currentUser.hasRole("ROLE_ADMIN"))
            return appointmentService.getAllAppointments();
        else return Lists.newArrayList();
    }

    @GetMapping("/availableHours/{providerId}/{workId}/{date}")
    public List<AppointmentRegisterForm> getAvailableHours(@PathVariable("providerId") UUID providerId, @PathVariable("workId") UUID workId, @PathVariable("date") String date, @AuthenticationPrincipal CustomUserDetails currentUser) {
    	OffsetDateTime offsetDateTime = OffsetDateTime.parse(date);
        return appointmentService.getAvailableHours(providerId, currentUser.getId(), workId, offsetDateTime)
                .stream()
                .map(timePeriod -> new AppointmentRegisterForm(workId, providerId, timePeriod.getStart().atDate(offsetDateTime.toLocalDate()), timePeriod.getEnd().atDate(offsetDateTime.toLocalDate())))
                .collect(Collectors.toList());
    }

    @GetMapping("/user/notifications")
    public int getUnreadNotificationsCount(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return notificationService.getUnreadNotifications(currentUser.getId()).size();
    }

}
