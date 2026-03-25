package br.com.fiap.aiury.representation;

import br.com.fiap.aiury.controller.CidadeController;
import br.com.fiap.aiury.controller.EstadoController;
import br.com.fiap.aiury.dto.EstadoRequestDTO;
import br.com.fiap.aiury.dto.EstadoResponseDTO;
import br.com.fiap.aiury.entities.Estado;
import br.com.fiap.aiury.mappers.EstadoMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Construtor de representacoes HATEOAS para o recurso de estado.
 */
@Component
public class EstadoRepresentationBuilder {

    private final EstadoMapper estadoMapper;

    public EstadoRepresentationBuilder(EstadoMapper estadoMapper) {
        this.estadoMapper = estadoMapper;
    }

    public EntityModel<EstadoResponseDTO> toModel(Estado estado) {
        EstadoResponseDTO dto = estadoMapper.toResponseDto(estado);
        Long estadoId = estado.getId();

        return EntityModel.of(
                dto,
                linkTo(methodOn(EstadoController.class).buscarEstadoPorId(estadoId)).withSelfRel(),
                linkTo(methodOn(EstadoController.class).listarEstados(null)).withRel(ApiRelations.ESTADOS),
                linkTo(methodOn(EstadoController.class).atualizarEstado(estadoId, (EstadoRequestDTO) null)).withRel(ApiRelations.ACAO_ATUALIZAR),
                linkTo(methodOn(EstadoController.class).deletarEstado(estadoId)).withRel(ApiRelations.ACAO_EXCLUIR),
                linkTo(methodOn(CidadeController.class).listarCidades(estadoId)).withRel(ApiRelations.CIDADES)
        );
    }

    public CollectionModel<EntityModel<EstadoResponseDTO>> toCollection(List<Estado> estados, String uf) {
        List<EntityModel<EstadoResponseDTO>> models = estados.stream()
                .map(this::toModel)
                .toList();

        return CollectionModel.of(
                models,
                linkTo(methodOn(EstadoController.class).listarEstados(uf)).withSelfRel(),
                linkTo(methodOn(EstadoController.class).cadastrarEstado((EstadoRequestDTO) null)).withRel(ApiRelations.ACAO_CRIAR)
        );
    }
}
