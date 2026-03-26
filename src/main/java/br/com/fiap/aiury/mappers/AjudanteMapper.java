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

    public Ajudante toEntity(AjudanteRequestDTO request) {
        if (request == null) {
            return null;
        }

        Ajudante ajudante = new Ajudante();
        ajudante.setAreaAtuacao(request.getAreaAtuacao());
        ajudante.setLogin(request.getLogin());
        ajudante.setSenha(request.getSenha());
        ajudante.setMotivacao(request.getMotivacao());
        ajudante.setDisponivel(Boolean.TRUE.equals(request.getDisponivel()));
        ajudante.setRating(request.getRating());
        return ajudante;
    }

    public void updateEntityFromDto(Ajudante ajudante, AjudanteRequestDTO request) {
        if (request.getAreaAtuacao() != null) {
            ajudante.setAreaAtuacao(request.getAreaAtuacao());
        }
        if (request.getLogin() != null) {
            ajudante.setLogin(request.getLogin());
        }
        if (request.getSenha() != null) {
            ajudante.setSenha(request.getSenha());
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

    public AjudanteResponseDTO toResponseDto(Ajudante ajudante) {
        if (ajudante == null) {
            return null;
        }

        AjudanteResponseDTO dto = new AjudanteResponseDTO();
        dto.setId(ajudante.getId());
        dto.setAreaAtuacao(ajudante.getAreaAtuacao());
        dto.setLogin(ajudante.getLogin());
        dto.setMotivacao(ajudante.getMotivacao());
        dto.setDisponivel(ajudante.isDisponivel());
        dto.setRating(ajudante.getRating());
        return dto;
    }
}
