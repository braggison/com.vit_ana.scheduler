package com.vit_ana.scheduler.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.vit_ana.scheduler.entity.Appointment;
import com.vit_ana.scheduler.security.CustomUserDetails;
import com.vit_ana.scheduler.service.AppointmentService;
import com.vit_ana.scheduler.service.ExchangeService;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;
    private final AppointmentService appointmentService;

    public ExchangeController(ExchangeService exchangeService, AppointmentService appointmentService) {
        this.exchangeService = exchangeService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/{oldAppointmentId}")
    public String showEligibleAppointmentsToExchange(@PathVariable("oldAppointmentId") UUID oldAppointmentId, Model model) {
        List<Appointment> eligibleAppointments = exchangeService.getEligibleAppointmentsForExchange(oldAppointmentId);
        model.addAttribute("appointmentId", oldAppointmentId);
        model.addAttribute("numberOfEligibleAppointments", eligibleAppointments.size());
        model.addAttribute("eligibleAppointments", eligibleAppointments);
        return "exchange/listProposals";
    }

    @GetMapping("/{oldAppointmentId}/{newAppointmentId}")
    public String showExchangeSummaryScreen(@PathVariable("oldAppointmentId") UUID oldAppointmentId, @PathVariable("newAppointmentId") UUID newAppointmentId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (exchangeService.checkIfExchangeIsPossible(oldAppointmentId, newAppointmentId, currentUser.getId())) {
            model.addAttribute("oldAppointment", appointmentService.getAppointmentByIdWithAuthorization(oldAppointmentId));
            model.addAttribute("newAppointment", appointmentService.getAppointmentById(newAppointmentId));
        } else {
            return "redirect:/appointments/all";
        }

        return "exchange/exchangeSummary";
    }

    @PostMapping()
    public String processExchangeRequest(@RequestParam("oldAppointmentId") UUID oldAppointmentId, @RequestParam("newAppointmentId") UUID newAppointmentId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        boolean result = exchangeService.requestExchange(oldAppointmentId, newAppointmentId, currentUser.getId());
        if (result) {
            model.addAttribute("message", "Exchange request sucsessfully sent!");
        } else {
            model.addAttribute("message", "Error! Exchange not sent!");
        }
        return "exchange/requestConfirmation";
    }

    @PostMapping("/accept")
    public String processExchangeAcceptation(@RequestParam("exchangeId") UUID exchangeId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        exchangeService.acceptExchange(exchangeId, currentUser.getId());
        return "redirect:/appointments/all";
    }

    @PostMapping("/reject")
    public String processExchangeRejection(@RequestParam("exchangeId") UUID exchangeId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        exchangeService.rejectExchange(exchangeId, currentUser.getId());
        return "redirect:/appointments/all";
    }
}
