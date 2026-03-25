package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.CidadeRequestDTO;
import br.com.fiap.aiury.entities.Cidade;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Contrato de servico para operacoes de cidade.
 */
public interface CidadeService {

    Cidade criarCidade(@Valid CidadeRequestDTO cidadeDTO);

    Cidade buscarPorId(Long id);

    List<Cidade> buscarTodos(Long estadoId);

    Cidade atualizarCidade(Long id, @Valid CidadeRequestDTO cidadeDTO);

    void deletarCidade(Long id);
}
