package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.UsuarioDTO;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.exceptions.NotFoundException;
import br.com.fiap.aiury.mappers.UsuarioMapper;
import br.com.fiap.aiury.repositories.CidadeRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository usuarioRepository;
    private final CidadeRepository cidadeRepository;
    private final UsuarioMapper usuarioMapper;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, CidadeRepository cidadeRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.cidadeRepository = cidadeRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    @Transactional
    public Usuario criarUsuario(UsuarioDTO usuarioDTO) {

        Cidade cidade = cidadeRepository.findById(usuarioDTO.getCidadeId())
                .orElseThrow(() -> new NotFoundException("Cidade não encontrada com ID: " + usuarioDTO.getCidadeId()));

        Usuario usuario = usuarioMapper.toEntity(usuarioDTO, cidade);

        // TODO: Implementar a criptografia
        System.out.println("LOG: Implementar criptografia de senha aqui...");

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com ID: " + id));
    }

    @Override
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional
    public Usuario atualizarUsuario(Long id, UsuarioDTO detalhesUsuarioDTO) {
        Usuario usuarioExistente = buscarPorId(id);

        Cidade novaCidade = null;
        if (detalhesUsuarioDTO.getCidadeId() != null) {
            novaCidade = cidadeRepository.findById(detalhesUsuarioDTO.getCidadeId())
                    .orElseThrow(() -> new NotFoundException("Cidade não encontrada com ID: " + detalhesUsuarioDTO.getCidadeId()));
        }

        usuarioMapper.updateEntityFromDto(usuarioExistente, detalhesUsuarioDTO, novaCidade);

        if (detalhesUsuarioDTO.getSenha() != null && detalhesUsuarioDTO.getSenha().length() >= 8) {
            System.out.println("LOG: Aplicar BCryptPasswordEncoder na nova senha...");
            // TODO: Aplicar BCryptPasswordEncoder aqui
            usuarioExistente.setSenha(detalhesUsuarioDTO.getSenha());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    @Transactional
    public void deletarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

}
