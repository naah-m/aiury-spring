package br.com.fiap.aiury.mappers;

import br.com.fiap.aiury.dto.AjudanteRequestDTO;
import br.com.fiap.aiury.dto.AjudanteResponseDTO;
import br.com.fiap.aiury.entities.Ajudante;
import org.springframework.stereotype.Component;

/**
 * Mapper dedicado a conversoes entre DTOs de ajudante e entidade.
 */
@Component
public class AjudanteMapper {

    /**
     * Cria entidade de ajudante a partir do DTO.
     *
     * @param request dados de entrada
     * @return entidade preenchida ou {@code null} se DTO nulo
     */
    public Ajudante toEntity(AjudanteRequestDTO request) {
        if (request == null) {
            return null;
        }

        Ajudante ajudante = new Ajudante();
        ajudante.setAreaAtuacao(request.getAreaAtuacao());
        ajudante.setMotivacao(request.getMotivacao());
        ajudante.setDisponivel(Boolean.TRUE.equals(request.getDisponivel()));
        ajudante.setRating(request.getRating());

        return ajudante;
    }

    /**
     * Atualiza entidade existente com os campos informados no DTO.
     *
     * @param ajudante entidade alvo
     * @param request novos dados de atualizacao
     */
    public void updateEntityFromDto(Ajudante ajudante, AjudanteRequestDTO request) {
        if (request.getAreaAtuacao() != null) {
            ajudante.setAreaAtuacao(request.getAreaAtuacao());
        }
        if (request.getMotivacao() != null) {
            ajudante.setMotivacao(request.getMotivacao());
        }
        if (request.getDisponivel() != null) {
            ajudante.setDisponivel(request.getDisponivel());
        }
        if (request.getRating() != null) {
            ajudante.setRating(request.getRating());
        }
    }

    /**
     * Converte entidade para DTO de resposta.
     *
     * @param ajudante entidade de dominio
     * @return representacao serializavel para a API
     */
    public AjudanteResponseDTO toResponseDto(Ajudante ajudante) {
        if (ajudante == null) {
            return null;
        }

        AjudanteResponseDTO dto = new AjudanteResponseDTO();
        dto.setId(ajudante.getId());
        dto.setAreaAtuacao(ajudante.getAreaAtuacao());
        dto.setMotivacao(ajudante.getMotivacao());
        dto.setDisponivel(ajudante.isDisponivel());
        dto.setRating(ajudante.getRating());

        return dto;
    }
}
