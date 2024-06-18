package com.github.marshallbaby.assistant.service.ai;

import com.github.marshallbaby.assistant.domain.ChatMessageDto;
import dev.langchain4j.data.message.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class ChatMessageConverter {

    public ChatMessage convertChatMessageDto(ChatMessageDto chatMessageDto) {

        return chatMessageDto.getIsUserMessage() ?
                convertUserMessage(chatMessageDto) :
                convertAiMessage(chatMessageDto);
    }

    public ChatMessageDto convertChatMessage(ChatMessage chatMessage) {

        return ChatMessageDto.builder()
                .isUserMessage(chatMessage.type() == ChatMessageType.USER)
                .payload(chatMessage.text())
                .build();
    }

    private ChatMessage convertUserMessage(ChatMessageDto chatMessageDto) {
        return new UserMessage(Collections.singletonList(
                new TextContent(chatMessageDto.getPayload())
        ));
    }

    private ChatMessage convertAiMessage(ChatMessageDto chatMessageDto) {
        return new AiMessage(chatMessageDto.getPayload());
    }

}
