package br.com.fiap.aiury.dto;

import br.com.fiap.aiury.entities.ChatStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO usado no transporte de dados de chat entre API e camada de servico.
 *
 * Papel na arquitetura:
 * - representa o contrato externo para abertura e manutencao de chats;
 * - evita exposicao direta da entidade com relacionamentos complexos.
 */
@Data
@Schema(name = "ChatRequestResponse", description = "Payload de chat para requisicoes e respostas")
public class ChatDTO {

    /**
     * Identificador do chat, usado principalmente em respostas.
     */
    @Schema(description = "Identificador do chat", example = "101")
    private Long id;

    /**
     * Identificador do usuario dono da sessao.
     */
    @NotNull(message = "O ID do usuario e obrigatorio")
    @Schema(description = "ID do usuario vinculado ao chat", example = "10")
    private Long usuarioId;

    /**
     * Identificador do ajudante associado ao chat.
     */
    @NotNull(message = "O ID do ajudante e obrigatorio")
    @Schema(description = "ID do ajudante vinculado ao chat", example = "3")
    private Long ajudanteId;

    /**
     * Data/hora de inicio da sessao.
     */
    @NotNull(message = "A data de inicio e obrigatoria")
    @Schema(description = "Data/hora de inicio da sessao", example = "2026-03-24T14:00:00")
    private LocalDateTime dataInicio;

    /**
     * Data/hora de encerramento (opcional enquanto o chat estiver ativo).
     */
    @Schema(description = "Data/hora de encerramento do chat", example = "2026-03-24T15:00:00", nullable = true)
    private LocalDateTime dataFim;

    /**
     * Status operacional do chat.
     */
    @NotNull(message = "O status do chat e obrigatorio")
    @Schema(description = "Status operacional do chat", example = "EM_ANDAMENTO")
    private ChatStatus status;
}
