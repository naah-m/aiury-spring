package br.com.fiap.aiury.mappers.web;

import br.com.fiap.aiury.dto.UsuarioRequestDTO;
import br.com.fiap.aiury.dto.web.UsuarioListItemView;
import br.com.fiap.aiury.dto.web.UsuarioWebForm;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Estado;
import br.com.fiap.aiury.entities.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioWebMapper {

    public UsuarioRequestDTO toRequestDto(UsuarioWebForm form) {
        UsuarioRequestDTO dto = new UsuarioRequestDTO();
        dto.setNomeReal(normalizarTextoObrigatorio(form.getNomeReal()));
        dto.setNomeAnonimo(normalizarTextoOpcional(form.getNomeAnonimo()));
        dto.setDataNascimento(form.getDataNascimento());
        dto.setCelular(normalizarTextoOpcional(form.getCelular()));
        dto.setSenha(form.getSenha());
        dto.setCidadeId(form.getCidadeId());
        return dto;
    }

    public UsuarioListItemView toListItem(Usuario usuario) {
        Cidade cidade = usuario.getCidade();
        Estado estado = cidade != null ? cidade.getEstado() : null;

        return new UsuarioListItemView(
                usuario.getId(),
                usuario.getNomeReal(),
                usuario.getNomeAnonimo(),
                cidade != null ? cidade.getNomeCidade() : "-",
                estado != null ? estado.getUf() : "-",
                usuario.getDataCadastro()
        );
    }

    public UsuarioWebForm toForm(Usuario usuario) {
        UsuarioWebForm form = new UsuarioWebForm();
        form.setNomeReal(usuario.getNomeReal());
        form.setNomeAnonimo(usuario.getNomeAnonimo());
        form.setDataNascimento(usuario.getDataNascimento());
        form.setCelular(usuario.getCelular());
        form.setCidadeId(usuario.getCidade() != null ? usuario.getCidade().getId() : null);
        return form;
    }

    private String normalizarTextoObrigatorio(String valor) {
        return valor == null ? null : valor.trim();
    }

    private String normalizarTextoOpcional(String valor) {
        if (valor == null) {
            return null;
        }

        String valorNormalizado = valor.trim();
        return valorNormalizado.isEmpty() ? null : valorNormalizado;
    }
}
