package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.dto.ChatDTO;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.mappers.ChatMapper;
import br.com.fiap.aiury.services.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST responsavel pelo recurso de chat.
 *
 * Papel na API:
 * - expor abertura e manutencao de sessoes de acolhimento;
 * - retornar dados no formato de {@link ChatDTO} para evitar acoplamento
 *   direto da entidade com o contrato HTTP.
 */
@RestController
@RequestMapping("/api/chats")
@Tag(name = "Chats", description = "Operacoes de abertura e manutencao de chats")
public class ChatController {

    private final ChatService chatService;
    private final ChatMapper chatMapper;

    @Autowired
    public ChatController(ChatService chatService, ChatMapper chatMapper) {
        this.chatService = chatService;
        this.chatMapper = chatMapper;
    }

    /**
     * Cria um novo chat.
     *
     * @param chatDTO dados de entrada da sessao
     * @return chat criado convertido para DTO
     */
    @PostMapping
    @Operation(summary = "Criar chat", description = "Abre um novo chat vinculando usuario e ajudante")
    public ResponseEntity<ChatDTO> cadastrarChat(@Valid @RequestBody ChatDTO chatDTO) {
        Chat novoChat = chatService.criarChat(chatDTO);
        return new ResponseEntity<>(chatMapper.toDto(novoChat), HttpStatus.CREATED);
    }

    /**
     * Busca chat por ID.
     *
     * @param id identificador do chat
     * @return chat encontrado convertido para DTO
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar chat por ID", description = "Busca um chat pelo identificador")
    public ResponseEntity<ChatDTO> buscarChatPorId(@PathVariable Long id) {
        Chat chat = chatService.buscarPorId(id);
        return ResponseEntity.ok(chatMapper.toDto(chat));
    }

    /**
     * Lista todos os chats cadastrados.
     *
     * @return colecao de DTOs de chat
     */
    @GetMapping
    @Operation(summary = "Listar chats", description = "Lista todos os chats cadastrados")
    public ResponseEntity<List<ChatDTO>> listarTodos() {
        List<ChatDTO> chats = chatService.buscarTodos()
                .stream()
                .map(chatMapper::toDto)
                .toList();

        return ResponseEntity.ok(chats);
    }

    /**
     * Atualiza um chat existente.
     *
     * @param id identificador do chat alvo
     * @param chatDTO novos dados do chat
     * @return chat atualizado convertido para DTO
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar chat", description = "Atualiza um chat existente pelo ID")
    public ResponseEntity<ChatDTO> atualizarChat(@PathVariable Long id, @Valid @RequestBody ChatDTO chatDTO) {
        Chat chatAtualizado = chatService.atualizarChat(id, chatDTO);
        return ResponseEntity.ok(chatMapper.toDto(chatAtualizado));
    }

    /**
     * Exclui chat por ID.
     *
     * @param id identificador do chat
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir chat", description = "Remove um chat pelo ID")
    public void deletarChat(@PathVariable Long id) {
        chatService.deletarChat(id);
    }
}
