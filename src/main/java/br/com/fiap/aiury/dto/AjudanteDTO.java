package br.com.fiap.aiury.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "AjudanteRequest", description = "Payload para criacao/atualizacao de ajudante")
public class AjudanteDTO {

    /**
     * Area de atuacao principal do ajudante.
     */
    @NotBlank(message = "A area de atuacao e obrigatoria")
    @Schema(description = "Area principal de atuacao", example = "Escuta ativa")
    private String areaAtuacao;

    /**
     * Texto livre sobre motivacao para acolher usuarios.
     */
    @Schema(description = "Texto de motivacao para atuar na plataforma", example = "Atuo como voluntario em apoio emocional")
    private String motivacao;

    /**
     * Flag operacional de disponibilidade para atendimentos.
     */
    @NotNull(message = "O campo disponivel e obrigatorio")
    @Schema(description = "Indica se o ajudante esta disponivel para novos chats", example = "true")
    private Boolean disponivel;

    /**
     * Nota media de avaliacao.
     */
    @Schema(description = "Avaliacao media recebida", example = "4.7")
    private Double rating;
}
