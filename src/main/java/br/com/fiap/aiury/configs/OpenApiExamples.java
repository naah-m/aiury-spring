package br.com.fiap.aiury.configs;

/**
 * Exemplos reutilizaveis para documentacao Swagger/OpenAPI.
 */
public final class OpenApiExamples {

    private OpenApiExamples() {
    }

    public static final String USUARIO_REQUEST = """
            {
              "nomeReal": "Maria Silva",
              "nomeAnonimo": "LuzInterior",
              "dataNascimento": "15/08/1998",
              "celular": "11999998888",
              "senha": "segredo123",
              "cidadeId": 1
            }
            """;

    public static final String ESTADO_REQUEST = """
            {
              "nomeEstado": "Sao Paulo",
              "uf": "SP"
            }
            """;

    public static final String CIDADE_REQUEST = """
            {
              "nomeCidade": "Sao Paulo",
              "estadoId": 1
            }
            """;

    public static final String AJUDANTE_REQUEST = """
            {
              "areaAtuacao": "Escuta ativa",
              "login": "ajudante.escuta",
              "senha": "apoio12345",
              "motivacao": "Acolhimento voluntario em plantao",
              "disponivel": true,
              "rating": 4.8
            }
            """;

    public static final String CHAT_REQUEST = """
            {
              "usuarioId": 1,
              "ajudanteId": 1,
              "dataInicio": "25/03/2026 14:00:00",
              "dataFim": null,
              "status": "INICIADO"
            }
            """;

    public static final String MENSAGEM_REQUEST = """
            {
              "chatId": 1,
              "remetenteId": 1,
              "texto": "Obrigado pela escuta de hoje.",
              "dataEnvio": "25/03/2026 14:15:00"
            }
            """;

    public static final String ERROR_NOT_FOUND = """
            {
              "timestamp": "25/03/2026 16:30:00",
              "status": 404,
              "error": "Not Found",
              "message": "Usuario nao encontrado com ID: 999",
              "path": "/api/usuarios/999",
              "validationErrors": null
            }
            """;

    public static final String ERROR_NOT_FOUND_GENERIC = """
            {
              "timestamp": "25/03/2026 16:30:00",
              "status": 404,
              "error": "Not Found",
              "message": "Recurso nao encontrado.",
              "path": "/api/recurso/999",
              "validationErrors": null
            }
            """;

    public static final String ERROR_CONFLICT = """
            {
              "timestamp": "25/03/2026 16:31:00",
              "status": 409,
              "error": "Conflict",
              "message": "Ja existe usuario cadastrado com o celular informado.",
              "path": "/api/usuarios",
              "validationErrors": null
            }
            """;

    public static final String ERROR_CONFLICT_GENERIC = """
            {
              "timestamp": "25/03/2026 16:31:00",
              "status": 409,
              "error": "Conflict",
              "message": "Conflito de integridade de dados.",
              "path": "/api/recurso",
              "validationErrors": null
            }
            """;

    public static final String ERROR_VALIDATION = """
            {
              "timestamp": "25/03/2026 16:32:00",
              "status": 400,
              "error": "Bad Request",
              "message": "Erro de validacao nos campos informados.",
              "path": "/api/usuarios",
              "validationErrors": {
                "nomeReal": "O nome real e obrigatorio"
              }
            }
            """;

    public static final String ERROR_VALIDATION_GENERIC = """
            {
              "timestamp": "25/03/2026 16:32:00",
              "status": 400,
              "error": "Bad Request",
              "message": "Erro de validacao nos campos informados.",
              "path": "/api/recurso",
              "validationErrors": {
                "campo": "Campo obrigatorio"
              }
            }
            """;

    public static final String ERROR_INVALID_DATE = """
            {
              "timestamp": "25/03/2026 16:33:00",
              "status": 400,
              "error": "Bad Request",
              "message": "Formato de data invalido no corpo da requisicao.",
              "path": "/api/usuarios",
              "validationErrors": {
                "dataNascimento": "Formato de data invalido. Use dd/MM/yyyy."
              }
            }
            """;

    public static final String ERROR_INVALID_DATE_TIME = """
            {
              "timestamp": "25/03/2026 16:33:00",
              "status": 400,
              "error": "Bad Request",
              "message": "Formato de data/hora invalido no corpo da requisicao.",
              "path": "/api/chats",
              "validationErrors": {
                "dataInicio": "Formato de data/hora invalido. Use dd/MM/yyyy HH:mm:ss."
              }
            }
            """;
}
