package br.com.fiap.aiury.mappers;

import br.com.fiap.aiury.dto.UsuarioDTO;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Mapper responsavel por conversoes entre {@link UsuarioDTO} e {@link Usuario}.
 *
 * Observacoes:
 * - converte data textual no padrao DD-MM-AAAA para {@link LocalDate};
 * - suporta atualizacao parcial da entidade para preservar campos nao enviados.
 */
@Component
public class UsuarioMapper {

    /**
     * Formatter padrao do contrato atual de entrada da API.
     */
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Converte DTO de usuario em entidade pronta para persistencia.
     *
     * @param usuarioDTO dados de entrada
     * @param cidade cidade resolvida pela camada de servico
     * @return entidade de usuario ou {@code null} quando DTO for nulo
     */
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

    /**
     * Atualiza entidade existente somente com campos informados no DTO.
     *
     * @param usuario entidade alvo
     * @param usuarioDTO dados recebidos no endpoint
     * @param novaCidade cidade nova opcional, resolvida no servico
     */
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
