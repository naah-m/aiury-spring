package br.com.fiap.aiury.entities;

/**
 * Enum que define os estados possiveis do ciclo de vida de um chat.
 *
 * Uso no dominio:
 * - controla o acompanhamento operacional da sessao de acolhimento;
 * - e persistido como {@code String} na entidade {@link Chat}.
 */
public enum ChatStatus {
    /** Sessao criada e pronta para iniciar interacao. */
    INICIADO,

    /** Sessao com troca ativa de mensagens entre participantes. */
    EM_ANDAMENTO,

    /** Sessao encerrada por decisao da pessoa usuaria. */
    FINALIZADO_USUARIO,

    /** Sessao encerrada por decisao do ajudante. */
    FINALIZADO_AJUDANTE,

    /** Sessao finalizada por criterio tecnico/sistemico. */
    FINALIZADO_SISTEMA
}
