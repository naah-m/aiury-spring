package br.com.fiap.aiury.representation;

import br.com.fiap.aiury.controller.AjudanteController;
import br.com.fiap.aiury.controller.ChatController;
import br.com.fiap.aiury.controller.MensagemController;
import br.com.fiap.aiury.controller.UsuarioController;
import br.com.fiap.aiury.dto.MensagemRequestDTO;
import br.com.fiap.aiury.dto.MensagemResponseDTO;
import br.com.fiap.aiury.entities.Mensagem;
import br.com.fiap.aiury.mappers.MensagemMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Construtor de representacoes HATEOAS para o recurso de mensagem.
 */
@Component
public class MensagemRepresentationBuilder {

    private final MensagemMapper mensagemMapper;

    public MensagemRepresentationBuilder(MensagemMapper mensagemMapper) {
        this.mensagemMapper = mensagemMapper;
    }

    public EntityModel<MensagemResponseDTO> toModel(Mensagem mensagem) {
        MensagemResponseDTO dto = mensagemMapper.toResponseDto(mensagem);
        Long mensagemId = mensagem.getId();
        Long chatId = mensagem.getChat() != null ? mensagem.getChat().getId() : null;
        Long remetenteUsuarioId = mensagem.getRemetente() != null ? mensagem.getRemetente().getId() : null;
        Long remetenteAjudanteId = mensagem.getRemetenteAjudante() != null ? mensagem.getRemetenteAjudante().getId() : null;

        EntityModel<MensagemResponseDTO> model = EntityModel.of(
                dto,
                linkTo(methodOn(MensagemController.class).buscarMensagemPorId(mensagemId)).withSelfRel(),
                linkTo(methodOn(MensagemController.class).listarTodos(null, null)).withRel(ApiRelations.MENSAGENS),
                linkTo(methodOn(MensagemController.class).atualizarMensagem(mensagemId, (MensagemRequestDTO) null)).withRel(ApiRelations.ACAO_ATUALIZAR),
                linkTo(methodOn(MensagemController.class).deletarMensagem(mensagemId)).withRel(ApiRelations.ACAO_EXCLUIR)
        );

        if (chatId != null) {
            model.add(linkTo(methodOn(ChatController.class).buscarChatPorId(chatId)).withRel(ApiRelations.CHAT));
        }
        if (remetenteUsuarioId != null) {
            model.add(linkTo(methodOn(UsuarioController.class).buscarUsuarioPorId(remetenteUsuarioId)).withRel(ApiRelations.REMETENTE));
        }
        if (remetenteAjudanteId != null) {
            model.add(linkTo(methodOn(AjudanteController.class).buscarAjudantePorId(remetenteAjudanteId)).withRel(ApiRelations.AJUDANTE));
        }

        return model;
    }

    public CollectionModel<EntityModel<MensagemResponseDTO>> toCollection(List<Mensagem> mensagens, Long chatId, Long remetenteId) {
        List<EntityModel<MensagemResponseDTO>> models = mensagens.stream()
                .map(this::toModel)
                .toList();

        return CollectionModel.of(
                models,
                linkTo(methodOn(MensagemController.class).listarTodos(chatId, remetenteId)).withSelfRel(),
                linkTo(methodOn(MensagemController.class).cadastrarMensagem((MensagemRequestDTO) null)).withRel(ApiRelations.ACAO_CRIAR)
        );
    }
}
