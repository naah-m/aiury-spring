package br.com.fiap.aiury.controller;

import br.com.fiap.aiury.dto.MensagemDTO;
import br.com.fiap.aiury.entities.Mensagem;
import br.com.fiap.aiury.mappers.MensagemMapper;
import br.com.fiap.aiury.services.MensagemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST do recurso de mensagens.
 *
 * Responsabilidades:
 * - disponibilizar CRUD basico de mensagens;
 * - converter entidade para DTO de resposta;
 * - manter contratos HTTP desacoplados da modelagem interna.
 */
@RestController
@RequestMapping("/api/mensagens")
@Tag(name = "Mensagens", description = "Operacoes de envio e manutencao de mensagens")
public class MensagemController {

    private final MensagemService mensagemService;
    private final MensagemMapper mensagemMapper;

    @Autowired
    public MensagemController(MensagemService mensagemService, MensagemMapper mensagemMapper) {
        this.mensagemService = mensagemService;
        this.mensagemMapper = mensagemMapper;
    }

    /**
     * Cria nova mensagem vinculada a um chat.
     *
     * @param mensagemDTO dados de entrada da mensagem
     * @return mensagem criada em formato DTO
     */
    @PostMapping
    @Operation(summary = "Criar mensagem", description = "Cria uma nova mensagem vinculada a um chat")
    public ResponseEntity<MensagemDTO> cadastrarMensagem(@Valid @RequestBody MensagemDTO mensagemDTO) {
        Mensagem novaMensagem = mensagemService.criarMensagem(mensagemDTO);
        return new ResponseEntity<>(mensagemMapper.toDto(novaMensagem), HttpStatus.CREATED);
    }

    /**
     * Busca mensagem por identificador.
     *
     * @param id identificador da mensagem
     * @return mensagem encontrada em formato DTO
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar mensagem por ID", description = "Busca uma mensagem pelo identificador")
    public ResponseEntity<MensagemDTO> buscarMensagemPorId(@PathVariable Long id) {
        Mensagem mensagem = mensagemService.buscarPorId(id);
        return ResponseEntity.ok(mensagemMapper.toDto(mensagem));
    }

    /**
     * Lista todas as mensagens cadastradas.
     *
     * @return lista de mensagens convertidas para DTO
     */
    @GetMapping
    @Operation(summary = "Listar mensagens", description = "Lista todas as mensagens cadastradas")
    public ResponseEntity<List<MensagemDTO>> listarTodos() {
        List<MensagemDTO> mensagens = mensagemService.buscarTodos()
                .stream()
                .map(mensagemMapper::toDto)
                .toList();

        return ResponseEntity.ok(mensagens);
    }

    /**
     * Atualiza mensagem existente.
     *
     * @param id identificador da mensagem
     * @param mensagemDTO novos dados da mensagem
     * @return mensagem atualizada em formato DTO
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar mensagem", description = "Atualiza uma mensagem existente pelo ID")
    public ResponseEntity<MensagemDTO> atualizarMensagem(@PathVariable Long id, @Valid @RequestBody MensagemDTO mensagemDTO) {
        Mensagem mensagemAtualizada = mensagemService.atualizarMensagem(id, mensagemDTO);
        return ResponseEntity.ok(mensagemMapper.toDto(mensagemAtualizada));
    }

    /**
     * Exclui mensagem por ID.
     *
     * @param id identificador da mensagem
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir mensagem", description = "Remove uma mensagem pelo ID")
    public void deletarMensagem(@PathVariable Long id) {
        mensagemService.deletarMensagem(id);
    }
}
