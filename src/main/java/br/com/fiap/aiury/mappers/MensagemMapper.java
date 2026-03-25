package br.com.fiap.aiury.mappers;

import br.com.fiap.aiury.dto.MensagemRequestDTO;
import br.com.fiap.aiury.dto.MensagemResponseDTO;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.Mensagem;
import br.com.fiap.aiury.entities.Usuario;
import org.springframework.stereotype.Component;

/**
 * Mapper de conversao entre entidade {@link Mensagem} e DTOs de mensagem.
 *
 * Papel:
 * - converter payload de entrada para entidade persistivel;
 * - transformar entidade de resposta em DTO enxuto.
 */
@Component
public class MensagemMapper {

    /**
     * Converte DTO em entidade de mensagem.
     *
     * @param request dados da mensagem
     * @param chat chat resolvido no servico
     * @param remetente usuario remetente resolvido no servico
     * @return entidade nova de mensagem
     */
    public Mensagem toEntity(MensagemRequestDTO request, Chat chat, Usuario remetente) {
        if (request == null) {
            return null;
        }

        Mensagem mensagem = new Mensagem();
        mensagem.setChat(chat);
        mensagem.setRemetente(remetente);
        mensagem.setTexto(request.getTexto());
        mensagem.setDataEnvio(request.getDataEnvio());

        return mensagem;
    }

    /**
     * Atualiza entidade de mensagem existente com os dados recebidos.
     *
     * @param mensagem entidade alvo
     * @param request dados novos de atualizacao
     * @param chat referencia de chat resolvida
     * @param remetente referencia de usuario remetente resolvida
     */
    public void updateEntityFromDto(Mensagem mensagem, MensagemRequestDTO request, Chat chat, Usuario remetente) {
        if (chat != null) {
            mensagem.setChat(chat);
        }
        if (remetente != null) {
            mensagem.setRemetente(remetente);
        }
        if (request.getTexto() != null) {
            mensagem.setTexto(request.getTexto());
        }
        if (request.getDataEnvio() != null) {
            mensagem.setDataEnvio(request.getDataEnvio());
        }
    }

    /**
     * Converte entidade para DTO de resposta.
     *
     * @param mensagem entidade de dominio
     * @return DTO serializavel para a API
     */
    public MensagemResponseDTO toResponseDto(Mensagem mensagem) {
        if (mensagem == null) {
            return null;
        }

        MensagemResponseDTO response = new MensagemResponseDTO();
        response.setId(mensagem.getId());
        response.setChatId(mensagem.getChat() != null ? mensagem.getChat().getId() : null);
        response.setRemetenteId(mensagem.getRemetente() != null ? mensagem.getRemetente().getId() : null);
        response.setTexto(mensagem.getTexto());
        response.setDataEnvio(mensagem.getDataEnvio());

        return response;
    }
}
