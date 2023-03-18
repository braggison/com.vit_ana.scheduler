package com.example.slabiak.appointmentscheduler.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;

public interface WorkingPlanRepository extends JpaRepository<WorkingPlan, UUID> {
    @Query("select w from WorkingPlan w where w.provider.id = :providerId")
    WorkingPlan getWorkingPlanByProviderId(@Param("providerId") UUID providerId);
}
