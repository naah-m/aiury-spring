package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.representation.ApiRelations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Entry point hipermidia da API.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Root", description = "Navegacao inicial da API via HATEOAS")
public class ApiRootController {

    @GetMapping
    @Operation(summary = "Entrypoint da API", description = "Retorna links para os principais recursos da API")
    @ApiResponse(responseCode = "200", description = "Links de navegacao retornados com sucesso")
    public RepresentationModel<?> root() {
        RepresentationModel<?> model = new RepresentationModel<>();
        model.add(linkTo(methodOn(ApiRootController.class).root()).withSelfRel());
        model.add(linkTo(methodOn(UsuarioController.class).listarTodos(null)).withRel(ApiRelations.USUARIOS));
        model.add(linkTo(methodOn(AjudanteController.class).listarTodos(null)).withRel(ApiRelations.AJUDANTES));
        model.add(linkTo(methodOn(ChatController.class).listarTodos(null, null, null)).withRel(ApiRelations.CHATS));
        model.add(linkTo(methodOn(MensagemController.class).listarTodos(null, null)).withRel(ApiRelations.MENSAGENS));
        model.add(linkTo(methodOn(CidadeController.class).listarCidades(null)).withRel(ApiRelations.CIDADES));
        model.add(linkTo(methodOn(EstadoController.class).listarEstados(null)).withRel(ApiRelations.ESTADOS));
        return model;
    }
}
