package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.dto.AjudanteDTO;
import br.com.fiap.aiury.dto.AjudanteResponseDTO;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.mappers.AjudanteMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembler HATEOAS para o recurso de ajudante.
 */
@Component
public class AjudanteModelAssembler implements RepresentationModelAssembler<Ajudante, EntityModel<AjudanteResponseDTO>> {

    private final AjudanteMapper ajudanteMapper;

    public AjudanteModelAssembler(AjudanteMapper ajudanteMapper) {
        this.ajudanteMapper = ajudanteMapper;
    }

    @Override
    public EntityModel<AjudanteResponseDTO> toModel(Ajudante ajudante) {
        AjudanteResponseDTO dto = ajudanteMapper.toResponseDto(ajudante);
        Long ajudanteId = ajudante.getId();

        return EntityModel.of(
                dto,
                linkTo(methodOn(AjudanteController.class).buscarAjudantePorId(ajudanteId)).withSelfRel(),
                linkTo(methodOn(AjudanteController.class).listarTodos(null)).withRel("ajudantes"),
                linkTo(methodOn(AjudanteController.class).atualizarAjudante(ajudanteId, (AjudanteDTO) null)).withRel("atualizar"),
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
