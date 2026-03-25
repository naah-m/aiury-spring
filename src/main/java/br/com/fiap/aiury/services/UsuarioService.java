package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.UsuarioRequestDTO;
import br.com.fiap.aiury.entities.Usuario;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Contrato de servicos para operacoes de usuario.
 *
 * Papel na arquitetura:
 * - define casos de uso expostos para o controller;
 * - isola regras de negocio e validacoes de existencia.
 */
public interface UsuarioService {

    /**
     * Cria novo usuario a partir do DTO de entrada.
     *
     * @param usuarioDTO dados validados de cadastro
     * @return usuario persistido
     */
    Usuario criarUsuario(@Valid UsuarioRequestDTO usuarioDTO);

    /**
     * Recupera usuario por identificador.
     *
     * @param id identificador do usuario
     * @return usuario localizado
     */
    Usuario buscarPorId(Long id);

    /**
     * Lista usuarios com filtro opcional por cidade.
     *
     * @param cidadeId filtro opcional por cidade
     * @return colecao de usuarios
     */
    List<Usuario> buscarTodos(Long cidadeId);

    /**
     * Atualiza dados do usuario existente.
     *
     * @param id identificador do registro alvo
     * @param detalhesUsuarioDTO novos dados de atualizacao
     * @return usuario atualizado
     */
    Usuario atualizarUsuario(Long id, @Valid UsuarioRequestDTO detalhesUsuarioDTO);

    /**
     * Remove usuario por ID.
     *
     * @param id identificador do usuario
     */
    void deletarUsuario(Long id);
}
