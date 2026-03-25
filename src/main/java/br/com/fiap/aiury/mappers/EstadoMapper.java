package br.com.fiap.aiury.mappers;

import br.com.fiap.aiury.dto.EstadoRequestDTO;
import br.com.fiap.aiury.dto.EstadoResponseDTO;
import br.com.fiap.aiury.entities.Estado;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Mapper para conversoes entre DTOs e entidade {@link Estado}.
 */
@Component
public class EstadoMapper {

    public Estado toEntity(EstadoRequestDTO request) {
        if (request == null) {
            return null;
        }

        Estado estado = new Estado();
        estado.setNomeEstado(normalizarNome(request.getNomeEstado()));
        estado.setUf(normalizarUf(request.getUf()));
        return estado;
    }

    public void updateEntityFromDto(Estado estado, EstadoRequestDTO request) {
        estado.setNomeEstado(normalizarNome(request.getNomeEstado()));
        estado.setUf(normalizarUf(request.getUf()));
    }

    public EstadoResponseDTO toResponseDto(Estado estado) {
        if (estado == null) {
            return null;
        }

        EstadoResponseDTO dto = new EstadoResponseDTO();
        dto.setId(estado.getId());
        dto.setNomeEstado(estado.getNomeEstado());
        dto.setUf(estado.getUf());
        return dto;
    }

    private String normalizarUf(String uf) {
        return uf == null ? null : uf.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizarNome(String nome) {
        return nome == null ? null : nome.trim();
    }
}
