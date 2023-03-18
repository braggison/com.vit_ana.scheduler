package com.example.slabiak.appointmentscheduler.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.slabiak.appointmentscheduler.entity.user.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByName(String roleName);
}
