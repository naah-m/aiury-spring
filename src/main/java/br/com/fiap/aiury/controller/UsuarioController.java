package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.dto.UsuarioDTO;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST responsavel pelo recurso de usuario.
 *
 * Papel na arquitetura:
 * - recebe requisicoes HTTP relacionadas ao cadastro de usuarios;
 * - delega regras para a camada de servico;
 * - monta representacoes HATEOAS via {@link UsuarioModelAssembler}.
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operacoes de cadastro e manutencao de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioModelAssembler usuarioModelAssembler;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, UsuarioModelAssembler usuarioModelAssembler) {
        this.usuarioService = usuarioService;
        this.usuarioModelAssembler = usuarioModelAssembler;
    }

    /**
     * Cria um novo usuario.
     *
     * @param usuarioDTO dados de entrada validados
     * @return usuario criado com links HATEOAS
     */
    @PostMapping
    @Operation(summary = "Criar usuario", description = "Cadastra um novo usuario")
    public ResponseEntity<EntityModel<Usuario>> cadastrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario novoUsuario = usuarioService.criarUsuario(usuarioDTO);
        EntityModel<Usuario> resource = usuarioModelAssembler.toModel(novoUsuario);
        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    /**
     * Recupera um usuario pelo identificador.
     *
     * @param id identificador do usuario
     * @return usuario encontrado com links HATEOAS
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Busca um usuario pelo identificador")
    public ResponseEntity<EntityModel<Usuario>> buscarUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        EntityModel<Usuario> resource = usuarioModelAssembler.toModel(usuario);
        return ResponseEntity.ok(resource);
    }

    /**
     * Lista todos os usuarios cadastrados.
     *
     * @return colecao HATEOAS de usuarios
     */
    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Lista todos os usuarios cadastrados")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> listarTodos() {
        List<Usuario> usuarios = usuarioService.buscarTodos();
        return ResponseEntity.ok(usuarioModelAssembler.toCollection(usuarios));
    }

    /**
     * Atualiza um usuario existente.
     *
     * @param id identificador do usuario alvo
     * @param usuarioDTO dados atualizados recebidos no corpo da requisicao
     * @return representacao atualizada com links HATEOAS
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuario", description = "Atualiza um usuario existente pelo ID")
    public ResponseEntity<EntityModel<Usuario>> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDTO);
        EntityModel<Usuario> resource = usuarioModelAssembler.toModel(usuarioAtualizado);
        return ResponseEntity.ok(resource);
    }

    /**
     * Remove usuario por identificador.
     *
     * @param id identificador do usuario
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir usuario", description = "Remove um usuario pelo ID")
    public void deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
    }
}
