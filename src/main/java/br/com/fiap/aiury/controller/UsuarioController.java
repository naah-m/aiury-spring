package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.dto.ApiErrorResponse;
import br.com.fiap.aiury.dto.UsuarioDTO;
import br.com.fiap.aiury.dto.UsuarioResponseDTO;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller REST do recurso de usuario.
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operacoes de cadastro e manutencao de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioModelAssembler usuarioModelAssembler;

    public UsuarioController(UsuarioService usuarioService, UsuarioModelAssembler usuarioModelAssembler) {
        this.usuarioService = usuarioService;
        this.usuarioModelAssembler = usuarioModelAssembler;
    }

    @PostMapping
    @Operation(summary = "Criar usuario", description = "Cadastra um novo usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario criado"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload invalido",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cidade nao encontrada",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> cadastrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario novoUsuario = usuarioService.criarUsuario(usuarioDTO);
        EntityModel<UsuarioResponseDTO> resource = usuarioModelAssembler.toModel(novoUsuario);
        URI location = linkTo(methodOn(UsuarioController.class).buscarUsuarioPorId(novoUsuario.getId())).toUri();
        return ResponseEntity.created(location).body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Retorna um usuario pelo identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> buscarUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuarioModelAssembler.toModel(usuario));
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Lista usuarios com filtro opcional por cidade")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<CollectionModel<EntityModel<UsuarioResponseDTO>>> listarTodos(
            @Parameter(description = "Filtro opcional por cidade")
            @RequestParam(required = false) Long cidadeId
    ) {
        List<Usuario> usuarios = usuarioService.buscarTodos(cidadeId);
        return ResponseEntity.ok(usuarioModelAssembler.toCollection(usuarios, cidadeId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuario", description = "Atualiza um usuario existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario atualizado"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload invalido",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario ou cidade nao encontrados",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDTO);
        return ResponseEntity.ok(usuarioModelAssembler.toModel(usuarioAtualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir usuario", description = "Remove um usuario pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario removido"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
