# Testes Manuais da API (Postman)

## Objetivo
Este material organiza a colecao manual de testes para o MVP atual da API.

## Estrutura sugerida
```text
docs/postman/
  README.md
  payloads/
    usuario-create.json
    usuario-update.json
    usuario-invalid-date.json
```

## Colecao recomendada no Postman
- **Nome da colecao:** `Aiury - Usuario API (MVP)`
- **Variavel de ambiente:** `baseUrl`
- **Valor sugerido para local:** `http://localhost:8080`

## Requisicoes que devem existir

| Nome da requisicao | Metodo | Rota | Body | Resultado esperado |
|---|---|---|---|---|
| Criar usuario | POST | `{{baseUrl}}/api/usuarios` | `payloads/usuario-create.json` | `201 Created` |
| Buscar usuario por ID | GET | `{{baseUrl}}/api/usuarios/{id}` | Sem body | `200 OK` ou `404 Not Found` |
| Listar usuarios | GET | `{{baseUrl}}/api/usuarios` | Sem body | `200 OK` |
| Atualizar usuario | PUT | `{{baseUrl}}/api/usuarios/{id}` | `payloads/usuario-update.json` | `200 OK`, `400` ou `404` |
| Deletar usuario | DELETE | `{{baseUrl}}/api/usuarios/{id}` | Sem body | `204 No Content` ou `404 Not Found` |

## Payloads de exemplo para importacao manual posterior
Use os arquivos JSON desta pasta para copiar e colar no Body (`raw` + `JSON`) das requisicoes no Postman.

- Criacao valida: `payloads/usuario-create.json`
- Atualizacao valida: `payloads/usuario-update.json`
- Validacao de formato de data: `payloads/usuario-invalid-date.json`

## Observacoes de uso
- O formato aceito em `dataNascimento` e `DD-MM-AAAA`.
- Em respostas, `LocalDate` tende a ser serializado em formato ISO (`AAAA-MM-DD`).
- O endpoint de `PUT` usa validacao de `UsuarioDTO`, entao requer os campos obrigatorios do DTO.
