package com.vit_ana.scheduler.service.impl;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.vit_ana.scheduler.dao.WorkingPlanRepository;
import com.vit_ana.scheduler.entity.WorkingPlan;
import com.vit_ana.scheduler.model.TimePeroid;
import com.vit_ana.scheduler.security.CustomUserDetails;
import com.vit_ana.scheduler.service.WorkingPlanService;

@Service
public class WorkingPlanServiceImpl implements WorkingPlanService {

    private final WorkingPlanRepository workingPlanRepository;

    public WorkingPlanServiceImpl(WorkingPlanRepository workingPlanRepository) {
        this.workingPlanRepository = workingPlanRepository;
    }

    @Override
    public void createWorkingPlan(WorkingPlan workingPlan) {
        workingPlanRepository.save(workingPlan);
    }
    
    @Override
    @PreAuthorize("#updateData.provider.id == principal.id")
    public void updateWorkingPlan(WorkingPlan updateData) {
        WorkingPlan workingPlan = workingPlanRepository.getOne(updateData.getId());
        workingPlan.getMonday().setWorkingHours(updateData.getMonday().getWorkingHours());
        workingPlan.getTuesday().setWorkingHours(updateData.getTuesday().getWorkingHours());
        workingPlan.getWednesday().setWorkingHours(updateData.getWednesday().getWorkingHours());
        workingPlan.getThursday().setWorkingHours(updateData.getThursday().getWorkingHours());
        workingPlan.getFriday().setWorkingHours(updateData.getFriday().getWorkingHours());
        workingPlan.getSaturday().setWorkingHours(updateData.getSaturday().getWorkingHours());
        workingPlan.getSunday().setWorkingHours(updateData.getSunday().getWorkingHours());
        workingPlanRepository.save(workingPlan);
    }

    @Override
    public void addBreakToWorkingPlan(TimePeroid breakToAdd, UUID planId, String dayOfWeek) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WorkingPlan workingPlan = workingPlanRepository.getOne(planId);
        if (workingPlan.getProvider().getId().equals(currentUser.getId())) {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
        }
        workingPlan.getDay(dayOfWeek).getBreaks().add(breakToAdd);
        workingPlanRepository.save(workingPlan);
    }

    @Override
    public void deleteBreakFromWorkingPlan(TimePeroid breakToDelete, UUID planId, String dayOfWeek) {
        CustomUserDetails currentUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WorkingPlan workingPlan = workingPlanRepository.getOne(planId);
        if (workingPlan.getProvider().getId().equals(currentUser.getId())) {
            throw new org.springframework.security.access.AccessDeniedException("Unauthorized");
        }
        workingPlan.getDay(dayOfWeek).getBreaks().remove(breakToDelete);
        workingPlanRepository.save(workingPlan);
    }


    @Override
    @PreAuthorize("#providerId == principal.id")
    public WorkingPlan getWorkingPlanByProviderId(UUID providerId) {
        return workingPlanRepository.getWorkingPlanByProviderId(providerId);
    }


}
