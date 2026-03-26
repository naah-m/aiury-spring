package br.com.fiap.aiury.mappers.web;

import br.com.fiap.aiury.dto.AjudanteRequestDTO;
import br.com.fiap.aiury.dto.web.AjudanteListItemView;
import br.com.fiap.aiury.dto.web.AjudanteWebForm;
import br.com.fiap.aiury.entities.Ajudante;
import org.springframework.stereotype.Component;

@Component
public class AjudanteWebMapper {

    public AjudanteRequestDTO toRequestDto(AjudanteWebForm form) {
        AjudanteRequestDTO dto = new AjudanteRequestDTO();
        dto.setAreaAtuacao(normalizarObrigatorio(form.getAreaAtuacao()));
        dto.setLogin(normalizarLogin(form.getLogin()));
        dto.setSenha(normalizarOpcional(form.getSenha()));
        dto.setMotivacao(normalizarOpcional(form.getMotivacao()));
        dto.setDisponivel(form.getDisponivel());
        dto.setRating(form.getRating());
        return dto;
    }

    public AjudanteListItemView toListItem(Ajudante ajudante) {
        return new AjudanteListItemView(
                ajudante.getId(),
                ajudante.getAreaAtuacao(),
                ajudante.getLogin(),
                ajudante.isDisponivel(),
                ajudante.getRating()
        );
    }

    public AjudanteWebForm toForm(Ajudante ajudante) {
        AjudanteWebForm form = new AjudanteWebForm();
        form.setAreaAtuacao(ajudante.getAreaAtuacao());
        form.setLogin(ajudante.getLogin());
        form.setMotivacao(ajudante.getMotivacao());
        form.setDisponivel(ajudante.isDisponivel());
        form.setRating(ajudante.getRating());
        return form;
    }

    private String normalizarObrigatorio(String valor) {
        return valor == null ? null : valor.trim();
    }

    private String normalizarOpcional(String valor) {
        if (valor == null) {
            return null;
        }
        String valorNormalizado = valor.trim();
        return valorNormalizado.isEmpty() ? null : valorNormalizado;
    }

    private String normalizarLogin(String valor) {
        if (valor == null) {
            return null;
        }
        return valor.trim().toLowerCase();
    }
}
