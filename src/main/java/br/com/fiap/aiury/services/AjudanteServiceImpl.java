package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.AjudanteDTO;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.AjudanteMapper;
import br.com.fiap.aiury.repositories.AjudanteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementacao da camada de servico para o recurso de ajudante.
 *
 * Responsabilidades:
 * - coordenar mapeamento DTO para entidade;
 * - validar existencia de registros em operacoes de consulta, atualizacao e exclusao.
 */
@Service
public class AjudanteServiceImpl implements AjudanteService {

    private final AjudanteRepository ajudanteRepository;
    private final AjudanteMapper ajudanteMapper;

    @Autowired
    public AjudanteServiceImpl(AjudanteRepository ajudanteRepository, AjudanteMapper ajudanteMapper) {
        this.ajudanteRepository = ajudanteRepository;
        this.ajudanteMapper = ajudanteMapper;
    }

    /**
     * Cria ajudante novo no banco.
     */
    @Override
    @Transactional
    public Ajudante criarAjudante(AjudanteDTO ajudanteDTO) {
        Ajudante ajudante = ajudanteMapper.toEntity(ajudanteDTO);
        return ajudanteRepository.save(ajudante);
    }

    /**
     * Busca ajudante por ID com tratamento de nao encontrado.
     */
    @Override
    public Ajudante buscarPorId(Long id) {
        return ajudanteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ajudante nao encontrado com ID: " + id));
    }

    /**
     * Lista ajudantes com filtro opcional de disponibilidade.
     */
    @Override
    public List<Ajudante> buscarTodos(Boolean disponivel) {
        if (disponivel == null) {
            return ajudanteRepository.findAll();
        }
        return ajudanteRepository.findByDisponivel(disponivel);
    }

    /**
     * Atualiza registro existente com base no DTO informado.
     */
    @Override
    @Transactional
    public Ajudante atualizarAjudante(Long id, AjudanteDTO ajudanteDTO) {
        Ajudante ajudanteExistente = buscarPorId(id);
        ajudanteMapper.updateEntityFromDto(ajudanteExistente, ajudanteDTO);
        return ajudanteRepository.save(ajudanteExistente);
    }

    /**
     * Exclui ajudante por ID, validando existencia antes da remocao.
     */
    @Override
    @Transactional
    public void deletarAjudante(Long id) {
        if (!ajudanteRepository.existsById(id)) {
            throw new NotFoundException("Ajudante nao encontrado com ID: " + id);
        }
        ajudanteRepository.deleteById(id);
    }
}
