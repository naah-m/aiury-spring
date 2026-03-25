package br.com.fiap.aiury.representation;

import br.com.fiap.aiury.controller.CidadeController;
import br.com.fiap.aiury.controller.EstadoController;
import br.com.fiap.aiury.controller.UsuarioController;
import br.com.fiap.aiury.dto.CidadeRequestDTO;
import br.com.fiap.aiury.dto.CidadeResponseDTO;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.mappers.CidadeMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Construtor de representacoes HATEOAS para o recurso de cidade.
 */
@Component
public class CidadeRepresentationBuilder {

    private final CidadeMapper cidadeMapper;

    public CidadeRepresentationBuilder(CidadeMapper cidadeMapper) {
        this.cidadeMapper = cidadeMapper;
    }

    public EntityModel<CidadeResponseDTO> toModel(Cidade cidade) {
        CidadeResponseDTO dto = cidadeMapper.toResponseDto(cidade);
        Long cidadeId = cidade.getId();
        Long estadoId = dto.getEstadoId();

        EntityModel<CidadeResponseDTO> model = EntityModel.of(
                dto,
                linkTo(methodOn(CidadeController.class).buscarCidadePorId(cidadeId)).withSelfRel(),
                linkTo(methodOn(CidadeController.class).listarCidades(null)).withRel(ApiRelations.CIDADES),
                linkTo(methodOn(CidadeController.class).atualizarCidade(cidadeId, (CidadeRequestDTO) null)).withRel(ApiRelations.ACAO_ATUALIZAR),
                linkTo(methodOn(CidadeController.class).deletarCidade(cidadeId)).withRel(ApiRelations.ACAO_EXCLUIR),
                linkTo(methodOn(UsuarioController.class).listarTodos(cidadeId)).withRel(ApiRelations.USUARIOS)
        );

        if (estadoId != null) {
            model.add(linkTo(methodOn(EstadoController.class).buscarEstadoPorId(estadoId)).withRel(ApiRelations.ESTADO));
        }

        return model;
    }

    public CollectionModel<EntityModel<CidadeResponseDTO>> toCollection(List<Cidade> cidades, Long estadoId) {
        List<EntityModel<CidadeResponseDTO>> models = cidades.stream()
                .map(this::toModel)
                .toList();

        return CollectionModel.of(
                models,
                linkTo(methodOn(CidadeController.class).listarCidades(estadoId)).withSelfRel(),
                linkTo(methodOn(CidadeController.class).cadastrarCidade((CidadeRequestDTO) null)).withRel(ApiRelations.ACAO_CRIAR)
        );
    }
}
