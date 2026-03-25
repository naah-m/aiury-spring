package br.com.fiap.aiury.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO de transporte para operacoes REST de mensagens.
 *
 * Papel na arquitetura:
 * - define o contrato de entrada/saida das mensagens no endpoint;
 * - centraliza validacoes de obrigatoriedade e limite de tamanho do texto.
 */
@Data
@Schema(name = "MensagemRequestResponse", description = "Payload de mensagem para requisicoes e respostas")
public class MensagemDTO {

    /**
     * Identificador da mensagem (preenchido em respostas).
     */
    @Schema(description = "Identificador da mensagem", example = "700")
    private Long id;

    /**
     * Identificador do chat ao qual a mensagem pertence.
     */
    @NotNull(message = "O ID do chat e obrigatorio")
    @Schema(description = "ID do chat relacionado", example = "101")
    private Long chatId;

    /**
     * Identificador do usuario remetente.
     */
    @NotNull(message = "O ID do remetente e obrigatorio")
    @Schema(description = "ID do usuario remetente", example = "10")
    private Long remetenteId;

    /**
     * Conteudo textual da mensagem.
     */
    @NotBlank(message = "O texto da mensagem e obrigatorio")
    @Size(max = 1000, message = "O texto da mensagem deve ter no maximo 1000 caracteres")
    @Schema(description = "Texto enviado na conversa", example = "Obrigado por me ouvir hoje.")
    private String texto;

    /**
     * Instante de envio da mensagem.
     */
    @NotNull(message = "A data de envio e obrigatoria")
    @Schema(description = "Data/hora do envio da mensagem", example = "2026-03-24T14:12:00")
    private LocalDateTime dataEnvio;
}
