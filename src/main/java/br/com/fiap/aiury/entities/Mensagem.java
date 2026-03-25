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
@Table(name = "tb_mensagem")
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mensagem")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_chat", nullable = false)
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_remetente", nullable = false)
    private Usuario remetente;

    @Column(name = "texto", nullable = false, length = 1000)
    private String texto;

    @Column(name = "data_envio", nullable = false)
    private LocalDateTime dataEnvio;
}
