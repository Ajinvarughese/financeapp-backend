package com.project.finance_api.dto;

public record AiChatRequest(
    String text,
    Long userId
) {}
