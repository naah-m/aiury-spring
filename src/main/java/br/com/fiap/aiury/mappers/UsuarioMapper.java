package br.com.fiap.aiury.mappers;

import br.com.fiap.aiury.dto.UsuarioDTO;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class UsuarioMapper {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Usuario toEntity(UsuarioDTO usuarioDTO, Cidade cidade) {
        if (usuarioDTO == null) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setNomeReal(usuarioDTO.getNomeReal());
        usuario.setNomeAnonimo(usuarioDTO.getNomeAnonimo());
        usuario.setCelular(usuarioDTO.getCelular());
        usuario.setSenha(usuarioDTO.getSenha());
        usuario.setCidade(cidade);
        usuario.setDataNascimento(LocalDate.parse(usuarioDTO.getDataNascimento(), formatter));

        return usuario;
    }

    public void updateEntityFromDto(Usuario usuario, UsuarioDTO usuarioDTO, Cidade novaCidade) {
        if (usuarioDTO.getNomeReal() != null) {
            usuario.setNomeReal(usuarioDTO.getNomeReal());
        }
        if (usuarioDTO.getNomeAnonimo() != null) {
            usuario.setNomeAnonimo(usuarioDTO.getNomeAnonimo());
        }
        if (usuarioDTO.getCelular() != null) {
            usuario.setCelular(usuarioDTO.getCelular());
        }

        if (novaCidade != null) {
            usuario.setCidade(novaCidade);
        }

        if (usuarioDTO.getDataNascimento() != null) {
            usuario.setDataNascimento(LocalDate.parse(usuarioDTO.getDataNascimento(), formatter));
        }

    }
}

