package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.configs.OpenApiExamples;
import br.com.fiap.aiury.dto.ApiErrorResponse;
import br.com.fiap.aiury.dto.EstadoRequestDTO;
import br.com.fiap.aiury.dto.EstadoResponseDTO;
import br.com.fiap.aiury.entities.Estado;
import br.com.fiap.aiury.representation.EstadoRepresentationBuilder;
import br.com.fiap.aiury.services.EstadoService;
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
 * Controller REST para catalogo de estados.
 */
@RestController
@RequestMapping("/api/estados")
@Tag(name = "Estados", description = "Operações de cadastro e consulta de estados")
public class EstadoController {

    private final EstadoService estadoService;
    private final EstadoRepresentationBuilder estadoRepresentationBuilder;

    public EstadoController(EstadoService estadoService, EstadoRepresentationBuilder estadoRepresentationBuilder) {
        this.estadoService = estadoService;
        this.estadoRepresentationBuilder = estadoRepresentationBuilder;
    }

    @PostMapping
    @Operation(summary = "Criar estado", description = "Cadastra um novo estado no catalogo")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Payload de criação de estado.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = EstadoRequestDTO.class),
                    examples = @ExampleObject(name = "EstadoValido", value = OpenApiExamples.ESTADO_REQUEST)
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Estado criado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload inválido",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito de unicidade para nome ou UF",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Conflito", value = OpenApiExamples.ERROR_CONFLICT_GENERIC)
                    )
            )
    })
    public ResponseEntity<EntityModel<EstadoResponseDTO>> cadastrarEstado(@Valid @RequestBody EstadoRequestDTO estadoDTO) {
        Estado novoEstado = estadoService.criarEstado(estadoDTO);
        EntityModel<EstadoResponseDTO> resource = estadoRepresentationBuilder.toModel(novoEstado);
        URI location = linkTo(methodOn(EstadoController.class).buscarEstadoPorId(novoEstado.getId())).toUri();
        return ResponseEntity.created(location).body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar estado por ID", description = "Retorna um estado pelo identificador")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado encontrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estado não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<EstadoResponseDTO>> buscarEstadoPorId(
            @Parameter(description = "ID do estado. Deve existir previamente.", example = "1")
            @PathVariable Long id
    ) {
        Estado estado = estadoService.buscarPorId(id);
        return ResponseEntity.ok(estadoRepresentationBuilder.toModel(estado));
    }

    @GetMapping
    @Operation(summary = "Listar estados", description = "Lista estados com filtro opcional por UF")
    @ApiResponse(
            responseCode = "200",
            description = "Lista retornada com sucesso"
    )
    public ResponseEntity<CollectionModel<EntityModel<EstadoResponseDTO>>> listarEstados(
            @Parameter(description = "Filtro opcional por UF, ex.: SP")
            @RequestParam(required = false) String uf
    ) {
        List<Estado> estados = estadoService.buscarTodos(uf);
        return ResponseEntity.ok(estadoRepresentationBuilder.toCollection(estados, uf));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar estado", description = "Atualiza um estado existente")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Payload de atualização de estado.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = EstadoRequestDTO.class),
                    examples = @ExampleObject(name = "EstadoAtualizacao", value = OpenApiExamples.ESTADO_REQUEST)
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estado atualizado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload inválido",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estado não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflito de unicidade para nome ou UF",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Conflito", value = OpenApiExamples.ERROR_CONFLICT_GENERIC)
                    )
            )
    })
    public ResponseEntity<EntityModel<EstadoResponseDTO>> atualizarEstado(
            @Parameter(description = "ID do estado a ser atualizado.", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody EstadoRequestDTO estadoDTO
    ) {
        Estado estadoAtualizado = estadoService.atualizarEstado(id, estadoDTO);
        return ResponseEntity.ok(estadoRepresentationBuilder.toModel(estadoAtualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir estado", description = "Remove um estado pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estado removido"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estado não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Estado vinculado a cidades",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<Void> deletarEstado(
            @Parameter(description = "ID do estado a ser removido.", example = "1")
            @PathVariable Long id
    ) {
        estadoService.deletarEstado(id);
        return ResponseEntity.noContent().build();
    }
}

