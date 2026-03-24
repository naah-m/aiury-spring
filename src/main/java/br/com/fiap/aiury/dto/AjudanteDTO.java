package br.com.fiap.aiury.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de entrada para criacao e atualizacao de ajudantes.
 *
 * Papel na arquitetura:
 * - delimita o contrato recebido via API REST;
 * - aplica validacoes de consistencia antes da camada de servico.
 */
@Data
public class AjudanteDTO {

    /**
     * Area de atuacao principal do ajudante.
     */
    @NotBlank(message = "A area de atuacao e obrigatoria")
    private String areaAtuacao;

    /**
     * Texto livre sobre motivacao para acolher usuarios.
     */
    private String motivacao;

    /**
     * Flag operacional de disponibilidade para atendimentos.
     */
    @NotNull(message = "O campo disponivel e obrigatorio")
    private Boolean disponivel;

    /**
     * Nota media de avaliacao.
     */
    private Double rating;
}
