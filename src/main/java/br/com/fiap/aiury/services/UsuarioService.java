package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.UsuarioDTO;
import br.com.fiap.aiury.entities.Usuario;
import jakarta.validation.Valid;

import java.util.List;

public interface UsuarioService {

    Usuario criarUsuario(@Valid UsuarioDTO usuarioDTO);

    Usuario buscarPorId(Long id);

    List<Usuario> buscarTodos();

    Usuario atualizarUsuario(Long id, @Valid UsuarioDTO detalhesUsuarioDTO);

    void deletarUsuario(Long id);

}
