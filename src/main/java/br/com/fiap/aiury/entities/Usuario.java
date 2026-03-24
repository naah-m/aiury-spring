package br.com.fiap.aiury.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

/**
 * Entidade que representa a pessoa usuaria da plataforma.
 *
 * Papel no dominio:
 * - concentra dados cadastrais basicos;
 * - referencia a cidade para localizacao;
 * - participa da abertura de chats e envio de mensagens.
 *
 * Observacoes de modelagem:
 * - ha anotacoes comentadas mantendo compatibilidade com convencoes legadas de nome de coluna;
 * - o uso de {@link CreationTimestamp} delega ao Hibernate o preenchimento da data de cadastro.
 */
@Data
@Table(name = "usuario")
@Entity
public class Usuario {

    // -------------------------------------------------------------------------
    // Identificacao
    // -------------------------------------------------------------------------

    /**
     * Identificador tecnico do usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "id_usuario")
    private Long id;

    // -------------------------------------------------------------------------
    // Dados principais
    // -------------------------------------------------------------------------

    /**
     * Nome civil utilizado em cadastros administrativos.
     */
    //@Column(name = "nome_real", nullable = false, length = 100)
    private String nomeReal;

    /**
     * Nome de exibicao para uso em contexto de acolhimento.
     */
    //@Column(name = "nome_anonimo", length = 50)
    private String nomeAnonimo;

    /**
     * Data de nascimento do usuario.
     */
    //@Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    /**
     * Telefone celular informado no cadastro.
     */
    //@Column(name = "telefone_celular", length = 15, unique = true)
    private String celular;

    /**
     * Credencial de autenticacao armazenada para login.
     */
    //@Column(nullable = false)
    private String senha;

    // -------------------------------------------------------------------------
    // Auditoria
    // -------------------------------------------------------------------------

    /**
     * Data de criacao do registro no banco.
     *
     * Preenchimento automatico realizado no momento da persistencia inicial.
     */
    @CreationTimestamp
    //@Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDate dataCadastro;

    // -------------------------------------------------------------------------
    // Relacionamentos
    // -------------------------------------------------------------------------

    /**
     * Cidade associada ao usuario para fins de localizacao.
     */
    @ManyToOne
    //@JoinColumn(name = "id_cidade", nullable = false)
    private Cidade cidade;
}
