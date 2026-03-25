package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.UsuarioRequestDTO;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.exceptions.ConflictException;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.UsuarioMapper;
import br.com.fiap.aiury.repositories.CidadeRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementacao de {@link UsuarioService}.
 *
 * Responsabilidades:
 * - aplicar validacoes de existencia (usuario e cidade);
 * - coordenar mapeamento DTO <-> entidade;
 * - orquestrar persistencia transacional no repositorio.
 */
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CidadeRepository cidadeRepository;
    private final UsuarioMapper usuarioMapper;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, CidadeRepository cidadeRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.cidadeRepository = cidadeRepository;
        this.usuarioMapper = usuarioMapper;
    }

    /**
     * Cria usuario novo validando previamente a cidade informada.
     */
    @Override
    @Transactional
    public Usuario criarUsuario(UsuarioRequestDTO usuarioDTO) {
        Cidade cidade = cidadeRepository.findById(usuarioDTO.getCidadeId())
                .orElseThrow(() -> new NotFoundException("Cidade nao encontrada com ID: " + usuarioDTO.getCidadeId()));

        validarCelularUnico(usuarioDTO.getCelular(), null);

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO, cidade);
        return usuarioRepository.save(usuario);
    }

    /**
     * Busca usuario por ID e dispara erro de negocio caso nao exista.
     */
    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario nao encontrado com ID: " + id));
    }

    /**
     * Retorna usuarios com filtro opcional por cidade.
     */
    @Override
    public List<Usuario> buscarTodos(Long cidadeId) {
        if (cidadeId == null) {
            return usuarioRepository.findAllByOrderByNomeRealAsc();
        }
        return usuarioRepository.findByCidade_Id(cidadeId);
    }

    /**
     * Atualiza dados do usuario e opcionalmente troca cidade.
     *
     * Mantem a regra existente de atualizar senha apenas quando o valor vier preenchido.
     */
    @Override
    @Transactional
    public Usuario atualizarUsuario(Long id, UsuarioRequestDTO detalhesUsuarioDTO) {
        Usuario usuarioExistente = buscarPorId(id);

        Cidade novaCidade = null;
        if (detalhesUsuarioDTO.getCidadeId() != null) {
            novaCidade = cidadeRepository.findById(detalhesUsuarioDTO.getCidadeId())
                    .orElseThrow(() -> new NotFoundException("Cidade nao encontrada com ID: " + detalhesUsuarioDTO.getCidadeId()));
        }

        validarCelularUnico(detalhesUsuarioDTO.getCelular(), id);
        usuarioMapper.updateEntityFromDto(usuarioExistente, detalhesUsuarioDTO, novaCidade);

        if (detalhesUsuarioDTO.getSenha() != null) {
            usuarioExistente.setSenha(detalhesUsuarioDTO.getSenha());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    /**
     * Remove usuario por ID com validacao de existencia.
     */
    @Override
    @Transactional
    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NotFoundException("Usuario nao encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
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
}
