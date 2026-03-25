package br.com.fiap.aiury.representation;

/**
 * Relacoes HATEOAS padronizadas da API.
 */
public final class ApiRelations {

    private ApiRelations() {
    }

    public static final String ACAO_CRIAR = "criar";
    public static final String ACAO_ATUALIZAR = "atualizar";
    public static final String ACAO_EXCLUIR = "excluir";

    public static final String USUARIOS = "usuarios";
    public static final String AJUDANTES = "ajudantes";
    public static final String CHATS = "chats";
    public static final String MENSAGENS = "mensagens";
    public static final String CIDADES = "cidades";
    public static final String ESTADOS = "estados";

    public static final String USUARIO = "usuario";
    public static final String AJUDANTE = "ajudante";
    public static final String REMETENTE = "remetente";
    public static final String CHAT = "chat";
    public static final String CIDADE = "cidade";
    public static final String ESTADO = "estado";
}
