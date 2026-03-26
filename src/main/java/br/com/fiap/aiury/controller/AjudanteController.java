package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.configs.OpenApiExamples;
import br.com.fiap.aiury.dto.AjudanteRequestDTO;
import br.com.fiap.aiury.dto.AjudanteResponseDTO;
import br.com.fiap.aiury.dto.ApiErrorResponse;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.representation.AjudanteRepresentationBuilder;
import br.com.fiap.aiury.services.AjudanteService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Controller REST do recurso de ajudantes.
 */
@RestController
@RequestMapping("/api/ajudantes")
@Tag(name = "Ajudantes", description = "Operações de cadastro e manutenção de ajudantes")
public class AjudanteController {

    private final AjudanteService ajudanteService;
    private final AjudanteRepresentationBuilder ajudanteRepresentationBuilder;

    public AjudanteController(AjudanteService ajudanteService, AjudanteRepresentationBuilder ajudanteRepresentationBuilder) {
        this.ajudanteService = ajudanteService;
        this.ajudanteRepresentationBuilder = ajudanteRepresentationBuilder;
    }

    @PostMapping
    @Operation(summary = "Criar ajudante", description = "Cadastra um novo ajudante")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Payload de criação de ajudante.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AjudanteRequestDTO.class),
                    examples = @ExampleObject(name = "AjudanteValido", value = OpenApiExamples.AJUDANTE_REQUEST)
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ajudante criado"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload inválido",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<AjudanteResponseDTO>> cadastrarAjudante(@Valid @RequestBody AjudanteRequestDTO ajudanteDTO) {
        Ajudante novoAjudante = ajudanteService.criarAjudante(ajudanteDTO);
        EntityModel<AjudanteResponseDTO> resource = ajudanteRepresentationBuilder.toModel(novoAjudante);
        URI location = linkTo(methodOn(AjudanteController.class).buscarAjudantePorId(novoAjudante.getId())).toUri();
        return ResponseEntity.created(location).body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ajudante por ID", description = "Retorna um ajudante pelo identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ajudante encontrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ajudante não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<AjudanteResponseDTO>> buscarAjudantePorId(
            @Parameter(description = "ID do ajudante. Deve existir previamente.", example = "1")
            @PathVariable Long id
    ) {
        Ajudante ajudante = ajudanteService.buscarPorId(id);
        return ResponseEntity.ok(ajudanteRepresentationBuilder.toModel(ajudante));
    }

    @GetMapping
    @Operation(summary = "Listar ajudantes", description = "Lista ajudantes com filtro opcional por disponibilidade")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public ResponseEntity<CollectionModel<EntityModel<AjudanteResponseDTO>>> listarTodos(
            @Parameter(description = "Filtro opcional por disponibilidade")
            @RequestParam(required = false) Boolean disponivel
    ) {
        List<Ajudante> ajudantes = ajudanteService.buscarTodos(disponivel);
        return ResponseEntity.ok(ajudanteRepresentationBuilder.toCollection(ajudantes, disponivel));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar ajudante", description = "Atualiza um ajudante existente")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Payload de atualização de ajudante.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AjudanteRequestDTO.class),
                    examples = @ExampleObject(name = "AjudanteAtualizacao", value = OpenApiExamples.AJUDANTE_REQUEST)
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ajudante atualizado"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload inválido",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ajudante não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<AjudanteResponseDTO>> atualizarAjudante(
            @Parameter(description = "ID do ajudante a ser atualizado.", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody AjudanteRequestDTO ajudanteDTO
    ) {
        Ajudante ajudanteAtualizado = ajudanteService.atualizarAjudante(id, ajudanteDTO);
        return ResponseEntity.ok(ajudanteRepresentationBuilder.toModel(ajudanteAtualizado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir ajudante", description = "Remove um ajudante pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ajudante removido"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ajudante não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Ajudante vinculado a chats",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<Void> deletarAjudante(
            @Parameter(description = "ID do ajudante a ser removido.", example = "1")
            @PathVariable Long id
    ) {
        ajudanteService.deletarAjudante(id);
        return ResponseEntity.noContent().build();
    }
}
