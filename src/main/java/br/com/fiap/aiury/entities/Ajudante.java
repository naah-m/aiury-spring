package br.com.fiap.aiury.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa o perfil de ajudante no dominio.
 *
 * Papel no dominio:
 * - armazena informacoes do voluntario/profissional que oferece acolhimento;
 * - pode ser vinculado a um ou mais chats de atendimento.
 *
 * Observacoes de persistencia:
 * - mapeada na tabela {@code TB_AJUDANTE};
 * - utiliza {@link Lob} para suportar textos longos no campo de motivacao.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_AJUDANTE")
public class Ajudante {

    // -------------------------------------------------------------------------
    // Identificacao
    // -------------------------------------------------------------------------

    /**
     * Identificador tecnico do ajudante.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // -------------------------------------------------------------------------
    // Dados principais
    // -------------------------------------------------------------------------

    /**
     * Especialidade ou area de atuacao declarada.
     */
    @Column(name = "area_atuacao", length = 100, nullable = false)
    private String areaAtuacao;

    /**
     * Texto livre com motivacao para atuar no acolhimento.
     */
    @Lob
    @Column(name = "motivacao")
    private String motivacao;

    /**
     * Indicador operacional de disponibilidade.
     */
    @Column(name = "disponivel", nullable = false)
    private boolean disponivel;

    /**
     * Avaliacao media recebida ao longo dos atendimentos.
     */
    @Column(name = "rating")
    private Double rating;
}
