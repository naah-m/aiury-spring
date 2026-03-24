package br.com.fiap.aiury.entities;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidade de referencia geografica para representar cidades.
 *
 * Papel no dominio:
 * - utilizada como localizacao de usuarios;
 * - relaciona-se com {@link Estado} para manter consistencia federativa.
 *
 * Observacao de compatibilidade:
 * - anotacoes de tabela/coluna permanecem comentadas para preservar historico
 *   de modelagem legada sem alterar comportamento atual.
 */
@Data
@Entity
//@Table(name = "cidade")
public class Cidade {

    // -------------------------------------------------------------------------
    // Identificacao
    // -------------------------------------------------------------------------

    /**
     * Identificador tecnico da cidade.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "id_cidade")
    private Long id;

    // -------------------------------------------------------------------------
    // Dados principais
    // -------------------------------------------------------------------------

    /**
     * Nome oficial da cidade.
     */
    //@Column(name = "nome_cidade", nullable = false, length = 100)
    private String nomeCidade;

    // -------------------------------------------------------------------------
    // Relacionamentos
    // -------------------------------------------------------------------------

    /**
     * Estado ao qual a cidade pertence.
     */
    @ManyToOne
    //@JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;
}
