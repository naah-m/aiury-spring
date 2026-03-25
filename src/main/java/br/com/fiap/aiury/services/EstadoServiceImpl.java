package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.EstadoRequestDTO;
import br.com.fiap.aiury.entities.Estado;
import br.com.fiap.aiury.exceptions.ConflictException;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.EstadoMapper;
import br.com.fiap.aiury.repositories.EstadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementacao da camada de servico para o catalogo de estados.
 */
@Service
public class EstadoServiceImpl implements EstadoService {

    private final EstadoRepository estadoRepository;
    private final EstadoMapper estadoMapper;

    public EstadoServiceImpl(EstadoRepository estadoRepository, EstadoMapper estadoMapper) {
        this.estadoRepository = estadoRepository;
        this.estadoMapper = estadoMapper;
    }

    @Override
    @Transactional
    public Estado criarEstado(EstadoRequestDTO estadoDTO) {
        validarDuplicidade(estadoDTO.getNomeEstado(), estadoDTO.getUf(), null);
        Estado estado = estadoMapper.toEntity(estadoDTO);
        return estadoRepository.save(estado);
    }

    @Override
    public Estado buscarPorId(Long id) {
        return estadoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Estado nao encontrado com ID: " + id));
    }

    @Override
    public List<Estado> buscarTodos(String uf) {
        if (uf == null || uf.isBlank()) {
            return estadoRepository.findAllByOrderByNomeEstadoAsc();
        }
        return estadoRepository.findByUfIgnoreCase(uf).stream().toList();
    }

    @Override
    @Transactional
    public Estado atualizarEstado(Long id, EstadoRequestDTO estadoDTO) {
        Estado estadoExistente = buscarPorId(id);
        validarDuplicidade(estadoDTO.getNomeEstado(), estadoDTO.getUf(), estadoExistente);
        estadoMapper.updateEntityFromDto(estadoExistente, estadoDTO);
        return estadoRepository.save(estadoExistente);
    }

    @Override
    @Transactional
    public void deletarEstado(Long id) {
        if (!estadoRepository.existsById(id)) {
            throw new NotFoundException("Estado nao encontrado com ID: " + id);
        }
        estadoRepository.deleteById(id);
    }

    private void validarDuplicidade(String nomeEstado, String uf, Estado estadoExistente) {
        if (nomeEstado != null && estadoRepository.existsByNomeEstadoIgnoreCase(nomeEstado)
                && (estadoExistente == null || !estadoExistente.getNomeEstado().equalsIgnoreCase(nomeEstado))) {
            throw new ConflictException("Ja existe estado cadastrado com nome: " + nomeEstado);
        }

        if (uf != null && estadoRepository.existsByUfIgnoreCase(uf)
                && (estadoExistente == null || !estadoExistente.getUf().equalsIgnoreCase(uf))) {
            throw new ConflictException("Ja existe estado cadastrado com UF: " + uf.toUpperCase());
        }
    }
}
