package br.com.fiap.aiury.dto.web;

public record ChatStatusStepView(
        String titulo,
        boolean concluido,
        boolean atual
) {
}
