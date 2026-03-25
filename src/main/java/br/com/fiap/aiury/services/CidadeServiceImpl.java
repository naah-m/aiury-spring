package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.CidadeRequestDTO;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Estado;
import br.com.fiap.aiury.exceptions.ConflictException;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.CidadeMapper;
import br.com.fiap.aiury.repositories.CidadeRepository;
import br.com.fiap.aiury.repositories.EstadoRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementacao da camada de servico para o catalogo de cidades.
 */
@Service
public class CidadeServiceImpl implements CidadeService {

    private final CidadeRepository cidadeRepository;
    private final EstadoRepository estadoRepository;
    private final CidadeMapper cidadeMapper;

    public CidadeServiceImpl(CidadeRepository cidadeRepository,
                             EstadoRepository estadoRepository,
                             CidadeMapper cidadeMapper) {
        this.cidadeRepository = cidadeRepository;
        this.estadoRepository = estadoRepository;
        this.cidadeMapper = cidadeMapper;
    }

    @Override
    @Transactional
    public Cidade criarCidade(CidadeRequestDTO cidadeDTO) {
        Estado estado = buscarEstadoPorId(cidadeDTO.getEstadoId());
        validarDuplicidade(cidadeDTO.getNomeCidade(), estado.getId(), null);
        Cidade cidade = cidadeMapper.toEntity(cidadeDTO, estado);
        return cidadeRepository.save(cidade);
    }

    @Override
    public Cidade buscarPorId(Long id) {
        return cidadeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cidade nao encontrada com ID: " + id));
    }

    @Override
    public List<Cidade> buscarTodos(Long estadoId) {
        if (estadoId == null) {
            return cidadeRepository.findAllByOrderByNomeCidadeAsc();
        }
        if (!estadoRepository.existsById(estadoId)) {
            throw new NotFoundException("Estado nao encontrado com ID: " + estadoId);
        }
        return cidadeRepository.findByEstado_IdOrderByNomeCidadeAsc(estadoId);
    }

    @Override
    @Transactional
    public Cidade atualizarCidade(Long id, CidadeRequestDTO cidadeDTO) {
        Cidade cidadeExistente = buscarPorId(id);
        Estado estado = buscarEstadoPorId(cidadeDTO.getEstadoId());
        validarDuplicidade(cidadeDTO.getNomeCidade(), estado.getId(), cidadeExistente);
        cidadeMapper.updateEntityFromDto(cidadeExistente, cidadeDTO, estado);
        return cidadeRepository.save(cidadeExistente);
    }

    @Override
    @Transactional
    public void deletarCidade(Long id) {
        if (!cidadeRepository.existsById(id)) {
            throw new NotFoundException("Cidade nao encontrada com ID: " + id);
        }

        try {
            cidadeRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Nao foi possivel excluir a cidade pois existem usuarios vinculados.");
        }
    }

    private Estado buscarEstadoPorId(Long estadoId) {
        return estadoRepository.findById(estadoId)
                .orElseThrow(() -> new NotFoundException("Estado nao encontrado com ID: " + estadoId));
    }

    private void validarDuplicidade(String nomeCidade, Long estadoId, Cidade cidadeExistente) {
        if (nomeCidade == null || estadoId == null) {
            return;
        }

        boolean mesmaCidadeNoMesmoEstado = cidadeExistente != null
                && cidadeExistente.getNomeCidade() != null
                && cidadeExistente.getNomeCidade().equalsIgnoreCase(nomeCidade.trim())
                && cidadeExistente.getEstado() != null
                && cidadeExistente.getEstado().getId().equals(estadoId);

        if (!mesmaCidadeNoMesmoEstado
                && cidadeRepository.existsByNomeCidadeIgnoreCaseAndEstado_Id(nomeCidade.trim(), estadoId)) {
            throw new ConflictException("Ja existe cidade cadastrada com este nome no estado informado.");
        }
    }
}
