package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.MensagemDTO;
import br.com.fiap.aiury.entities.Mensagem;
import jakarta.validation.Valid;

import java.util.List;

public interface MensagemService {

    Mensagem criarMensagem(@Valid MensagemDTO mensagemDTO);

    Mensagem buscarPorId(Long id);

    List<Mensagem> buscarTodos();

    Mensagem atualizarMensagem(Long id, @Valid MensagemDTO mensagemDTO);

    void deletarMensagem(Long id);
}
