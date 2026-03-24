package br.com.fiap.aiury.dto;

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
public class MensagemDTO {

    /**
     * Identificador da mensagem (preenchido em respostas).
     */
    private Long id;

    /**
     * Identificador do chat ao qual a mensagem pertence.
     */
    @NotNull(message = "O ID do chat e obrigatorio")
    private Long chatId;

    /**
     * Identificador do usuario remetente.
     */
    @NotNull(message = "O ID do remetente e obrigatorio")
    private Long remetenteId;

    /**
     * Conteudo textual da mensagem.
     */
    @NotBlank(message = "O texto da mensagem e obrigatorio")
    @Size(max = 1000, message = "O texto da mensagem deve ter no maximo 1000 caracteres")
    private String texto;

    /**
     * Instante de envio da mensagem.
     */
    @NotNull(message = "A data de envio e obrigatoria")
    private LocalDateTime dataEnvio;
}
