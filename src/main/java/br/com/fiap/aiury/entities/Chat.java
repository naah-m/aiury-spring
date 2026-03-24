package br.com.fiap.aiury.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidade que representa uma sessao de conversa entre usuario e ajudante.
 *
 * Papel no dominio:
 * - registra o ciclo de vida do atendimento (inicio, encerramento e status);
 * - funciona como agregador das mensagens trocadas durante a interacao.
 *
 * Observacoes de modelagem:
 * - usuario e obrigatorio para garantir autoria da sessao;
 * - ajudante esta mapeado como opcional no banco atual;
 * - lista de mensagens e relacao inversa de {@link Mensagem#chat}.
 */
@Data
@Entity
public class Chat {

    // -------------------------------------------------------------------------
    // Identificacao
    // -------------------------------------------------------------------------

    /**
     * Identificador tecnico do chat.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // -------------------------------------------------------------------------
    // Relacionamentos
    // -------------------------------------------------------------------------

    /**
     * Usuario que iniciou a sessao de acolhimento.
     */
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    /**
     * Ajudante designado para conduzir o atendimento.
     */
    @ManyToOne
    @JoinColumn(name = "id_ajudante")
    private Ajudante ajudante;

    /**
     * Colecao de mensagens pertencentes ao chat.
     *
     * Carregamento e gerenciado pelo relacionamento inverso em {@link Mensagem}.
     */
    @OneToMany(mappedBy = "chat")
    private List<Mensagem> mensagens;

    // -------------------------------------------------------------------------
    // Dados principais
    // -------------------------------------------------------------------------

    /**
     * Data/hora de inicio da sessao.
     */
    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    /**
     * Data/hora de encerramento da sessao.
     *
     * Valor pode ser nulo enquanto o chat estiver em andamento.
     */
    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    /**
     * Estado atual do ciclo de vida do chat.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ChatStatus status;
}
