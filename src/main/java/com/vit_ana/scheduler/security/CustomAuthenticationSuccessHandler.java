package com.vit_ana.scheduler.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.vit_ana.scheduler.service.AppointmentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final AppointmentService appointmentService;

    public CustomAuthenticationSuccessHandler(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();

        if (currentUser.hasRole("ROLE_ADMIN")) {
            appointmentService.updateAllAppointmentsStatuses();
        } else {
            appointmentService.updateUserAppointmentsStatuses(currentUser.getId());
        }
        response.sendRedirect(request.getContextPath() + "/");
    }

}
