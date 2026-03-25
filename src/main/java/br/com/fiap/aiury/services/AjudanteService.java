package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.AjudanteDTO;
import br.com.fiap.aiury.entities.Ajudante;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Contrato de servicos para gerenciamento de ajudantes.
 */
public interface AjudanteService {

    /**
     * Cria novo ajudante.
     *
     * @param ajudanteDTO dados de entrada validados
     * @return entidade criada
     */
    Ajudante criarAjudante(@Valid AjudanteDTO ajudanteDTO);

    /**
     * Busca ajudante por ID.
     *
     * @param id identificador do ajudante
     * @return ajudante encontrado
     */
    Ajudante buscarPorId(Long id);

    /**
     * Lista ajudantes com filtro opcional por disponibilidade.
     *
     * @param disponivel filtro opcional por disponibilidade
     * @return colecao de ajudantes
     */
    List<Ajudante> buscarTodos(Boolean disponivel);

    /**
     * Atualiza dados de ajudante existente.
     *
     * @param id identificador do registro alvo
     * @param ajudanteDTO novos dados
     * @return ajudante atualizado
     */
    Ajudante atualizarAjudante(Long id, @Valid AjudanteDTO ajudanteDTO);

    /**
     * Remove ajudante por ID.
     *
     * @param id identificador do ajudante
     */
    void deletarAjudante(Long id);
}
