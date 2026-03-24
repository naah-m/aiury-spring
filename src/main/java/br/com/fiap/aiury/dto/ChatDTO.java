package br.com.fiap.aiury.dto;

import br.com.fiap.aiury.entities.ChatStatus;
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
public class ChatDTO {

    /**
     * Identificador do chat, usado principalmente em respostas.
     */
    private Long id;

    /**
     * Identificador do usuario dono da sessao.
     */
    @NotNull(message = "O ID do usuario e obrigatorio")
    private Long usuarioId;

    /**
     * Identificador do ajudante associado ao chat.
     */
    @NotNull(message = "O ID do ajudante e obrigatorio")
    private Long ajudanteId;

    /**
     * Data/hora de inicio da sessao.
     */
    @NotNull(message = "A data de inicio e obrigatoria")
    private LocalDateTime dataInicio;

    /**
     * Data/hora de encerramento (opcional enquanto o chat estiver ativo).
     */
    private LocalDateTime dataFim;

    /**
     * Status operacional do chat.
     */
    @NotNull(message = "O status do chat e obrigatorio")
    private ChatStatus status;
}
