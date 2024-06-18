package com.github.marshallbaby.assistant.service.ai;

import com.github.marshallbaby.assistant.domain.ChatMessageDto;
import com.github.marshallbaby.assistant.repo.ChatMessageDtoRepository;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class PersistentChatMemory implements ChatMemoryStore {

    private final ChatMessageDtoRepository chatMessageDtoRepository;
    private final ChatMessageConverter chatMessageConverter;

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {

        return chatMessageDtoRepository.findAll(Sort.by("id"))
                .stream().map(chatMessageConverter::convertChatMessageDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {

        chatMessageDtoRepository.deleteAll();

        AtomicInteger idCounter = new AtomicInteger(1);

        List<ChatMessageDto> chatMessageDtos = messages.stream()
                .map(chatMessageConverter::convertChatMessage)
                .peek(dto -> dto.setId(idCounter.getAndIncrement()))
                .toList();

        chatMessageDtoRepository.saveAll(chatMessageDtos);
    }

    @Override
    public void deleteMessages(Object memoryId) {

        chatMessageDtoRepository.deleteAll();
    }
}
