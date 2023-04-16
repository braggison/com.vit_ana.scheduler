package com.vit_ana.scheduler.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vit_ana.scheduler.entity.Work;

public interface WorkRepository extends JpaRepository<Work, UUID> {
    @Query("select w from Work w inner join w.providers p where p.id in :providerId")
    List<Work> findByProviderId(@Param("providerId") UUID providerId);

    @Query("select w from Work w where w.targetCustomer = :target ")
    List<Work> findByTargetCustomer(@Param("target") String targetCustomer);

    @Query("select w from Work w inner join w.providers p where p.id in :providerId and w.targetCustomer = :target ")
    List<Work> findByTargetCustomerAndProviderId(@Param("target") String targetCustomer, @Param("providerId") UUID providerId);
}
