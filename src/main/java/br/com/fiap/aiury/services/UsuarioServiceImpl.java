package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.UsuarioRequestDTO;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.exceptions.ConflictException;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.UsuarioMapper;
import br.com.fiap.aiury.repositories.ChatRepository;
import br.com.fiap.aiury.repositories.CidadeRepository;
import br.com.fiap.aiury.repositories.MensagemRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Implementacao de {@link UsuarioService}.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CidadeRepository cidadeRepository;
    private final ChatRepository chatRepository;
    private final MensagemRepository mensagemRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                              CidadeRepository cidadeRepository,
                              ChatRepository chatRepository,
                              MensagemRepository mensagemRepository,
                              PasswordEncoder passwordEncoder,
                              UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.cidadeRepository = cidadeRepository;
        this.chatRepository = chatRepository;
        this.mensagemRepository = mensagemRepository;
        this.passwordEncoder = passwordEncoder;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    @Transactional
    public Usuario criarUsuario(UsuarioRequestDTO usuarioDTO) {
        if (!StringUtils.hasText(usuarioDTO.getSenha())) {
            throw new IllegalArgumentException("A senha do usuario e obrigatoria no cadastro.");
        }

        Cidade cidade = cidadeRepository.findById(usuarioDTO.getCidadeId())
                .orElseThrow(() -> new NotFoundException("Cidade nao encontrada com ID: " + usuarioDTO.getCidadeId()));

        validarCelularUnico(usuarioDTO.getCelular(), null);

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO, cidade);
        usuario.setSenha(codificarSenha(usuarioDTO.getSenha()));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado com ID: " + id));
    }

    @Override
    public List<Usuario> buscarTodos(Long cidadeId) {
        if (cidadeId == null) {
            return usuarioRepository.findAllByOrderByNomeRealAsc();
        }
        return usuarioRepository.findByCidade_Id(cidadeId);
    }

    @Override
    @Transactional
    public Usuario atualizarUsuario(Long id, UsuarioRequestDTO detalhesUsuarioDTO) {
        Usuario usuarioExistente = buscarPorId(id);

        Cidade novaCidade = null;
        if (detalhesUsuarioDTO.getCidadeId() != null) {
            novaCidade = cidadeRepository.findById(detalhesUsuarioDTO.getCidadeId())
                    .orElseThrow(() -> new NotFoundException(
                            "Cidade nao encontrada com ID: " + detalhesUsuarioDTO.getCidadeId()
                    ));
        }

        validarCelularUnico(detalhesUsuarioDTO.getCelular(), id);
        usuarioMapper.updateEntityFromDto(usuarioExistente, detalhesUsuarioDTO, novaCidade);

        if (StringUtils.hasText(detalhesUsuarioDTO.getSenha())) {
            usuarioExistente.setSenha(codificarSenha(detalhesUsuarioDTO.getSenha()));
        }

        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    @Transactional
    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NotFoundException("Usuario nao encontrado com ID: " + id);
        }

        try {
            mensagemRepository.deleteByChat_Usuario_Id(id);
            mensagemRepository.deleteByRemetente_Id(id);
            chatRepository.deleteByUsuario_Id(id);
            usuarioRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Nao foi possivel excluir o usuario pois existem registros vinculados.");
        }
    }

    @Override
    @Transactional
    public void alterarSenha(Long id, String senhaAtual, String novaSenha) {
        Usuario usuario = buscarPorId(id);

        if (!StringUtils.hasText(senhaAtual)) {
            throw new IllegalArgumentException("Informe a senha atual.");
        }
        if (!StringUtils.hasText(novaSenha)) {
            throw new IllegalArgumentException("Informe a nova senha.");
        }

        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new IllegalArgumentException("A senha atual informada nao confere.");
        }
        if (passwordEncoder.matches(novaSenha, usuario.getSenha())) {
            throw new IllegalArgumentException("A nova senha deve ser diferente da senha atual.");
        }

        usuario.setSenha(codificarSenha(novaSenha));
        usuarioRepository.save(usuario);
    }

    private void validarCelularUnico(String celular, Long usuarioIdAtual) {
        if (celular == null || celular.isBlank()) {
            return;
        }

        boolean celularEmUso = usuarioIdAtual == null
                ? usuarioRepository.existsByCelular(celular)
                : usuarioRepository.existsByCelularAndIdNot(celular, usuarioIdAtual);

        if (celularEmUso) {
            throw new ConflictException("Ja existe usuario cadastrado com o celular informado.");
        }
    }

    private String codificarSenha(String senha) {
        if (!StringUtils.hasText(senha)) {
            return senha;
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
