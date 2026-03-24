package br.com.fiap.aiury.mappers;

import br.com.fiap.aiury.dto.MensagemDTO;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.Mensagem;
import br.com.fiap.aiury.entities.Usuario;
import org.springframework.stereotype.Component;

/**
 * Mapper de conversao entre {@link Mensagem} e {@link MensagemDTO}.
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
     * @param mensagemDTO dados da mensagem
     * @param chat chat resolvido no servico
     * @param remetente usuario remetente resolvido no servico
     * @return entidade nova de mensagem
     */
    public Mensagem toEntity(MensagemDTO mensagemDTO, Chat chat, Usuario remetente) {
        if (mensagemDTO == null) {
            return null;
        }

        Mensagem mensagem = new Mensagem();
        mensagem.setChat(chat);
        mensagem.setRemetente(remetente);
        mensagem.setTexto(mensagemDTO.getTexto());
        mensagem.setDataEnvio(mensagemDTO.getDataEnvio());

        return mensagem;
    }

    /**
     * Atualiza entidade de mensagem existente com os dados recebidos.
     *
     * @param mensagem entidade alvo
     * @param mensagemDTO dados novos de atualizacao
     * @param chat referencia de chat resolvida
     * @param remetente referencia de usuario remetente resolvida
     */
    public void updateEntityFromDto(Mensagem mensagem, MensagemDTO mensagemDTO, Chat chat, Usuario remetente) {
        if (chat != null) {
            mensagem.setChat(chat);
        }
        if (remetente != null) {
            mensagem.setRemetente(remetente);
        }
        if (mensagemDTO.getTexto() != null) {
            mensagem.setTexto(mensagemDTO.getTexto());
        }
        if (mensagemDTO.getDataEnvio() != null) {
            mensagem.setDataEnvio(mensagemDTO.getDataEnvio());
        }
    }

    /**
     * Converte entidade para DTO de resposta.
     *
     * @param mensagem entidade de dominio
     * @return DTO serializavel para a API
     */
    public MensagemDTO toDto(Mensagem mensagem) {
        if (mensagem == null) {
            return null;
        }

        MensagemDTO mensagemDTO = new MensagemDTO();
        mensagemDTO.setId(mensagem.getId());
        mensagemDTO.setChatId(mensagem.getChat() != null ? mensagem.getChat().getId() : null);
        mensagemDTO.setRemetenteId(mensagem.getRemetente() != null ? mensagem.getRemetente().getId() : null);
        mensagemDTO.setTexto(mensagem.getTexto());
        mensagemDTO.setDataEnvio(mensagem.getDataEnvio());

        return mensagemDTO;
    }
}
