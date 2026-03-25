package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.dto.ChatDTO;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.ChatStatus;
import br.com.fiap.aiury.mappers.ChatMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembler HATEOAS para o recurso de chat.
 */
@Component
public class ChatModelAssembler implements RepresentationModelAssembler<Chat, EntityModel<ChatDTO>> {

    private final ChatMapper chatMapper;

    public ChatModelAssembler(ChatMapper chatMapper) {
        this.chatMapper = chatMapper;
    }

    @Override
    public EntityModel<ChatDTO> toModel(Chat chat) {
        ChatDTO dto = chatMapper.toDto(chat);
        Long chatId = chat.getId();
        Long usuarioId = chat.getUsuario() != null ? chat.getUsuario().getId() : null;
        Long ajudanteId = chat.getAjudante() != null ? chat.getAjudante().getId() : null;

        EntityModel<ChatDTO> model = EntityModel.of(
                dto,
                linkTo(methodOn(ChatController.class).buscarChatPorId(chatId)).withSelfRel(),
                linkTo(methodOn(ChatController.class).listarTodos(null, null, null)).withRel("chats"),
                linkTo(methodOn(ChatController.class).atualizarChat(chatId, (ChatDTO) null)).withRel("atualizar"),
                linkTo(methodOn(ChatController.class).deletarChat(chatId)).withRel("excluir"),
                linkTo(methodOn(MensagemController.class).listarTodos(chatId, null)).withRel("mensagens")
        );

        if (usuarioId != null) {
            model.add(linkTo(methodOn(UsuarioController.class).buscarUsuarioPorId(usuarioId)).withRel("usuario"));
        }
        if (ajudanteId != null) {
            model.add(linkTo(methodOn(AjudanteController.class).buscarAjudantePorId(ajudanteId)).withRel("ajudante"));
        }

        return model;
    }

    public CollectionModel<EntityModel<ChatDTO>> toCollection(List<Chat> chats, Long usuarioId, Long ajudanteId, ChatStatus status) {
        List<EntityModel<ChatDTO>> models = chats.stream()
                .map(this::toModel)
                .toList();

        return CollectionModel.of(
                models,
                linkTo(methodOn(ChatController.class).listarTodos(usuarioId, ajudanteId, status)).withSelfRel()
        );
    }
}
