package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.configs.OpenApiExamples;
import br.com.fiap.aiury.dto.ApiErrorResponse;
import br.com.fiap.aiury.dto.UsuarioRequestDTO;
import br.com.fiap.aiury.dto.UsuarioResponseDTO;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.representation.UsuarioRepresentationBuilder;
import br.com.fiap.aiury.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
@Tag(name = "Usuarios", description = "Operações de cadastro e manutenção de usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioRepresentationBuilder usuarioRepresentationBuilder;

    public UsuarioController(UsuarioService usuarioService, UsuarioRepresentationBuilder usuarioRepresentationBuilder) {
        this.usuarioService = usuarioService;
        this.usuarioRepresentationBuilder = usuarioRepresentationBuilder;
    }

    @PostMapping
    @Operation(
            summary = "Criar usuario",
            description = "Cadastra um novo usuario. Pre-requisito: cidadeId informado deve existir previamente."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Payload de criação de usuario. Campo dataNascimento deve seguir o formato dd/MM/yyyy.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioRequestDTO.class),
                    examples = @ExampleObject(name = "UsuarioValido", value = OpenApiExamples.USUARIO_REQUEST)
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario criado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload inválido",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "ErroValidacao", value = OpenApiExamples.ERROR_VALIDATION),
                                    @ExampleObject(name = "FormatoDataInvalido", value = OpenApiExamples.ERROR_INVALID_DATE)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cidade não encontrada",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "RecursoNaoEncontrado", value = OpenApiExamples.ERROR_NOT_FOUND)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Celular já cadastrado",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Conflito", value = OpenApiExamples.ERROR_CONFLICT)
                    )
            )
    })
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> cadastrarUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioDTO) {
        Usuario novoUsuario = usuarioService.criarUsuario(usuarioDTO);
        EntityModel<UsuarioResponseDTO> resource = usuarioRepresentationBuilder.toModel(novoUsuario);
        URI location = linkTo(methodOn(UsuarioController.class).buscarUsuarioPorId(novoUsuario.getId())).toUri();
        return ResponseEntity.created(location).body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Retorna um usuario pelo identificador")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> buscarUsuarioPorId(
            @Parameter(description = "ID do usuario. Deve existir previamente.", example = "1")
            @PathVariable Long id
    ) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuarioRepresentationBuilder.toModel(usuario));
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Lista usuarios com filtro opcional por cidade")
    @ApiResponse(
            responseCode = "200",
            description = "Lista retornada com sucesso"
    )
    public ResponseEntity<CollectionModel<EntityModel<UsuarioResponseDTO>>> listarTodos(
            @Parameter(description = "Filtro opcional por cidade")
            @RequestParam(required = false) Long cidadeId
    ) {
        List<Usuario> usuarios = usuarioService.buscarTodos(cidadeId);
        return ResponseEntity.ok(usuarioRepresentationBuilder.toCollection(usuarios, cidadeId));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar usuario",
            description = "Atualiza um usuario existente. Pre-requisitos: id do usuario e cidadeId (quando informado) devem existir."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Payload de atualização. Campo dataNascimento deve seguir o formato dd/MM/yyyy.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioRequestDTO.class),
                    examples = @ExampleObject(name = "UsuarioAtualizacao", value = OpenApiExamples.USUARIO_REQUEST)
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario atualizado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload inválido",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "ErroValidacao", value = OpenApiExamples.ERROR_VALIDATION),
                                    @ExampleObject(name = "FormatoDataInvalido", value = OpenApiExamples.ERROR_INVALID_DATE)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario ou cidade não encontrados",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "RecursoNaoEncontrado", value = OpenApiExamples.ERROR_NOT_FOUND)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Celular já cadastrado",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Conflito", value = OpenApiExamples.ERROR_CONFLICT)
                    )
            )
    })
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> atualizarUsuario(
            @Parameter(description = "ID do usuario a ser atualizado.", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UsuarioRequestDTO usuarioDTO
    ) {
        Usuario usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDTO);
        return ResponseEntity.ok(usuarioRepresentationBuilder.toModel(usuarioAtualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir usuario", description = "Remove um usuario pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario removido"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Usuario vinculado a outros recursos",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<Void> deletarUsuario(
            @Parameter(description = "ID do usuario a ser removido.", example = "1")
            @PathVariable Long id
    ) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}

