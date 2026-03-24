package br.com.fiap.aiury.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de entrada para operacoes de cadastro e atualizacao de usuario.
 *
 * Papel na arquitetura:
 * - define contrato HTTP recebido pelos endpoints de usuario;
 * - concentra validacoes de formato e obrigatoriedade no limite da API.
 *
 * Observacao:
 * - dataNascimento utiliza formato textual DD-MM-AAAA por compatibilidade
 *   com o contrato atual do endpoint.
 */
@Data
public class UsuarioDTO {

    /**
     * Nome civil do usuario.
     */
    @NotBlank(message = "O nome Ã© obrigatÃ³rio")
    private String nomeReal;

    /**
     * Nome de exibicao opcional para contexto de acolhimento.
     */
    private String nomeAnonimo;

    /**
     * Data de nascimento em formato DD-MM-AAAA.
     */
    @NotBlank(message = "A data de nascimento Ã© obrigatÃ³ria")
    @Pattern(regexp = "^\\d{2}-\\d{2}-\\d{4}$", message = "Formato invÃ¡lido para data de nascimento. Use DD-MM-AAAA")
    private String dataNascimento;

    /**
     * Celular de contato do usuario.
     */
    private String celular;

    /**
     * Senha de autenticacao com tamanho minimo definido por validacao.
     */
    @NotBlank(message = "A senha Ã© obrigatÃ³ria")
    @Size(min = 8, message = "A senha deve ter no mÃ­nimo 8 caracteres")
    private String senha;

    /**
     * Referencia da cidade do usuario.
     */
    @NotNull(message = "O ID da cidade Ã© necessÃ¡rio para a localizaÃ§Ã£o")
    private Long cidadeId;
}
