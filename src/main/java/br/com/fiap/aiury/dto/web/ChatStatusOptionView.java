package br.com.fiap.aiury.dto.web;

import br.com.fiap.aiury.entities.ChatStatus;

public record ChatStatusOptionView(
        ChatStatus value,
        String label
) {
}
