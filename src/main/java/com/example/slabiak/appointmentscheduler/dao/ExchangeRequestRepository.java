package com.example.slabiak.appointmentscheduler.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.slabiak.appointmentscheduler.entity.ExchangeRequest;

public interface ExchangeRequestRepository extends JpaRepository<ExchangeRequest, UUID> {
}
