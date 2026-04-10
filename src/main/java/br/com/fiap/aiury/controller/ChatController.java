package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.configs.OpenApiExamples;
import br.com.fiap.aiury.dto.ApiErrorResponse;
import br.com.fiap.aiury.dto.ChatRequestDTO;
import br.com.fiap.aiury.dto.ChatResponseDTO;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.ChatStatus;
import br.com.fiap.aiury.representation.ChatRepresentationBuilder;
import br.com.fiap.aiury.services.ChatService;
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
 * Controller REST do recurso de chats.
 */
@RestController
@RequestMapping("/api/chats")
@Tag(name = "Chats", description = "Operações de abertura e manutenção de chats")
public class ChatController {

    private final ChatService chatService;
    private final ChatRepresentationBuilder chatRepresentationBuilder;

    public ChatController(ChatService chatService, ChatRepresentationBuilder chatRepresentationBuilder) {
        this.chatService = chatService;
        this.chatRepresentationBuilder = chatRepresentationBuilder;
    }

    @PostMapping
    @Operation(
            summary = "Criar chat",
            description = "Abre um novo chat vinculando usuario e ajudante. Pre-requisitos: usuarioId e ajudanteId devem existir previamente."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Payload de criação de chat. Campos dataInicio/dataFim devem seguir dd/MM/yyyy HH:mm:ss.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ChatRequestDTO.class),
                    examples = @ExampleObject(name = "ChatValido", value = OpenApiExamples.CHAT_REQUEST)
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Chat criado"
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
                    description = "Usuario ou ajudante não encontrados",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "RecursoNaoEncontrado", value = OpenApiExamples.ERROR_NOT_FOUND_GENERIC)
                    )
            )
    })
    public ResponseEntity<EntityModel<ChatResponseDTO>> cadastrarChat(@Valid @RequestBody ChatRequestDTO chatDTO) {
        Chat novoChat = chatService.criarChat(chatDTO);
        EntityModel<ChatResponseDTO> resource = chatRepresentationBuilder.toModel(novoChat);
        URI location = linkTo(methodOn(ChatController.class).buscarChatPorId(novoChat.getId())).toUri();
        return ResponseEntity.created(location).body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar chat por ID", description = "Retorna um chat pelo identificador")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Chat encontrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chat não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<ChatResponseDTO>> buscarChatPorId(
            @Parameter(description = "ID do chat. Deve existir previamente.", example = "1")
            @PathVariable Long id
    ) {
        Chat chat = chatService.buscarPorId(id);
        return ResponseEntity.ok(chatRepresentationBuilder.toModel(chat));
    }

    @GetMapping
    @Operation(summary = "Listar chats", description = "Lista chats com filtros opcionais")
    @ApiResponse(
            responseCode = "200",
            description = "Lista retornada com sucesso"
    )
    public ResponseEntity<CollectionModel<EntityModel<ChatResponseDTO>>> listarTodos(
            @Parameter(description = "Filtro opcional por usuario")
            @RequestParam(required = false) Long usuarioId,
            @Parameter(description = "Filtro opcional por ajudante")
            @RequestParam(required = false) Long ajudanteId,
            @Parameter(description = "Filtro opcional por status do chat")
            @RequestParam(required = false) ChatStatus status
    ) {
        List<Chat> chats = chatService.buscarTodos(usuarioId, ajudanteId, status);
        return ResponseEntity.ok(chatRepresentationBuilder.toCollection(chats, usuarioId, ajudanteId, status));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar chat",
            description = "Atualiza um chat existente. Pre-requisitos: id do chat, usuarioId e ajudanteId devem existir."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            description = "Payload de atualização de chat. Campos dataInicio/dataFim devem seguir dd/MM/yyyy HH:mm:ss.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ChatRequestDTO.class),
                    examples = @ExampleObject(name = "ChatAtualizacao", value = OpenApiExamples.CHAT_REQUEST)
            )
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Chat atualizado"
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
                    description = "Chat, usuario ou ajudante não encontrados",
                    content = @Content(
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "RecursoNaoEncontrado", value = OpenApiExamples.ERROR_NOT_FOUND_GENERIC)
                    )
            )
    })
    public ResponseEntity<EntityModel<ChatResponseDTO>> atualizarChat(
            @Parameter(description = "ID do chat a ser atualizado.", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ChatRequestDTO chatDTO
    ) {
        Chat chatAtualizado = chatService.atualizarChat(id, chatDTO);
        return ResponseEntity.ok(chatRepresentationBuilder.toModel(chatAtualizado));
    }

    @PostMapping("/{id}/encerrar")
    @Operation(
            summary = "Encerrar chat",
            description = "Permite que o usuario ou ajudante vinculado ao atendimento finalize o chat com seu proprio perfil."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Chat encerrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chat nao encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<EntityModel<ChatResponseDTO>> encerrarChat(
            @Parameter(description = "ID do chat a ser encerrado.", example = "1")
            @PathVariable Long id
    ) {
        Chat chatEncerrado = chatService.encerrarChat(id);
        return ResponseEntity.ok(chatRepresentationBuilder.toModel(chatEncerrado));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir chat", description = "Remove um chat pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Chat removido"),
            @ApiResponse(
                    responseCode = "404",
                    description = "Chat não encontrado",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<Void> deletarChat(
            @Parameter(description = "ID do chat a ser removido.", example = "1")
            @PathVariable Long id
    ) {
        chatService.deletarChat(id);
        return ResponseEntity.noContent().build();
    }
}

