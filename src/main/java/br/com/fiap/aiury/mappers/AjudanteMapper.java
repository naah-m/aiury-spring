package br.com.fiap.aiury.mappers;

import br.com.fiap.aiury.dto.AjudanteDTO;
import br.com.fiap.aiury.dto.AjudanteResponseDTO;
import br.com.fiap.aiury.entities.Ajudante;
import org.springframework.stereotype.Component;

/**
 * Mapper dedicado a conversoes entre {@link AjudanteDTO} e {@link Ajudante}.
 */
@Component
public class AjudanteMapper {

    /**
     * Cria entidade de ajudante a partir do DTO.
     *
     * @param ajudanteDTO dados de entrada
     * @return entidade preenchida ou {@code null} se DTO nulo
     */
    public Ajudante toEntity(AjudanteDTO ajudanteDTO) {
        if (ajudanteDTO == null) {
            return null;
        }

        Ajudante ajudante = new Ajudante();
        ajudante.setAreaAtuacao(ajudanteDTO.getAreaAtuacao());
        ajudante.setMotivacao(ajudanteDTO.getMotivacao());
        ajudante.setDisponivel(Boolean.TRUE.equals(ajudanteDTO.getDisponivel()));
        ajudante.setRating(ajudanteDTO.getRating());

        return ajudante;
    }

    /**
     * Atualiza entidade existente com os campos informados no DTO.
     *
     * @param ajudante entidade alvo
     * @param ajudanteDTO novos dados de atualizacao
     */
    public void updateEntityFromDto(Ajudante ajudante, AjudanteDTO ajudanteDTO) {
        if (ajudanteDTO.getAreaAtuacao() != null) {
            ajudante.setAreaAtuacao(ajudanteDTO.getAreaAtuacao());
        }
        if (ajudanteDTO.getMotivacao() != null) {
            ajudante.setMotivacao(ajudanteDTO.getMotivacao());
        }
        if (ajudanteDTO.getDisponivel() != null) {
            ajudante.setDisponivel(ajudanteDTO.getDisponivel());
        }
        if (ajudanteDTO.getRating() != null) {
            ajudante.setRating(ajudanteDTO.getRating());
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
