package br.com.fiap.aiury.dto.web;

public record DashboardSummaryView(
        long totalUsuarios,
        long totalAjudantes,
        long totalChats,
        long totalMensagens
) {
}
