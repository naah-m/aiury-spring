package br.com.fiap.aiury.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
//@Table(name = "chat")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "id_chat")
    private Long id;

    @ManyToOne
    //@JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    //@JoinColumn(name = "id_ajudante")
    private Ajudante ajudante;

    @Temporal(TemporalType.TIMESTAMP)
    //@Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Temporal(TemporalType.TIMESTAMP)
    //@Column(name = "data_fim")
    private LocalDateTime dataFim; // Nullable conforme o modelo conceitual (data_fim)

    @Enumerated(EnumType.STRING)
    //@Column(name = "status", nullable = false)
    private ChatStatus status;

    @OneToMany(mappedBy = "chat")
    private List<Mensagem> mensagens;

}
