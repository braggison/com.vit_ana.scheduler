package com.vit_ana.scheduler.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.vit_ana.scheduler.security.CustomUserDetails;
import com.vit_ana.scheduler.service.UserService;

@Controller
public class HomeController {

    private final UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String showHome(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        model.addAttribute("user", userService.getUserById(currentUser.getId()));
        return "home";
    }

    @GetMapping("/login")
    public String login(Model model, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser != null) {
            return "redirect:/";
        }
        return "users/login";
    }

    @GetMapping("/access-denied")
    public String showAccessDeniedPage() {
        return "access-denied";
    }


}
