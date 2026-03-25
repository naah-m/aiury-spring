package br.com.fiap.aiury.mappers;

import br.com.fiap.aiury.dto.CidadeRequestDTO;
import br.com.fiap.aiury.dto.CidadeResponseDTO;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Estado;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversoes entre DTOs e entidade {@link Cidade}.
 */
@Component
public class CidadeMapper {

    public Cidade toEntity(CidadeRequestDTO request, Estado estado) {
        if (request == null) {
            return null;
        }

        Cidade cidade = new Cidade();
        cidade.setNomeCidade(normalizarNome(request.getNomeCidade()));
        cidade.setEstado(estado);
        return cidade;
    }

    public void updateEntityFromDto(Cidade cidade, CidadeRequestDTO request, Estado estado) {
        cidade.setNomeCidade(normalizarNome(request.getNomeCidade()));
        cidade.setEstado(estado);
    }

    public CidadeResponseDTO toResponseDto(Cidade cidade) {
        if (cidade == null) {
            return null;
        }

        CidadeResponseDTO dto = new CidadeResponseDTO();
        dto.setId(cidade.getId());
        dto.setNomeCidade(cidade.getNomeCidade());

        Estado estado = cidade.getEstado();
        if (estado != null) {
            dto.setEstadoId(estado.getId());
            dto.setEstadoNome(estado.getNomeEstado());
            dto.setEstadoUf(estado.getUf());
        }

        return dto;
    }

    private String normalizarNome(String nome) {
        return nome == null ? null : nome.trim();
    }
}
