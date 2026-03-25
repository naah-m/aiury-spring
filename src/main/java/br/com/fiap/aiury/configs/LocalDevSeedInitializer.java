package br.com.fiap.aiury.configs;

import br.com.fiap.aiury.entities.Ajudante;
import br.com.fiap.aiury.entities.Chat;
import br.com.fiap.aiury.entities.ChatStatus;
import br.com.fiap.aiury.entities.Cidade;
import br.com.fiap.aiury.entities.Estado;
import br.com.fiap.aiury.entities.Mensagem;
import br.com.fiap.aiury.entities.Usuario;
import br.com.fiap.aiury.repositories.AjudanteRepository;
import br.com.fiap.aiury.repositories.ChatRepository;
import br.com.fiap.aiury.repositories.CidadeRepository;
import br.com.fiap.aiury.repositories.EstadoRepository;
import br.com.fiap.aiury.repositories.MensagemRepository;
import br.com.fiap.aiury.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Carga inicial minima para demonstracao local no profile dev.
 *
 * Caracteristicas:
 * - controlada por propriedade (aiury.seed.enabled);
 * - idempotente, reutilizando dados existentes para evitar duplicidade;
 * - preparada para deixar dashboard e listagens com dados logo ao subir a aplicacao.
 */
@Component
@Profile("dev")
@ConditionalOnProperty(prefix = "aiury.seed", name = "enabled", havingValue = "true", matchIfMissing = true)
public class LocalDevSeedInitializer implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalDevSeedInitializer.class);

    private static final String SENHA_PADRAO_USUARIOS = "demo12345";

    private static final LocalDateTime CHAT_ATIVO_INICIO = LocalDateTime.of(2026, 3, 25, 14, 0);
    private static final LocalDateTime CHAT_FINALIZADO_INICIO = LocalDateTime.of(2026, 3, 24, 19, 0);
    private static final LocalDateTime CHAT_FINALIZADO_FIM = LocalDateTime.of(2026, 3, 24, 19, 45);

    private final EstadoRepository estadoRepository;
    private final CidadeRepository cidadeRepository;
    private final UsuarioRepository usuarioRepository;
    private final AjudanteRepository ajudanteRepository;
    private final ChatRepository chatRepository;
    private final MensagemRepository mensagemRepository;

    public LocalDevSeedInitializer(EstadoRepository estadoRepository,
                                   CidadeRepository cidadeRepository,
                                   UsuarioRepository usuarioRepository,
                                   AjudanteRepository ajudanteRepository,
                                   ChatRepository chatRepository,
                                   MensagemRepository mensagemRepository) {
        this.estadoRepository = estadoRepository;
        this.cidadeRepository = cidadeRepository;
        this.usuarioRepository = usuarioRepository;
        this.ajudanteRepository = ajudanteRepository;
        this.chatRepository = chatRepository;
        this.mensagemRepository = mensagemRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Estado sp = upsertEstado("Sao Paulo", "SP");
        Estado rj = upsertEstado("Rio de Janeiro", "RJ");

        Cidade saoPaulo = upsertCidade("Sao Paulo", sp);
        Cidade campinas = upsertCidade("Campinas", sp);
        upsertCidade("Rio de Janeiro", rj);

        Usuario camila = upsertUsuario(
                "Camila Nunes",
                "AuroraCalma",
                LocalDate.of(1998, 8, 15),
                "11999998888",
                SENHA_PADRAO_USUARIOS,
                saoPaulo
        );

        Usuario diego = upsertUsuario(
                "Diego Pereira",
                "RitmoSereno",
                LocalDate.of(1994, 2, 11),
                "11988887777",
                SENHA_PADRAO_USUARIOS,
                campinas
        );

        Ajudante escutaAtiva = upsertAjudante(
                "Escuta ativa",
                "Acolhimento inicial e escuta qualificada para momentos de vulnerabilidade.",
                true,
                4.9
        );

        Ajudante plantaoEmocional = upsertAjudante(
                "Plantao emocional",
                "Atendimento em janelas agendadas para suporte pontual.",
                false,
                4.6
        );

        Chat chatAtivo = upsertChat(
                camila,
                escutaAtiva,
                CHAT_ATIVO_INICIO,
                null,
                ChatStatus.EM_ANDAMENTO
        );

        Chat chatFinalizado = upsertChat(
                diego,
                plantaoEmocional,
                CHAT_FINALIZADO_INICIO,
                CHAT_FINALIZADO_FIM,
                ChatStatus.FINALIZADO_USUARIO
        );

        upsertMensagem(chatAtivo, camila, "Oi, eu precisava conversar com alguem agora.", CHAT_ATIVO_INICIO.plusMinutes(3));
        upsertMensagem(chatAtivo, camila, "Obrigado por me ouvir, ja me sinto mais calmo.", CHAT_ATIVO_INICIO.plusMinutes(8));
        upsertMensagem(chatFinalizado, diego, "Consegui organizar meus pensamentos apos o atendimento.", CHAT_FINALIZADO_INICIO.plusMinutes(12));

        LOGGER.info("Seed local aplicada (dev): 2 estados, 3 cidades, 2 usuarios, 2 ajudantes, 2 chats, 3 mensagens.");
    }

    private Estado upsertEstado(String nomeEstado, String uf) {
        Estado estado = estadoRepository.findByUfIgnoreCase(uf).orElseGet(Estado::new);
        estado.setNomeEstado(nomeEstado);
        estado.setUf(uf.toUpperCase(Locale.ROOT));
        return estadoRepository.save(estado);
    }

    private Cidade upsertCidade(String nomeCidade, Estado estado) {
        Cidade cidade = cidadeRepository
                .findByNomeCidadeIgnoreCaseAndEstado_Id(nomeCidade, estado.getId())
                .orElseGet(Cidade::new);
        cidade.setNomeCidade(nomeCidade);
        cidade.setEstado(estado);
        return cidadeRepository.save(cidade);
    }

    private Usuario upsertUsuario(String nomeReal,
                                  String nomeAnonimo,
                                  LocalDate dataNascimento,
                                  String celular,
                                  String senha,
                                  Cidade cidade) {
        Usuario usuario = usuarioRepository.findByCelular(celular).orElseGet(Usuario::new);
        usuario.setNomeReal(nomeReal);
        usuario.setNomeAnonimo(nomeAnonimo);
        usuario.setDataNascimento(dataNascimento);
        usuario.setCelular(celular);
        usuario.setSenha(senha);
        usuario.setCidade(cidade);
        return usuarioRepository.save(usuario);
    }

    private Ajudante upsertAjudante(String areaAtuacao, String motivacao, boolean disponivel, double rating) {
        Ajudante ajudante = ajudanteRepository.findFirstByAreaAtuacaoIgnoreCaseOrderByIdAsc(areaAtuacao)
                .orElseGet(Ajudante::new);
        ajudante.setAreaAtuacao(areaAtuacao);
        ajudante.setMotivacao(motivacao);
        ajudante.setDisponivel(disponivel);
        ajudante.setRating(rating);
        return ajudanteRepository.save(ajudante);
    }

    private Chat upsertChat(Usuario usuario,
                            Ajudante ajudante,
                            LocalDateTime dataInicio,
                            LocalDateTime dataFim,
                            ChatStatus status) {
        Chat chat = chatRepository
                .findFirstByUsuario_IdAndAjudante_IdAndDataInicioOrderByIdAsc(usuario.getId(), ajudante.getId(), dataInicio)
                .orElseGet(Chat::new);
        chat.setUsuario(usuario);
        chat.setAjudante(ajudante);
        chat.setDataInicio(dataInicio);
        chat.setDataFim(dataFim);
        chat.setStatus(status);
        return chatRepository.save(chat);
    }

    private Mensagem upsertMensagem(Chat chat, Usuario remetente, String texto, LocalDateTime dataEnvio) {
        Mensagem mensagem = mensagemRepository
                .findFirstByChat_IdAndRemetente_IdAndDataEnvioAndTextoOrderByIdAsc(
                        chat.getId(),
                        remetente.getId(),
                        dataEnvio,
                        texto
                )
                .orElseGet(Mensagem::new);
        mensagem.setChat(chat);
        mensagem.setRemetente(remetente);
        mensagem.setTexto(texto);
        mensagem.setDataEnvio(dataEnvio);
        return mensagemRepository.save(mensagem);
    }
}
