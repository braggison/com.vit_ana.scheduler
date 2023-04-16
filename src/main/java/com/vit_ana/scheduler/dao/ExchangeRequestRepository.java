package com.vit_ana.scheduler.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vit_ana.scheduler.entity.ExchangeRequest;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, UUID> {
}
