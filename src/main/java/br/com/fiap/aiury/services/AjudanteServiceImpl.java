package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.AjudanteRequestDTO;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.exceptions.ConflictException;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.AjudanteMapper;
import br.com.fiap.aiury.repositories.AjudanteRepository;
import br.com.fiap.aiury.repositories.ChatRepository;
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
    private final PasswordEncoder passwordEncoder;
    private final AjudanteMapper ajudanteMapper;

    @Autowired
    public AjudanteServiceImpl(AjudanteRepository ajudanteRepository,
                               ChatRepository chatRepository,
                               PasswordEncoder passwordEncoder,
                               AjudanteMapper ajudanteMapper) {
        this.ajudanteRepository = ajudanteRepository;
        this.chatRepository = chatRepository;
        this.passwordEncoder = passwordEncoder;
        this.ajudanteMapper = ajudanteMapper;
    }

    @Override
    @Transactional
    public Ajudante criarAjudante(AjudanteRequestDTO ajudanteDTO) {
        if (!StringUtils.hasText(ajudanteDTO.getSenha())) {
            throw new IllegalArgumentException("A senha do ajudante e obrigatoria no cadastro.");
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
                .orElseThrow(() -> new NotFoundException("Ajudante nao encontrado com ID: " + id));
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
        Ajudante ajudante = ajudanteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ajudante nao encontrado com ID: " + id));

        try {
            List<Chat> chats = chatRepository.findAll(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("ajudante").get("id"), id),
                    org.springframework.data.domain.Sort.by("id")
            );
            chats.forEach(chatRepository::delete);
            ajudanteRepository.delete(ajudante);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Nao foi possivel excluir o ajudante pois existem chats vinculados.");
        }
    }

    @Override
    @Transactional
    public void alterarSenha(Long id, String senhaAtual, String novaSenha) {
        Ajudante ajudante = buscarPorId(id);

        if (!StringUtils.hasText(senhaAtual)) {
            throw new IllegalArgumentException("Informe a senha atual.");
        }
        if (!StringUtils.hasText(novaSenha)) {
            throw new IllegalArgumentException("Informe a nova senha.");
        }

        if (!passwordEncoder.matches(senhaAtual, ajudante.getSenha())) {
            throw new IllegalArgumentException("A senha atual informada nao confere.");
        }
        if (passwordEncoder.matches(novaSenha, ajudante.getSenha())) {
            throw new IllegalArgumentException("A nova senha deve ser diferente da senha atual.");
        }

        ajudante.setSenha(codificarSenha(novaSenha));
        ajudanteRepository.save(ajudante);
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
            throw new ConflictException("Ja existe ajudante cadastrado com o login informado.");
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
