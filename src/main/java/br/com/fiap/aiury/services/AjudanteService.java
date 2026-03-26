package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.AjudanteRequestDTO;
import br.com.fiap.aiury.entities.Ajudante;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Contrato de servicos para gerenciamento de ajudantes.
 */
public interface AjudanteService {

    Ajudante criarAjudante(@Valid AjudanteRequestDTO ajudanteDTO);

    Ajudante buscarPorId(Long id);

    List<Ajudante> buscarTodos(Boolean disponivel);

    Ajudante atualizarAjudante(Long id, @Valid AjudanteRequestDTO ajudanteDTO);

    void deletarAjudante(Long id);

    void alterarSenha(Long id, String senhaAtual, String novaSenha);
}
