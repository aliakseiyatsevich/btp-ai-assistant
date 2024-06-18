package com.github.marshallbaby.assistant.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChatMessageDto {

    @Id
    private Integer id;
    private String payload;
    private Boolean isUserMessage;

}
