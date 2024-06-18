package com.github.marshallbaby.assistant.rest;

import com.github.marshallbaby.assistant.domain.Prompt;
import com.github.marshallbaby.assistant.service.ai.Assistant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final Assistant assistant;

    @GetMapping
    public String processPrompt(@RequestBody Prompt prompt) {

        log.info("Processing prompt: [{}].", prompt);
        return assistant.chat(prompt.message());
    }

}
