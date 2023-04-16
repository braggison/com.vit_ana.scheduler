package com.vit_ana.scheduler.dao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vit_ana.scheduler.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    @Query("select N from Notification N join N.user u where u.id = :userId and N.isRead=false")
    List<Notification> getAllUnreadNotifications(@Param("userId") UUID userId);
}
