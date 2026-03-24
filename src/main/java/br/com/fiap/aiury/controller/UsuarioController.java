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
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controlador responsável por expor os endpoints de Usuário.
 * Aqui estão as operações básicas de CRUD com suporte a HATEOAS.
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operacoes de cadastro e manutencao de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ➕ Criar novo usuário
    @PostMapping
    @Operation(summary = "Criar usuario", description = "Cadastra um novo usuario")
    public ResponseEntity<EntityModel<Usuario>> cadastrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario novoUsuario = usuarioService.criarUsuario(usuarioDTO);

        EntityModel<Usuario> resource = EntityModel.of(novoUsuario,
                linkTo(methodOn(UsuarioController.class).buscarUsuarioPorId(novoUsuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarTodos()).withRel("todos-usuarios"));

        return new ResponseEntity<>(resource, HttpStatus.CREATED);
    }

    // 🔍 Buscar usuário por ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Busca um usuario pelo identificador")
    public ResponseEntity<EntityModel<Usuario>> buscarUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);

        EntityModel<Usuario> resource = EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).buscarUsuarioPorId(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarTodos()).withRel("todos-usuarios"));

        return ResponseEntity.ok(resource);
    }

    // 📋 Listar todos os usuários
    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Lista todos os usuarios cadastrados")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> listarTodos() {
        List<EntityModel<Usuario>> usuarios = usuarioService.buscarTodos().stream()
                .map(usuario -> EntityModel.of(usuario,
                        linkTo(methodOn(UsuarioController.class).buscarUsuarioPorId(usuario.getId())).withSelfRel()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(usuarios,
                        linkTo(methodOn(UsuarioController.class).listarTodos()).withSelfRel())
        );
    }

    // ✏️ Atualizar usuário existente
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuario", description = "Atualiza um usuario existente pelo ID")
    public ResponseEntity<EntityModel<Usuario>> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDTO);

        EntityModel<Usuario> resource = EntityModel.of(usuarioAtualizado,
                linkTo(methodOn(UsuarioController.class).buscarUsuarioPorId(id)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarTodos()).withRel("todos-usuarios"));

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
