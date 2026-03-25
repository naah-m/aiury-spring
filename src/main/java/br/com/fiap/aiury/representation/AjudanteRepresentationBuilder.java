package br.com.fiap.aiury.representation;

import br.com.fiap.aiury.controller.AjudanteController;
import br.com.fiap.aiury.controller.ChatController;
import br.com.fiap.aiury.dto.AjudanteRequestDTO;
import br.com.fiap.aiury.dto.AjudanteResponseDTO;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.mappers.AjudanteMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Construtor de representacoes HATEOAS para o recurso de ajudante.
 */
@Component
public class AjudanteRepresentationBuilder {

    private final AjudanteMapper ajudanteMapper;

    public AjudanteRepresentationBuilder(AjudanteMapper ajudanteMapper) {
        this.ajudanteMapper = ajudanteMapper;
    }

    public EntityModel<AjudanteResponseDTO> toModel(Ajudante ajudante) {
        AjudanteResponseDTO dto = ajudanteMapper.toResponseDto(ajudante);
        Long ajudanteId = ajudante.getId();

        return EntityModel.of(
                dto,
                linkTo(methodOn(AjudanteController.class).buscarAjudantePorId(ajudanteId)).withSelfRel(),
                linkTo(methodOn(AjudanteController.class).listarTodos(null)).withRel("ajudantes"),
                linkTo(methodOn(AjudanteController.class).atualizarAjudante(ajudanteId, (AjudanteRequestDTO) null)).withRel("atualizar"),
                linkTo(methodOn(AjudanteController.class).deletarAjudante(ajudanteId)).withRel("excluir"),
                linkTo(methodOn(ChatController.class).listarTodos(null, ajudanteId, null)).withRel("chats-do-ajudante")
        );
    }

    public CollectionModel<EntityModel<AjudanteResponseDTO>> toCollection(List<Ajudante> ajudantes, Boolean disponivel) {
        List<EntityModel<AjudanteResponseDTO>> models = ajudantes.stream()
                .map(this::toModel)
                .toList();

        return CollectionModel.of(
                models,
                linkTo(methodOn(AjudanteController.class).listarTodos(disponivel)).withSelfRel()
        );
    }
}
