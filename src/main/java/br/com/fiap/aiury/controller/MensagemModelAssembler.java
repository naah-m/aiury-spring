package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.dto.MensagemDTO;
import br.com.fiap.aiury.entities.Mensagem;
import br.com.fiap.aiury.mappers.MensagemMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembler HATEOAS para o recurso de mensagem.
 */
@Component
public class MensagemModelAssembler implements RepresentationModelAssembler<Mensagem, EntityModel<MensagemDTO>> {

    private final MensagemMapper mensagemMapper;

    public MensagemModelAssembler(MensagemMapper mensagemMapper) {
        this.mensagemMapper = mensagemMapper;
    }

    @Override
    public EntityModel<MensagemDTO> toModel(Mensagem mensagem) {
        MensagemDTO dto = mensagemMapper.toDto(mensagem);
        Long mensagemId = mensagem.getId();
        Long chatId = mensagem.getChat() != null ? mensagem.getChat().getId() : null;
        Long remetenteId = mensagem.getRemetente() != null ? mensagem.getRemetente().getId() : null;

        EntityModel<MensagemDTO> model = EntityModel.of(
                dto,
                linkTo(methodOn(MensagemController.class).buscarMensagemPorId(mensagemId)).withSelfRel(),
                linkTo(methodOn(MensagemController.class).listarTodos(null, null)).withRel("mensagens"),
                linkTo(methodOn(MensagemController.class).atualizarMensagem(mensagemId, (MensagemDTO) null)).withRel("atualizar"),
                linkTo(methodOn(MensagemController.class).deletarMensagem(mensagemId)).withRel("excluir")
        );

        if (chatId != null) {
            model.add(linkTo(methodOn(ChatController.class).buscarChatPorId(chatId)).withRel("chat"));
        }
        if (remetenteId != null) {
            model.add(linkTo(methodOn(UsuarioController.class).buscarUsuarioPorId(remetenteId)).withRel("remetente"));
        }

        return model;
    }

    public CollectionModel<EntityModel<MensagemDTO>> toCollection(List<Mensagem> mensagens, Long chatId, Long remetenteId) {
        List<EntityModel<MensagemDTO>> models = mensagens.stream()
                .map(this::toModel)
                .toList();

        return CollectionModel.of(
                models,
                linkTo(methodOn(MensagemController.class).listarTodos(chatId, remetenteId)).withSelfRel()
        );
    }
}
