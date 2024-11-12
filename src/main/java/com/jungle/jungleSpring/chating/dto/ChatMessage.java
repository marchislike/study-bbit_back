package com.jungle.jungleSpring.chating.dto;

import lombok.Getter;

@Getter
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

    // Getters and setters
}