package br.com.fiap.aiury.dto.web;

import java.time.LocalDate;

public record UsuarioListItemView(
        Long id,
        String nomeReal,
        String nomeAnonimo,
        String cidadeNome,
        String estadoUf,
        LocalDate dataCadastro
) {
}
