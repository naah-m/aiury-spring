package br.com.fiap.aiury.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Entidade de referencia para unidades federativas (estado/UF).
 *
 * Papel no dominio:
 * - fornece catalogo de estados para composicao de endereco;
 * - e utilizada indiretamente por usuarios via entidade {@link Cidade}.
 *
 * Observacao de compatibilidade:
 * - anotacoes de tabela/coluna comentadas foram preservadas para historico
 *   de nomenclatura legada sem impacto no mapeamento atual.
 */
@Data
@Entity
//@Table(name = "estado")
public class Estado {

    // -------------------------------------------------------------------------
    // Identificacao
    // -------------------------------------------------------------------------

    /**
     * Identificador tecnico do estado.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "id_estado")
    private Long id;

    // -------------------------------------------------------------------------
    // Dados principais
    // -------------------------------------------------------------------------

    /**
     * Nome descritivo do estado.
     */
    //@Column(name = "nome_estado", nullable = false, length = 50, unique = true)
    private String nomeEstado;

    /**
     * Sigla federativa (UF) com 2 caracteres.
     */
    //@Column(name = "uf", nullable = false, length = 2, unique = true)
    private String uf;
}
