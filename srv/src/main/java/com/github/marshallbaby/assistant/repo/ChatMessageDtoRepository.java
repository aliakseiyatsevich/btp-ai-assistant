package com.github.marshallbaby.assistant.repo;

import com.github.marshallbaby.assistant.domain.ChatMessageDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageDtoRepository extends JpaRepository<ChatMessageDto, Integer> {
}
