package com.vit_ana.scheduler.service.impl;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.vit_ana.scheduler.dao.WorkRepository;
import com.vit_ana.scheduler.entity.Work;
import com.vit_ana.scheduler.entity.user.customer.Customer;
import com.vit_ana.scheduler.exception.WorkNotFoundException;
import com.vit_ana.scheduler.service.UserService;
import com.vit_ana.scheduler.service.WorkService;

import java.util.List;
import java.util.UUID;

@Service
public class WorkServiceImpl implements WorkService {

    private final WorkRepository workRepository;
    private final UserService userService;

    public WorkServiceImpl(WorkRepository workRepository, UserService userService) {
        this.workRepository = workRepository;
        this.userService = userService;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void createNewWork(Work work) {
        workRepository.save(work);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void updateWork(Work workUpdateData) {
        Work work = getWorkById(workUpdateData.getId());
        work.setName(workUpdateData.getName());
        work.setPrice(workUpdateData.getPrice());
        work.setDuration(workUpdateData.getDuration());
        work.setDescription(workUpdateData.getDescription());
        work.setEditable(workUpdateData.getEditable());
        work.setIsUseSlots(workUpdateData.getIsUseSlots());
        work.setTargetCustomer(workUpdateData.getTargetCustomer());
        workRepository.save(work);
    }

    @Override
    public Work getWorkById(UUID workId) {
        return workRepository.findById(workId).orElseThrow(WorkNotFoundException::new);
    }

    @Override
    public Boolean workExistsById(UUID workId) {
    	return workRepository.existsById(workId);
    }
    
    @Override
    public List<Work> getAllWorks() {
        return workRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteWorkById(UUID workId) {
        workRepository.deleteById(workId);
    }

    @Override
    public boolean isWorkForCustomer(UUID workId, UUID customerId) {
        Customer customer = userService.getCustomerById(customerId);
        Work work = getWorkById(workId);
        if (customer.hasRole("ROLE_CUSTOMER_RETAIL") && !work.getTargetCustomer().equals("retail")) {
            return false;
        } else return !customer.hasRole("ROLE_CUSTOMER_CORPORATE") || work.getTargetCustomer().equals("corporate");
    }

    @Override
    public List<Work> getWorksByProviderId(UUID providerId) {
        return workRepository.findByProviderId(providerId);
    }

    @Override
    public List<Work> getRetailCustomerWorks() {
        return workRepository.findByTargetCustomer("retail");
    }

    @Override
    public List<Work> getCorporateCustomerWorks() {
        return workRepository.findByTargetCustomer("corporate");
    }

    @Override
    public List<Work> getWorksForRetailCustomerByProviderId(UUID providerId) {
        return workRepository.findByTargetCustomerAndProviderId("retail", providerId);
    }

    @Override
    public List<Work> getWorksForCorporateCustomerByProviderId(UUID providerId) {
        return workRepository.findByTargetCustomerAndProviderId("corporate", providerId);
    }


}
