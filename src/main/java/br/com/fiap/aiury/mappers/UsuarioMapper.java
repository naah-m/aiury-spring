package br.com.fiap.aiury.mappers;

import br.com.fiap.aiury.dto.UsuarioRequestDTO;
import br.com.fiap.aiury.dto.UsuarioResponseDTO;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Usuario;
import org.springframework.stereotype.Component;

/**
 * Mapper responsavel por conversoes entre DTOs de usuario e entidade.
 */
@Component
public class UsuarioMapper {

    /**
     * Converte DTO de usuario em entidade pronta para persistencia.
     *
     * @param request dados de entrada
     * @param cidade cidade resolvida pela camada de servico
     * @return entidade de usuario ou {@code null} quando DTO for nulo
     */
    public Usuario toEntity(UsuarioRequestDTO request, Cidade cidade) {
        if (request == null) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setNomeReal(request.getNomeReal());
        usuario.setNomeAnonimo(request.getNomeAnonimo());
        usuario.setCelular(request.getCelular());
        usuario.setSenha(request.getSenha());
        usuario.setCidade(cidade);
        usuario.setDataNascimento(request.getDataNascimento());

        return usuario;
    }

    /**
     * Atualiza entidade existente somente com campos informados no DTO.
     *
     * @param usuario entidade alvo
     * @param request dados recebidos no endpoint
     * @param novaCidade cidade nova opcional, resolvida no servico
     */
    public void updateEntityFromDto(Usuario usuario, UsuarioRequestDTO request, Cidade novaCidade) {
        if (request.getNomeReal() != null) {
            usuario.setNomeReal(request.getNomeReal());
        }
        if (request.getNomeAnonimo() != null) {
            usuario.setNomeAnonimo(request.getNomeAnonimo());
        }
        if (request.getCelular() != null) {
            usuario.setCelular(request.getCelular());
        }

        if (novaCidade != null) {
            usuario.setCidade(novaCidade);
        }

        if (request.getDataNascimento() != null) {
            usuario.setDataNascimento(request.getDataNascimento());
        }
    }

    /**
     * Converte entidade para DTO de resposta sem dados sensiveis.
     *
     * @param usuario entidade de dominio
     * @return dto de resposta
     */
    public UsuarioResponseDTO toResponseDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNomeReal(usuario.getNomeReal());
        dto.setNomeAnonimo(usuario.getNomeAnonimo());
        dto.setDataNascimento(usuario.getDataNascimento());
        dto.setCelular(usuario.getCelular());
        dto.setDataCadastro(usuario.getDataCadastro());
        dto.setCidadeId(usuario.getCidade() != null ? usuario.getCidade().getId() : null);

        return dto;
    }
}
