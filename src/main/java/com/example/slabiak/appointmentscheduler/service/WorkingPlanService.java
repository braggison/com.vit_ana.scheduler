package com.example.slabiak.appointmentscheduler.service;

import java.util.UUID;

import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;

public interface WorkingPlanService {
    void updateWorkingPlan(WorkingPlan workingPlan);

    void addBreakToWorkingPlan(TimePeroid breakToAdd, UUID planId, String dayOfWeek);

    void deleteBreakFromWorkingPlan(TimePeroid breakToDelete, UUID planId, String dayOfWeek);

    WorkingPlan getWorkingPlanByProviderId(UUID providerId);
}
