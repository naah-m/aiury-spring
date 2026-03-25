package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.dto.ApiErrorResponse;
import br.com.fiap.aiury.dto.MensagemRequestDTO;
import br.com.fiap.aiury.dto.MensagemResponseDTO;
import br.com.fiap.aiury.entities.Mensagem;
import br.com.fiap.aiury.representation.MensagemRepresentationBuilder;
import br.com.fiap.aiury.services.MensagemService;
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
 * Controller REST do recurso de mensagens.
 */
@RestController
@RequestMapping("/api/mensagens")
@Tag(name = "Mensagens", description = "Operacoes de envio e manutencao de mensagens")
public class MensagemController {

    private final MensagemService mensagemService;
    private final MensagemRepresentationBuilder mensagemRepresentationBuilder;

    public MensagemController(MensagemService mensagemService, MensagemRepresentationBuilder mensagemRepresentationBuilder) {
        this.mensagemService = mensagemService;
        this.mensagemRepresentationBuilder = mensagemRepresentationBuilder;
    }

    @PostMapping
    @Operation(summary = "Criar mensagem", description = "Cria uma nova mensagem vinculada a um chat")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Mensagem criada",
                    content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload invalido",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chat ou remetente nao encontrados",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
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
                    description = "Mensagem encontrada",
                    content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Mensagem nao encontrada",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<MensagemResponseDTO>> buscarMensagemPorId(@PathVariable Long id) {
        Mensagem mensagem = mensagemService.buscarPorId(id);
        return ResponseEntity.ok(mensagemRepresentationBuilder.toModel(mensagem));
    }

    @GetMapping
    @Operation(summary = "Listar mensagens", description = "Lista mensagens com filtros opcionais")
    @ApiResponse(
            responseCode = "200",
            description = "Lista retornada com sucesso",
            content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))
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
    @Operation(summary = "Atualizar mensagem", description = "Atualiza uma mensagem existente")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Mensagem atualizada",
                    content = @Content(schema = @Schema(implementation = MensagemResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Payload invalido",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Mensagem, chat ou remetente nao encontrados",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<MensagemResponseDTO>> atualizarMensagem(@PathVariable Long id, @Valid @RequestBody MensagemRequestDTO mensagemDTO) {
        Mensagem mensagemAtualizada = mensagemService.atualizarMensagem(id, mensagemDTO);
        return ResponseEntity.ok(mensagemRepresentationBuilder.toModel(mensagemAtualizada));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir mensagem", description = "Remove uma mensagem pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Mensagem removida"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Mensagem nao encontrada",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<Void> deletarMensagem(@PathVariable Long id) {
        mensagemService.deletarMensagem(id);
        return ResponseEntity.noContent().build();
    }
}
