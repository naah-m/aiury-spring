package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.AjudanteDTO;
import br.com.fiap.aiury.entities.Ajudante;
import jakarta.validation.Valid;

import java.util.List;

public interface AjudanteService {

    Ajudante criarAjudante(@Valid AjudanteDTO ajudanteDTO);

    Ajudante buscarPorId(Long id);

    List<Ajudante> buscarTodos();

    Ajudante atualizarAjudante(Long id, @Valid AjudanteDTO ajudanteDTO);

    void deletarAjudante(Long id);
}
