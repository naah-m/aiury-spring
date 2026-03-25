package br.com.fiap.aiury.repositories;

import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.ChatStatus;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Estado;
import br.com.fiap.aiury.entities.Mensagem;
import br.com.fiap.aiury.entities.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class MensagemRepositoryTest {

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AjudanteRepository ajudanteRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Test
    void deveFiltrarMensagensPorChatERemetenteComOrdenacao() {
        Estado estado = new Estado();
        estado.setNomeEstado("Sao Paulo");
        estado.setUf("SP");
        estado = estadoRepository.save(estado);

        Cidade cidade = new Cidade();
        cidade.setNomeCidade("Sao Paulo");
        cidade.setEstado(estado);
        cidade = cidadeRepository.save(cidade);

        Usuario usuario1 = new Usuario();
        usuario1.setNomeReal("Maria Silva");
        usuario1.setDataNascimento(LocalDate.of(1998, 8, 15));
        usuario1.setCelular("11999998888");
        usuario1.setSenha("segredo123");
        usuario1.setCidade(cidade);
        usuario1 = usuarioRepository.save(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setNomeReal("Joao Souza");
        usuario2.setDataNascimento(LocalDate.of(1996, 5, 10));
        usuario2.setCelular("11988887777");
        usuario2.setSenha("segredo123");
        usuario2.setCidade(cidade);
        usuario2 = usuarioRepository.save(usuario2);

        Ajudante ajudante = new Ajudante();
        ajudante.setAreaAtuacao("Escuta ativa");
        ajudante.setMotivacao("Voluntario");
        ajudante.setDisponivel(true);
        ajudante.setRating(4.6);
        ajudante = ajudanteRepository.save(ajudante);

        Chat chat = new Chat();
        chat.setUsuario(usuario1);
        chat.setAjudante(ajudante);
        chat.setDataInicio(LocalDateTime.of(2026, 3, 24, 14, 0));
        chat.setStatus(ChatStatus.EM_ANDAMENTO);
        chat = chatRepository.save(chat);

        Mensagem mensagem1 = new Mensagem();
        mensagem1.setChat(chat);
        mensagem1.setRemetente(usuario1);
        mensagem1.setTexto("Primeira mensagem");
        mensagem1.setDataEnvio(LocalDateTime.of(2026, 3, 24, 14, 5));

        Mensagem mensagem2 = new Mensagem();
        mensagem2.setChat(chat);
        mensagem2.setRemetente(usuario2);
        mensagem2.setTexto("Resposta");
        mensagem2.setDataEnvio(LocalDateTime.of(2026, 3, 24, 14, 6));

        mensagemRepository.saveAll(List.of(mensagem2, mensagem1));

        List<Mensagem> porChat = mensagemRepository.findByChat_IdOrderByDataEnvioAsc(chat.getId());
        List<Mensagem> porChatERemetente = mensagemRepository.findByChat_IdAndRemetente_IdOrderByDataEnvioAsc(chat.getId(), usuario1.getId());

        assertThat(porChat).hasSize(2);
        assertThat(porChat.getFirst().getTexto()).isEqualTo("Primeira mensagem");
        assertThat(porChatERemetente).hasSize(1);
        assertThat(porChatERemetente.getFirst().getRemetente().getId()).isEqualTo(usuario1.getId());
    }
}
