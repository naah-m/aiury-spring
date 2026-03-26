package br.com.fiap.aiury.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entidade que representa uma mensagem textual dentro de um chat.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TB_MENSAGEM")
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MENSAGEM")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ID_CHAT",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_TB_MENSAGEM_ID_CHAT")
    )
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ID_USUARIO_REMETENTE",
            foreignKey = @ForeignKey(name = "FK_TB_MSG_ID_USR_REM")
    )
    private Usuario remetente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ID_AJUDANTE_REMETENTE",
            foreignKey = @ForeignKey(name = "FK_TB_MSG_ID_AJD_REM")
    )
    private Ajudante remetenteAjudante;

    @Column(name = "TX_MENSAGEM", nullable = false, length = 1000)
    private String texto;

    @Column(name = "DH_ENVIO", nullable = false)
    private LocalDateTime dataEnvio;
}
