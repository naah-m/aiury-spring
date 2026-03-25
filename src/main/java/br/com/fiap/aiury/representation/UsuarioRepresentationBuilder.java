package br.com.fiap.aiury.representation;

import br.com.fiap.aiury.controller.ChatController;
import br.com.fiap.aiury.controller.CidadeController;
import br.com.fiap.aiury.controller.EstadoController;
import br.com.fiap.aiury.controller.MensagemController;
import br.com.fiap.aiury.controller.UsuarioController;
import br.com.fiap.aiury.dto.UsuarioRequestDTO;
import br.com.fiap.aiury.dto.UsuarioResponseDTO;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.mappers.UsuarioMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Construtor de representacoes HATEOAS para o recurso de usuario.
 */
@Component
public class UsuarioRepresentationBuilder {

    private final UsuarioMapper usuarioMapper;

    public UsuarioRepresentationBuilder(UsuarioMapper usuarioMapper) {
        this.usuarioMapper = usuarioMapper;
    }

    public EntityModel<UsuarioResponseDTO> toModel(Usuario usuario) {
        UsuarioResponseDTO dto = usuarioMapper.toResponseDto(usuario);
        Long usuarioId = usuario.getId();
        Long cidadeId = usuario.getCidade() != null ? usuario.getCidade().getId() : null;

        EntityModel<UsuarioResponseDTO> model = EntityModel.of(
                dto,
                linkTo(methodOn(UsuarioController.class).buscarUsuarioPorId(usuarioId)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarTodos(null)).withRel(ApiRelations.USUARIOS),
                linkTo(methodOn(UsuarioController.class).atualizarUsuario(usuarioId, (UsuarioRequestDTO) null)).withRel(ApiRelations.ACAO_ATUALIZAR),
                linkTo(methodOn(UsuarioController.class).deletarUsuario(usuarioId)).withRel(ApiRelations.ACAO_EXCLUIR),
                linkTo(methodOn(ChatController.class).listarTodos(usuarioId, null, null)).withRel(ApiRelations.CHATS),
                linkTo(methodOn(MensagemController.class).listarTodos(null, usuarioId)).withRel(ApiRelations.MENSAGENS)
        );

        if (cidadeId != null) {
            model.add(linkTo(methodOn(CidadeController.class).buscarCidadePorId(cidadeId)).withRel(ApiRelations.CIDADE));
            if (usuario.getCidade().getEstado() != null) {
                Long estadoId = usuario.getCidade().getEstado().getId();
                model.add(linkTo(methodOn(EstadoController.class).buscarEstadoPorId(estadoId)).withRel(ApiRelations.ESTADO));
            }
        }

        return model;
    }

    public CollectionModel<EntityModel<UsuarioResponseDTO>> toCollection(List<Usuario> usuarios, Long cidadeId) {
        List<EntityModel<UsuarioResponseDTO>> models = usuarios.stream()
                .map(this::toModel)
                .toList();

        return CollectionModel.of(
                models,
                linkTo(methodOn(UsuarioController.class).listarTodos(cidadeId)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).cadastrarUsuario((UsuarioRequestDTO) null)).withRel(ApiRelations.ACAO_CRIAR)
        );
    }
}
