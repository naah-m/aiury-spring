package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.configs.OpenApiExamples;
import br.com.fiap.aiury.dto.ApiErrorResponse;
import br.com.fiap.aiury.dto.MensagemRequestDTO;
import br.com.fiap.aiury.dto.MensagemResponseDTO;
import br.com.fiap.aiury.entities.Mensagem;
import br.com.fiap.aiury.representation.MensagemRepresentationBuilder;
import br.com.fiap.aiury.services.MensagemService;
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
 * Controller REST do recurso de mensagens.
 */
@RestController
@RequestMapping("/api/mensagens")
@Tag(name = "Mensagens", description = "Operações de envio e manutenção de mensagens")
public class MensagemController {

    private final MensagemService mensagemService;
    private final MensagemRepresentationBuilder mensagemRepresentationBuilder;

    public MensagemController(MensagemService mensagemService, MensagemRepresentationBuilder mensagemRepresentationBuilder) {
        this.mensagemService = mensagemService;
        this.mensagemRepresentationBuilder = mensagemRepresentationBuilder;
    }

    @PostMapping
    @Operation(
            summary = "Criar mensagem",
            description = "Cria uma nova mensagem vinculada a um chat. Pre-requisitos: chatId e remetente (usuario ou ajudante) devem existir e pertencer ao chat."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Payload de criação de mensagem. Campo dataEnvio deve seguir dd/MM/yyyy HH:mm:ss.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MensagemRequestDTO.class),
                    examples = @ExampleObject(name = "MensagemValida", value = OpenApiExamples.MENSAGEM_REQUEST)
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Mensagem criada"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload inválido",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "ErroValidacao", value = OpenApiExamples.ERROR_VALIDATION_GENERIC),
                                    @ExampleObject(name = "FormatoDataHoraInvalido", value = OpenApiExamples.ERROR_INVALID_DATE_TIME)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chat ou remetente não encontrados",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "RecursoNaoEncontrado", value = OpenApiExamples.ERROR_NOT_FOUND_GENERIC)
                    )
            )
    })
    public ResponseEntity<EntityModel<MensagemResponseDTO>> cadastrarMensagem(@Valid @RequestBody MensagemRequestDTO mensagemDTO) {
        Mensagem novaMensagem = mensagemService.criarMensagem(mensagemDTO);
        EntityModel<MensagemResponseDTO> resource = mensagemRepresentationBuilder.toModel(novaMensagem);
        URI location = linkTo(methodOn(MensagemController.class).buscarMensagemPorId(novaMensagem.getId())).toUri();
        return ResponseEntity.created(location).body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar mensagem por ID", description = "Retorna uma mensagem pelo identificador")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Mensagem encontrada"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Mensagem não encontrada",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<MensagemResponseDTO>> buscarMensagemPorId(
            @Parameter(description = "ID da mensagem. Deve existir previamente.", example = "1")
            @PathVariable Long id
    ) {
        Mensagem mensagem = mensagemService.buscarPorId(id);
        return ResponseEntity.ok(mensagemRepresentationBuilder.toModel(mensagem));
    }

    @GetMapping
    @Operation(summary = "Listar mensagens", description = "Lista mensagens com filtros opcionais")
    @ApiResponse(
            responseCode = "200",
            description = "Lista retornada com sucesso"
    )
    public ResponseEntity<CollectionModel<EntityModel<MensagemResponseDTO>>> listarTodos(
            @Parameter(description = "Filtro opcional por chat")
            @RequestParam(required = false) Long chatId,
            @Parameter(description = "Filtro opcional por remetente")
            @RequestParam(required = false) Long remetenteId
    ) {
        List<Mensagem> mensagens = mensagemService.buscarTodos(chatId, remetenteId);
        return ResponseEntity.ok(mensagemRepresentationBuilder.toCollection(mensagens, chatId, remetenteId));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar mensagem",
            description = "Atualiza uma mensagem existente. Pre-requisitos: id da mensagem, chatId e remetente (usuario ou ajudante) devem existir."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Payload de atualização de mensagem. Campo dataEnvio deve seguir dd/MM/yyyy HH:mm:ss.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = MensagemRequestDTO.class),
                    examples = @ExampleObject(name = "MensagemAtualizacao", value = OpenApiExamples.MENSAGEM_REQUEST)
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Mensagem atualizada"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload inválido",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = {
                                    @ExampleObject(name = "ErroValidacao", value = OpenApiExamples.ERROR_VALIDATION_GENERIC),
                                    @ExampleObject(name = "FormatoDataHoraInvalido", value = OpenApiExamples.ERROR_INVALID_DATE_TIME)
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Mensagem, chat ou remetente não encontrados",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "RecursoNaoEncontrado", value = OpenApiExamples.ERROR_NOT_FOUND_GENERIC)
                    )
            )
    })
    public ResponseEntity<EntityModel<MensagemResponseDTO>> atualizarMensagem(
            @Parameter(description = "ID da mensagem a ser atualizada.", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody MensagemRequestDTO mensagemDTO
    ) {
        Mensagem mensagemAtualizada = mensagemService.atualizarMensagem(id, mensagemDTO);
        return ResponseEntity.ok(mensagemRepresentationBuilder.toModel(mensagemAtualizada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir mensagem", description = "Remove uma mensagem pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Mensagem removida"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Mensagem não encontrada",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<Void> deletarMensagem(
            @Parameter(description = "ID da mensagem a ser removida.", example = "1")
            @PathVariable Long id
    ) {
        mensagemService.deletarMensagem(id);
        return ResponseEntity.noContent().build();
    }
}

