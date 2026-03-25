package br.com.fiap.aiury.representation;

import br.com.fiap.aiury.controller.AjudanteController;
import br.com.fiap.aiury.controller.ChatController;
import br.com.fiap.aiury.controller.MensagemController;
import br.com.fiap.aiury.controller.UsuarioController;
import br.com.fiap.aiury.dto.ChatRequestDTO;
import br.com.fiap.aiury.dto.ChatResponseDTO;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.ChatStatus;
import br.com.fiap.aiury.mappers.ChatMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Construtor de representacoes HATEOAS para o recurso de chat.
 */
@Component
public class ChatRepresentationBuilder {

    private final ChatMapper chatMapper;

    public ChatRepresentationBuilder(ChatMapper chatMapper) {
        this.chatMapper = chatMapper;
    }

    public EntityModel<ChatResponseDTO> toModel(Chat chat) {
        ChatResponseDTO dto = chatMapper.toResponseDto(chat);
        Long chatId = chat.getId();
        Long usuarioId = chat.getUsuario() != null ? chat.getUsuario().getId() : null;
        Long ajudanteId = chat.getAjudante() != null ? chat.getAjudante().getId() : null;

        EntityModel<ChatResponseDTO> model = EntityModel.of(
                dto,
                linkTo(methodOn(ChatController.class).buscarChatPorId(chatId)).withSelfRel(),
                linkTo(methodOn(ChatController.class).listarTodos(null, null, null)).withRel(ApiRelations.CHATS),
                linkTo(methodOn(ChatController.class).atualizarChat(chatId, (ChatRequestDTO) null)).withRel(ApiRelations.ACAO_ATUALIZAR),
                linkTo(methodOn(ChatController.class).deletarChat(chatId)).withRel(ApiRelations.ACAO_EXCLUIR),
                linkTo(methodOn(MensagemController.class).listarTodos(chatId, null)).withRel(ApiRelations.MENSAGENS)
        );

        if (usuarioId != null) {
            model.add(linkTo(methodOn(UsuarioController.class).buscarUsuarioPorId(usuarioId)).withRel(ApiRelations.USUARIO));
        }
        if (ajudanteId != null) {
            model.add(linkTo(methodOn(AjudanteController.class).buscarAjudantePorId(ajudanteId)).withRel(ApiRelations.AJUDANTE));
        }

        return model;
    }

    public CollectionModel<EntityModel<ChatResponseDTO>> toCollection(List<Chat> chats, Long usuarioId, Long ajudanteId, ChatStatus status) {
        List<EntityModel<ChatResponseDTO>> models = chats.stream()
                .map(this::toModel)
                .toList();

        return CollectionModel.of(
                models,
                linkTo(methodOn(ChatController.class).listarTodos(usuarioId, ajudanteId, status)).withSelfRel(),
                linkTo(methodOn(ChatController.class).cadastrarChat((ChatRequestDTO) null)).withRel(ApiRelations.ACAO_CRIAR)
        );
    }
}
