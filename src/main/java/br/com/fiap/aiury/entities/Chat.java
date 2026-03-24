package br.com.fiap.aiury.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_ajudante")
    private Ajudante ajudante;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim; // Nullable conforme o modelo conceitual (data_fim)

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ChatStatus status;

    @OneToMany(mappedBy = "chat")
    private List<Mensagem> mensagens;

}
