package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.EstadoRequestDTO;
import br.com.fiap.aiury.entities.Estado;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Contrato de servico para operações de estado.
 */
public interface EstadoService {

    Estado criarEstado(@Valid EstadoRequestDTO estadoDTO);

    Estado buscarPorId(Long id);

    List<Estado> buscarTodos(String uf);

    Estado atualizarEstado(Long id, @Valid EstadoRequestDTO estadoDTO);

    void deletarEstado(Long id);
}

