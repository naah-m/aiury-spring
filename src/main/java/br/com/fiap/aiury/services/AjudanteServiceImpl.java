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

@Service
public class AjudanteServiceImpl implements AjudanteService {

    private final AjudanteRepository ajudanteRepository;
    private final AjudanteMapper ajudanteMapper;

    @Autowired
    public AjudanteServiceImpl(AjudanteRepository ajudanteRepository, AjudanteMapper ajudanteMapper) {
        this.ajudanteRepository = ajudanteRepository;
        this.ajudanteMapper = ajudanteMapper;
    }

    @Override
    @Transactional
    public Ajudante criarAjudante(AjudanteDTO ajudanteDTO) {
        Ajudante ajudante = ajudanteMapper.toEntity(ajudanteDTO);
        return ajudanteRepository.save(ajudante);
    }

    @Override
    public Ajudante buscarPorId(Long id) {
        return ajudanteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ajudante nao encontrado com ID: " + id));
    }

    @Override
    public List<Ajudante> buscarTodos() {
        return ajudanteRepository.findAll();
    }

    @Override
    @Transactional
    public Ajudante atualizarAjudante(Long id, AjudanteDTO ajudanteDTO) {
        Ajudante ajudanteExistente = buscarPorId(id);
        ajudanteMapper.updateEntityFromDto(ajudanteExistente, ajudanteDTO);
        return ajudanteRepository.save(ajudanteExistente);
    }

    @Override
    @Transactional
    public void deletarAjudante(Long id) {
        if (!ajudanteRepository.existsById(id)) {
            throw new NotFoundException("Ajudante nao encontrado com ID: " + id);
        }
        ajudanteRepository.deleteById(id);
    }
}
