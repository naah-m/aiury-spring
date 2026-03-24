package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.dto.AjudanteDTO;
import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.services.AjudanteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ajudantes")
@Tag(name = "Ajudantes", description = "Operacoes de cadastro e manutencao de ajudantes")
public class AjudanteController {

    private final AjudanteService ajudanteService;

    @Autowired
    public AjudanteController(AjudanteService ajudanteService) {
        this.ajudanteService = ajudanteService;
    }

    @PostMapping
    @Operation(summary = "Criar ajudante", description = "Cadastra um novo ajudante")
    public ResponseEntity<Ajudante> cadastrarAjudante(@Valid @RequestBody AjudanteDTO ajudanteDTO) {
        Ajudante novoAjudante = ajudanteService.criarAjudante(ajudanteDTO);
        return new ResponseEntity<>(novoAjudante, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ajudante por ID", description = "Busca um ajudante pelo identificador")
    public ResponseEntity<Ajudante> buscarAjudantePorId(@PathVariable Long id) {
        Ajudante ajudante = ajudanteService.buscarPorId(id);
        return ResponseEntity.ok(ajudante);
    }

    @GetMapping
    @Operation(summary = "Listar ajudantes", description = "Lista todos os ajudantes cadastrados")
    public ResponseEntity<List<Ajudante>> listarTodos() {
        List<Ajudante> ajudantes = ajudanteService.buscarTodos();
        return ResponseEntity.ok(ajudantes);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar ajudante", description = "Atualiza um ajudante existente pelo ID")
    public ResponseEntity<Ajudante> atualizarAjudante(@PathVariable Long id, @Valid @RequestBody AjudanteDTO ajudanteDTO) {
        Ajudante ajudanteAtualizado = ajudanteService.atualizarAjudante(id, ajudanteDTO);
        return ResponseEntity.ok(ajudanteAtualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir ajudante", description = "Remove um ajudante pelo ID")
    public void deletarAjudante(@PathVariable Long id) {
        ajudanteService.deletarAjudante(id);
    }
}
