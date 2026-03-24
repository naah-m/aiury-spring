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

    @PostMapping
    @Operation(summary = "Criar chat", description = "Abre um novo chat vinculando usuario e ajudante")
    public ResponseEntity<ChatDTO> cadastrarChat(@Valid @RequestBody ChatDTO chatDTO) {
        Chat novoChat = chatService.criarChat(chatDTO);
        return new ResponseEntity<>(chatMapper.toDto(novoChat), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar chat por ID", description = "Busca um chat pelo identificador")
    public ResponseEntity<ChatDTO> buscarChatPorId(@PathVariable Long id) {
        Chat chat = chatService.buscarPorId(id);
        return ResponseEntity.ok(chatMapper.toDto(chat));
    }

    @GetMapping
    @Operation(summary = "Listar chats", description = "Lista todos os chats cadastrados")
    public ResponseEntity<List<ChatDTO>> listarTodos() {
        List<ChatDTO> chats = chatService.buscarTodos()
                .stream()
                .map(chatMapper::toDto)
                .toList();

        return ResponseEntity.ok(chats);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar chat", description = "Atualiza um chat existente pelo ID")
    public ResponseEntity<ChatDTO> atualizarChat(@PathVariable Long id, @Valid @RequestBody ChatDTO chatDTO) {
        Chat chatAtualizado = chatService.atualizarChat(id, chatDTO);
        return ResponseEntity.ok(chatMapper.toDto(chatAtualizado));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir chat", description = "Remove um chat pelo ID")
    public void deletarChat(@PathVariable Long id) {
        chatService.deletarChat(id);
    }
}
