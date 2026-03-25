package br.com.fiap.aiury.services;

import br.com.fiap.aiury.dto.MensagemRequestDTO;
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
    Mensagem criarMensagem(@Valid MensagemRequestDTO mensagemDTO);

    /**
     * Busca mensagem por ID.
     *
     * @param id identificador da mensagem
     * @return mensagem encontrada
     */
    Mensagem buscarPorId(Long id);

    /**
     * Lista mensagens com filtros opcionais.
     *
     * @param chatId filtro opcional por chat
     * @param remetenteId filtro opcional por remetente
     * @return colecao de mensagens
     */
    List<Mensagem> buscarTodos(Long chatId, Long remetenteId);

    /**
     * Atualiza mensagem existente.
     *
     * @param id identificador do registro alvo
     * @param mensagemDTO novos dados da mensagem
     * @return mensagem atualizada
     */
    Mensagem atualizarMensagem(Long id, @Valid MensagemRequestDTO mensagemDTO);

    /**
     * Remove mensagem por identificador.
     *
     * @param id identificador da mensagem
     */
    void deletarMensagem(Long id);
}
