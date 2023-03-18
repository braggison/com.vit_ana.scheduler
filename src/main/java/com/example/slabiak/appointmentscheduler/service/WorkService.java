package com.example.slabiak.appointmentscheduler.service;

import java.util.List;
import java.util.UUID;

import com.example.slabiak.appointmentscheduler.entity.Work;

public interface WorkService {
    void createNewWork(Work work);

    Work getWorkById(UUID workId);

    List<Work> getAllWorks();

    List<Work> getWorksByProviderId(UUID providerId);

    List<Work> getRetailCustomerWorks();

    List<Work> getCorporateCustomerWorks();

    List<Work> getWorksForRetailCustomerByProviderId(UUID providerId);

    List<Work> getWorksForCorporateCustomerByProviderId(UUID providerId);

    void updateWork(Work work);

    void deleteWorkById(UUID workId);

    boolean isWorkForCustomer(UUID workId, UUID customerId);
}
