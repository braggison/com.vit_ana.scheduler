package com.vit_ana.scheduler.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vit_ana.scheduler.entity.ChatMessage;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

}
