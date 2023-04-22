package com.vit_ana.scheduler.controller;

import java.util.UUID;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.vit_ana.scheduler.entity.WorkingPlan;
import com.vit_ana.scheduler.model.ChangePasswordForm;
import com.vit_ana.scheduler.model.TimePeriod;
import com.vit_ana.scheduler.model.UserForm;
import com.vit_ana.scheduler.security.CustomUserDetails;
import com.vit_ana.scheduler.service.AppointmentService;
import com.vit_ana.scheduler.service.UserService;
import com.vit_ana.scheduler.service.WorkService;
import com.vit_ana.scheduler.service.WorkingPlanService;
import com.vit_ana.scheduler.validation.groups.CreateProvider;
import com.vit_ana.scheduler.validation.groups.CreateUser;
import com.vit_ana.scheduler.validation.groups.UpdateProvider;
import com.vit_ana.scheduler.validation.groups.UpdateUser;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/providers")
public class ProviderController {

    private final UserService userService;
    private final WorkService workService;
    private final WorkingPlanService workingPlanService;
    private final AppointmentService appointmentService;

    public ProviderController(UserService userService, WorkService workService, WorkingPlanService workingPlanService, AppointmentService appointmentService) {
        this.userService = userService;
        this.workService = workService;
        this.workingPlanService = workingPlanService;
        this.appointmentService = appointmentService;
    }


    @GetMapping("/all")
    public String showAllProviders(Model model) {
        model.addAttribute("providers", userService.getAllProviders());
        return "users/listProviders";
    }

    @GetMapping("/{id}")
    public String showProviderDetails(@PathVariable("id") UUID providerId, Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser.getId().equals(providerId) || currentUser.hasRole("ROLE_ADMIN")) {
            if (!model.containsAttribute("user")) {
                model.addAttribute("user", new UserForm(userService.getProviderById(providerId)));
            }
            if (!model.containsAttribute("passwordChange")) {
                model.addAttribute("passwordChange", new ChangePasswordForm(providerId));
            }
            model.addAttribute("account_type", "provider");
            model.addAttribute("formActionProfile", "/providers/update/profile");
            model.addAttribute("formActionPassword", "/providers/update/password");
            model.addAttribute("allWorks", workService.getAllWorks());
            model.addAttribute("numberOfScheduledAppointments", appointmentService.getNumberOfScheduledAppointmentsForUser(providerId));
            model.addAttribute("numberOfCanceledAppointments", appointmentService.getNumberOfCanceledAppointmentsForUser(providerId));
            return "users/updateUserForm";

        } else {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
        }
    }

    @PostMapping("/update/profile")
    public String processProviderUpdate(@Validated({UpdateUser.class, UpdateProvider.class}) @ModelAttribute("user") UserForm userUpdateData, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            redirectAttributes.addFlashAttribute("user", userUpdateData);
            return "redirect:/providers/" + userUpdateData.getId();
        }
        userService.updateProviderProfile(userUpdateData);
        return "redirect:/providers/" + userUpdateData.getId();
    }

    @GetMapping("/new")
    public String showProviderRegistrationForm(Model model) {
        if (!model.containsAttribute("user")) model.addAttribute("user", new UserForm());
        model.addAttribute("account_type", "provider");
        model.addAttribute("registerAction", "/providers/new");
        model.addAttribute("allWorks", workService.getAllWorks());
        return "users/createUserForm";
    }

    @PostMapping("/new")
    public String processProviderRegistrationForm(@Validated({CreateUser.class, CreateProvider.class}) @ModelAttribute("user") UserForm userForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", bindingResult);
            redirectAttributes.addFlashAttribute("user", userForm);
            return "redirect:/providers/new";
        }
        userService.saveNewProvider(userForm);
        return "redirect:/providers/all";
    }

    @PostMapping("/delete")
    public String processDeleteProviderRequest(@RequestParam("providerId") UUID providerId) {
        userService.deleteUserById(providerId);
        return "redirect:/providers/all";
    }

    @GetMapping("/availability")
    public String showProviderAvailability(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        model.addAttribute("plan", workingPlanService.getWorkingPlanByProviderId(currentUser.getId()));
        model.addAttribute("breakModel", new TimePeriod());
        return "users/showOrUpdateProviderAvailability";
    }

    @PostMapping("/availability")
    public String processProviderWorkingPlanUpdate(@ModelAttribute("plan") WorkingPlan plan) {
        workingPlanService.updateWorkingPlan(plan);
        return "redirect:/providers/availability";
    }

    @PostMapping("/availability/breakes/add")
    public String processProviderAddBreak(@ModelAttribute("breakModel") TimePeriod breakToAdd, @RequestParam("planId") UUID planId, @RequestParam("dayOfWeek") String dayOfWeek) {
        workingPlanService.addBreakToWorkingPlan(breakToAdd, planId, dayOfWeek);
        return "redirect:/providers/availability";
    }

    @PostMapping("/availability/breakes/delete")
    public String processProviderDeleteBreak(@ModelAttribute("breakModel") TimePeriod breakToDelete, @RequestParam("planId") UUID planId, @RequestParam("dayOfWeek") String dayOfWeek) {
        workingPlanService.deleteBreakFromWorkingPlan(breakToDelete, planId, dayOfWeek);
        return "redirect:/providers/availability";
    }

    @PostMapping("/update/password")
    public String processProviderPasswordUpate(@Valid @ModelAttribute("passwordChange") ChangePasswordForm passwordChange, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passwordChange", bindingResult);
            redirectAttributes.addFlashAttribute("passwordChange", passwordChange);
            return "redirect:/providers/" + passwordChange.getId();
        }
        userService.updateUserPassword(passwordChange);
        return "redirect:/providers/" + passwordChange.getId();
    }


}
