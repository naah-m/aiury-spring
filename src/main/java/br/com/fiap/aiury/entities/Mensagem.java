package br.com.fiap.aiury.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade que representa uma mensagem textual dentro de um chat.
 *
 * Papel no dominio:
 * - registra cada evento de comunicacao entre participantes do acolhimento;
 * - preserva autoria, vinculo ao chat e instante de envio.
 *
 * Observacoes de persistencia:
 * - mapeada na tabela {@code TB_MENSAGEM};
 * - depende de referencias obrigatorias para chat e remetente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_MENSAGEM")
public class Mensagem {

    // -------------------------------------------------------------------------
    // Identificacao
    // -------------------------------------------------------------------------

    /**
     * Identificador tecnico da mensagem.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensagem")
    private Long id;

    // -------------------------------------------------------------------------
    // Relacionamentos
    // -------------------------------------------------------------------------

    /**
     * Chat ao qual a mensagem pertence.
     */
    @ManyToOne
    @JoinColumn(name = "id_chat", nullable = false)
    private Chat chat;

    /**
     * Usuario remetente da mensagem.
     */
    @ManyToOne
    @JoinColumn(name = "id_remetente", nullable = false)
    private Usuario remetente;

    // -------------------------------------------------------------------------
    // Conteudo e auditoria de evento
    // -------------------------------------------------------------------------

    /**
     * Conteudo textual enviado no chat.
     */
    @Column(nullable = false, length = 1000)
    private String texto;

    /**
     * Data/hora do envio da mensagem.
     */
    @Column(name = "data_envio", nullable = false)
    private LocalDateTime dataEnvio;
}
