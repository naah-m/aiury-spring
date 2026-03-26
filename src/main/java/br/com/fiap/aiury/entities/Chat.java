package br.com.fiap.aiury.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa uma sessao de conversa entre usuario e ajudante.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TB_CHAT")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CHAT")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ID_USUARIO",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_TB_CHAT_ID_USUARIO")
    )
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ID_AJUDANTE",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_TB_CHAT_ID_AJUDANTE")
    )
    private Ajudante ajudante;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mensagem> mensagens = new ArrayList<>();

    @Column(name = "DH_INICIO", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "DH_FIM")
    private LocalDateTime dataFim;

    @Enumerated(EnumType.STRING)
    @Column(name = "ST_CHAT", nullable = false, length = 40)
    private ChatStatus status;
}
