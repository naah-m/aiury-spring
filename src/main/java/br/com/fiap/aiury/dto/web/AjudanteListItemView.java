package br.com.fiap.aiury.dto.web;

public record AjudanteListItemView(
        Long id,
        String areaAtuacao,
        String login,
        boolean disponivel,
        Double rating
) {
}
