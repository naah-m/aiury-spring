package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.dto.UsuarioDTO;
import br.com.fiap.aiury.dto.UsuarioResponseDTO;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.mappers.UsuarioMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembler responsavel por enriquecer representacoes de usuario com links HATEOAS.
 */
@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<UsuarioResponseDTO>> {

    private final UsuarioMapper usuarioMapper;

    public UsuarioModelAssembler(UsuarioMapper usuarioMapper) {
        this.usuarioMapper = usuarioMapper;
    }

    @Override
    public EntityModel<UsuarioResponseDTO> toModel(Usuario usuario) {
        UsuarioResponseDTO dto = usuarioMapper.toResponseDto(usuario);
        Long usuarioId = usuario.getId();

        return EntityModel.of(
                dto,
                linkTo(methodOn(UsuarioController.class).buscarUsuarioPorId(usuarioId)).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarTodos(null)).withRel("usuarios"),
                linkTo(methodOn(UsuarioController.class).atualizarUsuario(usuarioId, (UsuarioDTO) null)).withRel("atualizar"),
                linkTo(methodOn(UsuarioController.class).deletarUsuario(usuarioId)).withRel("excluir"),
                linkTo(methodOn(ChatController.class).listarTodos(usuarioId, null, null)).withRel("chats"),
                linkTo(methodOn(MensagemController.class).listarTodos(null, usuarioId)).withRel("mensagens-enviadas")
        );
    }

    public CollectionModel<EntityModel<UsuarioResponseDTO>> toCollection(List<Usuario> usuarios, Long cidadeId) {
        List<EntityModel<UsuarioResponseDTO>> models = usuarios.stream()
                .map(this::toModel)
                .toList();

        return CollectionModel.of(
                models,
                linkTo(methodOn(UsuarioController.class).listarTodos(cidadeId)).withSelfRel()
        );
    }
}
