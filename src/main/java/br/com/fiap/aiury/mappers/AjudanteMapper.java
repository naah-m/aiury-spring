package br.com.fiap.aiury.mappers;

import br.com.fiap.aiury.dto.AjudanteDTO;
import br.com.fiap.aiury.entities.Ajudante;
import org.springframework.stereotype.Component;

@Component
public class AjudanteMapper {

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
}
