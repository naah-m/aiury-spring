package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.dto.ApiErrorResponse;
import br.com.fiap.aiury.dto.CidadeRequestDTO;
import br.com.fiap.aiury.dto.CidadeResponseDTO;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.representation.CidadeRepresentationBuilder;
import br.com.fiap.aiury.services.CidadeService;
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
 * Controller REST para catalogo de cidades.
 */
@RestController
@RequestMapping("/api/cidades")
@Tag(name = "Cidades", description = "Operacoes de cadastro e consulta de cidades")
public class CidadeController {

    private final CidadeService cidadeService;
    private final CidadeRepresentationBuilder cidadeRepresentationBuilder;

    public CidadeController(CidadeService cidadeService, CidadeRepresentationBuilder cidadeRepresentationBuilder) {
        this.cidadeService = cidadeService;
        this.cidadeRepresentationBuilder = cidadeRepresentationBuilder;
    }

    @PostMapping
    @Operation(summary = "Criar cidade", description = "Cadastra uma cidade vinculada a um estado")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Cidade criada"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload invalido",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estado nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Cidade duplicada no mesmo estado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<CidadeResponseDTO>> cadastrarCidade(@Valid @RequestBody CidadeRequestDTO cidadeDTO) {
        Cidade novaCidade = cidadeService.criarCidade(cidadeDTO);
        EntityModel<CidadeResponseDTO> resource = cidadeRepresentationBuilder.toModel(novaCidade);
        URI location = linkTo(methodOn(CidadeController.class).buscarCidadePorId(novaCidade.getId())).toUri();
        return ResponseEntity.created(location).body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cidade por ID", description = "Retorna uma cidade pelo identificador")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cidade encontrada"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cidade nao encontrada",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<CidadeResponseDTO>> buscarCidadePorId(@PathVariable Long id) {
        Cidade cidade = cidadeService.buscarPorId(id);
        return ResponseEntity.ok(cidadeRepresentationBuilder.toModel(cidade));
    }

    @GetMapping
    @Operation(summary = "Listar cidades", description = "Lista cidades com filtro opcional por estado")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estado do filtro nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<CollectionModel<EntityModel<CidadeResponseDTO>>> listarCidades(
            @Parameter(description = "Filtro opcional por estado")
            @RequestParam(required = false) Long estadoId
    ) {
        List<Cidade> cidades = cidadeService.buscarTodos(estadoId);
        return ResponseEntity.ok(cidadeRepresentationBuilder.toCollection(cidades, estadoId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cidade", description = "Atualiza uma cidade existente")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cidade atualizada"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload invalido",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cidade ou estado nao encontrados",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Cidade duplicada no mesmo estado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<CidadeResponseDTO>> atualizarCidade(@PathVariable Long id, @Valid @RequestBody CidadeRequestDTO cidadeDTO) {
        Cidade cidadeAtualizada = cidadeService.atualizarCidade(id, cidadeDTO);
        return ResponseEntity.ok(cidadeRepresentationBuilder.toModel(cidadeAtualizada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir cidade", description = "Remove uma cidade pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cidade removida"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cidade nao encontrada",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Cidade vinculada a usuarios",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<Void> deletarCidade(@PathVariable Long id) {
        cidadeService.deletarCidade(id);
        return ResponseEntity.noContent().build();
    }
}
