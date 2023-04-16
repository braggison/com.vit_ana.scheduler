package com.example.slabiak.appointmentscheduler.config;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//@Component
public class VersionInterceptor implements HandlerInterceptor {

    private BuildProperties buildProperties;

    public VersionInterceptor(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
                           final ModelAndView modelAndView) throws Exception {

        if (modelAndView != null) {
            modelAndView.getModelMap().addAttribute("currentVersion", buildProperties.getVersion());
        }
    }
}