package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.MensagemDTO;
import br.com.fiap.aiury.entities.Mensagem;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Contrato de servico para operacoes de mensagens.
 */
public interface MensagemService {

    /**
     * Cria nova mensagem.
     *
     * @param mensagemDTO dados de entrada
     * @return mensagem persistida
     */
    Mensagem criarMensagem(@Valid MensagemDTO mensagemDTO);

    /**
     * Busca mensagem por ID.
     *
     * @param id identificador da mensagem
     * @return mensagem encontrada
     */
    Mensagem buscarPorId(Long id);

    /**
     * Lista todas as mensagens.
     *
     * @return colecao de mensagens
     */
    List<Mensagem> buscarTodos();

    /**
     * Atualiza mensagem existente.
     *
     * @param id identificador do registro alvo
     * @param mensagemDTO novos dados da mensagem
     * @return mensagem atualizada
     */
    Mensagem atualizarMensagem(Long id, @Valid MensagemDTO mensagemDTO);

    /**
     * Remove mensagem por identificador.
     *
     * @param id identificador da mensagem
     */
    void deletarMensagem(Long id);
}
