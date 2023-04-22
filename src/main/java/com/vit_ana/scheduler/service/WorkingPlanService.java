package com.vit_ana.scheduler.service;

import java.util.UUID;

import com.vit_ana.scheduler.entity.WorkingPlan;
import com.vit_ana.scheduler.model.TimePeriod;

public interface WorkingPlanService {
    void createWorkingPlan(WorkingPlan workingPlan);
	
    void updateWorkingPlan(WorkingPlan workingPlan);

    void addBreakToWorkingPlan(TimePeriod breakToAdd, UUID planId, String dayOfWeek);

    void deleteBreakFromWorkingPlan(TimePeriod breakToDelete, UUID planId, String dayOfWeek);

    WorkingPlan getWorkingPlanByProviderId(UUID providerId);
}
