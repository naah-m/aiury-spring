package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.dto.UsuarioDTO;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador responsável por expor os endpoints de Usuário.
 * Aqui estão as operações básicas de CRUD com suporte a HATEOAS.
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

    // ➕ Criar novo usuário
    @PostMapping
    @Operation(summary = "Criar usuario", description = "Cadastra um novo usuario")
    public ResponseEntity<EntityModel<Usuario>> cadastrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario novoUsuario = usuarioService.criarUsuario(usuarioDTO);
        EntityModel<Usuario> resource = usuarioModelAssembler.toModel(novoUsuario);

        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    // 🔍 Buscar usuário por ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Busca um usuario pelo identificador")
    public ResponseEntity<EntityModel<Usuario>> buscarUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        EntityModel<Usuario> resource = usuarioModelAssembler.toModel(usuario);

        return ResponseEntity.ok(resource);
    }

    // 📋 Listar todos os usuários
    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Lista todos os usuarios cadastrados")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> listarTodos() {
        List<Usuario> usuarios = usuarioService.buscarTodos();
        return ResponseEntity.ok(usuarioModelAssembler.toCollection(usuarios));
    }

    // ✏️ Atualizar usuário existente
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuario", description = "Atualiza um usuario existente pelo ID")
    public ResponseEntity<EntityModel<Usuario>> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDTO);
        EntityModel<Usuario> resource = usuarioModelAssembler.toModel(usuarioAtualizado);

        return ResponseEntity.ok(resource);
    }

    // 🗑️ Deletar usuário
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir usuario", description = "Remove um usuario pelo ID")
    public void deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
    }
}
