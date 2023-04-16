package com.vit_ana.scheduler.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vit_ana.scheduler.entity.user.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByName(String roleName);
}
