package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.AjudanteRequestDTO;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.exceptions.ConflictException;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.AjudanteMapper;
import br.com.fiap.aiury.repositories.AjudanteRepository;
import br.com.fiap.aiury.repositories.ChatRepository;
import br.com.fiap.aiury.repositories.MensagemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Implementacao da camada de servico para o recurso de ajudante.
 */
@Service
public class AjudanteServiceImpl implements AjudanteService {

    private final AjudanteRepository ajudanteRepository;
    private final ChatRepository chatRepository;
    private final MensagemRepository mensagemRepository;
    private final PasswordEncoder passwordEncoder;
    private final AjudanteMapper ajudanteMapper;

    @Autowired
    public AjudanteServiceImpl(AjudanteRepository ajudanteRepository,
                               ChatRepository chatRepository,
                               MensagemRepository mensagemRepository,
                               PasswordEncoder passwordEncoder,
                               AjudanteMapper ajudanteMapper) {
        this.ajudanteRepository = ajudanteRepository;
        this.chatRepository = chatRepository;
        this.mensagemRepository = mensagemRepository;
        this.passwordEncoder = passwordEncoder;
        this.ajudanteMapper = ajudanteMapper;
    }

    @Override
    @Transactional
    public Ajudante criarAjudante(AjudanteRequestDTO ajudanteDTO) {
        if (!StringUtils.hasText(ajudanteDTO.getSenha())) {
            throw new IllegalArgumentException("A senha do ajudante é obrigatória no cadastro.");
        }

        validarLoginUnico(ajudanteDTO.getLogin(), null);
        Ajudante ajudante = ajudanteMapper.toEntity(ajudanteDTO);
        ajudante.setLogin(normalizarLogin(ajudanteDTO.getLogin()));
        ajudante.setSenha(codificarSenha(ajudanteDTO.getSenha()));
        return ajudanteRepository.save(ajudante);
    }

    @Override
    public Ajudante buscarPorId(Long id) {
        return ajudanteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ajudante não encontrado com ID: " + id));
    }

    @Override
    public List<Ajudante> buscarTodos(Boolean disponivel) {
        if (disponivel == null) {
            return ajudanteRepository.findAllByOrderByAreaAtuacaoAsc();
        }
        return ajudanteRepository.findByDisponivel(disponivel);
    }

    @Override
    @Transactional
    public Ajudante atualizarAjudante(Long id, AjudanteRequestDTO ajudanteDTO) {
        Ajudante ajudanteExistente = buscarPorId(id);
        validarLoginUnico(ajudanteDTO.getLogin(), id);

        String senhaAnterior = ajudanteExistente.getSenha();
        ajudanteMapper.updateEntityFromDto(ajudanteExistente, ajudanteDTO);
        ajudanteExistente.setLogin(normalizarLogin(ajudanteExistente.getLogin()));

        if (StringUtils.hasText(ajudanteDTO.getSenha())) {
            ajudanteExistente.setSenha(codificarSenha(ajudanteDTO.getSenha()));
        } else {
            ajudanteExistente.setSenha(senhaAnterior);
        }

        return ajudanteRepository.save(ajudanteExistente);
    }

    @Override
    @Transactional
    public void deletarAjudante(Long id) {
        if (!ajudanteRepository.existsById(id)) {
            throw new NotFoundException("Ajudante não encontrado com ID: " + id);
        }

        try {
            mensagemRepository.deleteByChat_Ajudante_Id(id);
            mensagemRepository.deleteByRemetenteAjudante_Id(id);
            chatRepository.deleteByAjudante_Id(id);
            ajudanteRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Não foi possível excluir o ajudante pois existem chats vinculados.");
        }
    }

    private void validarLoginUnico(String login, Long ajudanteIdAtual) {
        if (!StringUtils.hasText(login)) {
            return;
        }

        String loginNormalizado = normalizarLogin(login);
        boolean loginEmUso = ajudanteIdAtual == null
                ? ajudanteRepository.existsByLoginIgnoreCase(loginNormalizado)
                : ajudanteRepository.existsByLoginIgnoreCaseAndIdNot(loginNormalizado, ajudanteIdAtual);

        if (loginEmUso) {
            throw new ConflictException("Já existe ajudante cadastrado com o login informado.");
        }
    }

    private String normalizarLogin(String login) {
        return login == null ? null : login.trim().toLowerCase();
    }

    private String codificarSenha(String senha) {
        if (senha == null) {
            return null;
        }

        String valorNormalizado = senha.trim();
        if (valorNormalizado.startsWith("$2a$")
                || valorNormalizado.startsWith("$2b$")
                || valorNormalizado.startsWith("$2y$")) {
            return valorNormalizado;
        }
        return passwordEncoder.encode(valorNormalizado);
    }
}
