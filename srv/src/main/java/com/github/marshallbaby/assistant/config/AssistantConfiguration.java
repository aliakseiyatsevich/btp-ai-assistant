package com.github.marshallbaby.assistant.config;

import com.github.marshallbaby.assistant.service.ai.Assistant;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AssistantConfiguration {

    @Bean
    public ChatMemory chatMemory(ChatMemoryStore chatMemoryStore) {

        return MessageWindowChatMemory.builder()
                .maxMessages(10)
                .chatMemoryStore(chatMemoryStore)
                .build();
    }

    @Bean
    public Assistant assistant(ChatMemory chatMemory, OpenAiChatModel openAiChatModel) {

        return AiServices.builder(Assistant.class)
                .chatLanguageModel(openAiChatModel)
                .chatMemory(chatMemory)
                .build();
    }

}
