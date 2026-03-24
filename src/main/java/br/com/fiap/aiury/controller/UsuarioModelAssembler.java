package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.dto.UsuarioDTO;
import br.com.fiap.aiury.entities.Usuario;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Assembler responsavel por enriquecer {@link Usuario} com links HATEOAS.
 *
 * Papel na arquitetura:
 * - centraliza montagem de links para evitar repeticao no controller;
 * - padroniza representacao individual e de colecao de usuarios.
 */
@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    /**
     * Converte entidade de usuario em recurso HATEOAS.
     *
     * @param usuario entidade de dominio
     * @return recurso enriquecido com links de navegacao
     */
    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(
                usuario,
                linkTo(methodOn(UsuarioController.class).buscarUsuarioPorId(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).listarTodos()).withRel("todos-usuarios"),
                linkTo(methodOn(UsuarioController.class).atualizarUsuario(usuario.getId(), (UsuarioDTO) null)).withRel("atualizar"),
                linkTo(UsuarioController.class).slash(usuario.getId()).withRel("excluir")
        );
    }

    /**
     * Converte lista de usuarios em colecao HATEOAS.
     *
     * @param usuarios lista de entidades de usuario
     * @return colecao com links globais e individuais
     */
    public CollectionModel<EntityModel<Usuario>> toCollection(List<Usuario> usuarios) {
        List<EntityModel<Usuario>> models = usuarios.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(
                models,
                linkTo(methodOn(UsuarioController.class).listarTodos()).withSelfRel()
        );
    }
}
