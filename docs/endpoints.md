# Endpoints Atuais da API

## Base
- Base path geral: `/api`
- Controllers implementados:
  - `UsuarioController`
  - `AjudanteController`
  - `ChatController`
  - `MensagemController`
- Formato de conteudo: `application/json`

---

## Usuario

### 1) Criar usuario
- **HTTP:** `POST`
- **Rota:** `/api/usuarios`
- **Descricao:** cria um novo usuario e retorna o recurso com links HATEOAS.

#### Payload de exemplo
```json
{
  "nomeReal": "Maria Silva",
  "nomeAnonimo": "LuzInterior",
  "dataNascimento": "15-08-1998",
  "celular": "11999998888",
  "senha": "segredo123",
  "cidadeId": 1
}
```

#### Resposta esperada (201)
```json
{
  "id": 10,
  "nomeReal": "Maria Silva",
  "nomeAnonimo": "LuzInterior",
  "dataNascimento": "1998-08-15",
  "celular": "11999998888",
  "senha": "segredo123",
  "dataCadastro": "2026-03-24",
  "cidade": {
    "id": 1
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/usuarios/10"
    },
    "todos-usuarios": {
      "href": "http://localhost:8080/api/usuarios"
    }
  }
}
```

### 2) Buscar usuario por ID
- **HTTP:** `GET`
- **Rota:** `/api/usuarios/{id}`
- **Descricao:** retorna um usuario por identificador com links HATEOAS.

#### Payload de exemplo
- Nao se aplica.

#### Resposta esperada (200)
```json
{
  "id": 10,
  "nomeReal": "Maria Silva",
  "nomeAnonimo": "LuzInterior",
  "dataNascimento": "1998-08-15",
  "celular": "11999998888",
  "senha": "segredo123",
  "dataCadastro": "2026-03-24",
  "cidade": {
    "id": 1
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/usuarios/10"
    },
    "todos-usuarios": {
      "href": "http://localhost:8080/api/usuarios"
    }
  }
}
```

### 3) Listar usuarios
- **HTTP:** `GET`
- **Rota:** `/api/usuarios`
- **Descricao:** retorna colecao de usuarios com links HATEOAS.

#### Payload de exemplo
- Nao se aplica.

#### Resposta esperada (200)
```json
{
  "_embedded": {
    "<rel-da-colecao>": [
      {
        "id": 10,
        "nomeReal": "Maria Silva",
        "nomeAnonimo": "LuzInterior",
        "dataNascimento": "1998-08-15",
        "celular": "11999998888",
        "senha": "segredo123",
        "dataCadastro": "2026-03-24",
        "cidade": {
          "id": 1
        }
      }
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/usuarios"
    }
  }
}
```

### 4) Atualizar usuario
- **HTTP:** `PUT`
- **Rota:** `/api/usuarios/{id}`
- **Descricao:** atualiza usuario existente e retorna recurso com links HATEOAS.

#### Payload de exemplo
```json
{
  "nomeReal": "Maria Silva Atualizada",
  "nomeAnonimo": "LuzInterior",
  "dataNascimento": "15-08-1998",
  "celular": "11999990000",
  "senha": "novaSenha123",
  "cidadeId": 2
}
```

#### Resposta esperada (200)
```json
{
  "id": 10,
  "nomeReal": "Maria Silva Atualizada",
  "nomeAnonimo": "LuzInterior",
  "dataNascimento": "1998-08-15",
  "celular": "11999990000",
  "senha": "novaSenha123",
  "dataCadastro": "2026-03-24",
  "cidade": {
    "id": 2
  },
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/usuarios/10"
    },
    "todos-usuarios": {
      "href": "http://localhost:8080/api/usuarios"
    }
  }
}
```

### 5) Deletar usuario
- **HTTP:** `DELETE`
- **Rota:** `/api/usuarios/{id}`
- **Descricao:** remove usuario por ID.

#### Payload de exemplo
- Nao se aplica.

#### Resposta esperada
- `204 No Content` sem corpo.

---

## Ajudante

### 6) Criar ajudante
- **HTTP:** `POST`
- **Rota:** `/api/ajudantes`
- **Descricao:** cria um novo ajudante.

#### Payload de exemplo
```json
{
  "areaAtuacao": "Apoio emocional",
  "motivacao": "Quero ajudar pessoas em momentos dificeis",
  "disponivel": true,
  "rating": 4.8
}
```

#### Resposta esperada (201)
```json
{
  "id": 1,
  "areaAtuacao": "Apoio emocional",
  "motivacao": "Quero ajudar pessoas em momentos dificeis",
  "disponivel": true,
  "rating": 4.8
}
```

### 7) Buscar ajudante por ID
- **HTTP:** `GET`
- **Rota:** `/api/ajudantes/{id}`
- **Descricao:** retorna um ajudante por identificador.

#### Payload de exemplo
- Nao se aplica.

#### Resposta esperada (200)
```json
{
  "id": 1,
  "areaAtuacao": "Apoio emocional",
  "motivacao": "Quero ajudar pessoas em momentos dificeis",
  "disponivel": true,
  "rating": 4.8
}
```

### 8) Listar ajudantes
- **HTTP:** `GET`
- **Rota:** `/api/ajudantes`
- **Descricao:** retorna lista de ajudantes.

#### Payload de exemplo
- Nao se aplica.

#### Resposta esperada (200)
```json
[
  {
    "id": 1,
    "areaAtuacao": "Apoio emocional",
    "motivacao": "Quero ajudar pessoas em momentos dificeis",
    "disponivel": true,
    "rating": 4.8
  }
]
```

### 9) Atualizar ajudante
- **HTTP:** `PUT`
- **Rota:** `/api/ajudantes/{id}`
- **Descricao:** atualiza um ajudante existente.

#### Payload de exemplo
```json
{
  "areaAtuacao": "Escuta ativa",
  "motivacao": "Atendimento voluntario",
  "disponivel": false,
  "rating": 4.9
}
```

#### Resposta esperada (200)
```json
{
  "id": 1,
  "areaAtuacao": "Escuta ativa",
  "motivacao": "Atendimento voluntario",
  "disponivel": false,
  "rating": 4.9
}
```

### 10) Deletar ajudante
- **HTTP:** `DELETE`
- **Rota:** `/api/ajudantes/{id}`
- **Descricao:** remove ajudante por ID.

#### Payload de exemplo
- Nao se aplica.

#### Resposta esperada
- `204 No Content` sem corpo.

---

## Chat

`status` aceito no payload: `INICIADO`, `EM_ANDAMENTO`, `FINALIZADO_USUARIO`, `FINALIZADO_AJUDANTE`, `FINALIZADO_SISTEMA`.

### 11) Criar chat
- **HTTP:** `POST`
- **Rota:** `/api/chats`
- **Descricao:** cria um chat vinculado a usuario e ajudante por ID.

#### Payload de exemplo
```json
{
  "usuarioId": 10,
  "ajudanteId": 1,
  "dataInicio": "2026-03-24T13:30:00",
  "dataFim": null,
  "status": "INICIADO"
}
```

#### Resposta esperada (201)
```json
{
  "id": 100,
  "usuarioId": 10,
  "ajudanteId": 1,
  "dataInicio": "2026-03-24T13:30:00",
  "dataFim": null,
  "status": "INICIADO"
}
```

### 12) Buscar chat por ID
- **HTTP:** `GET`
- **Rota:** `/api/chats/{id}`
- **Descricao:** retorna um chat por identificador.

#### Payload de exemplo
- Nao se aplica.

#### Resposta esperada (200)
```json
{
  "id": 100,
  "usuarioId": 10,
  "ajudanteId": 1,
  "dataInicio": "2026-03-24T13:30:00",
  "dataFim": null,
  "status": "EM_ANDAMENTO"
}
```

### 13) Listar chats
- **HTTP:** `GET`
- **Rota:** `/api/chats`
- **Descricao:** retorna lista de chats.

#### Payload de exemplo
- Nao se aplica.

#### Resposta esperada (200)
```json
[
  {
    "id": 100,
    "usuarioId": 10,
    "ajudanteId": 1,
    "dataInicio": "2026-03-24T13:30:00",
    "dataFim": null,
    "status": "EM_ANDAMENTO"
  }
]
```

### 14) Atualizar chat
- **HTTP:** `PUT`
- **Rota:** `/api/chats/{id}`
- **Descricao:** atualiza chat existente.

#### Payload de exemplo
```json
{
  "usuarioId": 10,
  "ajudanteId": 1,
  "dataInicio": "2026-03-24T13:30:00",
  "dataFim": "2026-03-24T14:10:00",
  "status": "FINALIZADO_USUARIO"
}
```

#### Resposta esperada (200)
```json
{
  "id": 100,
  "usuarioId": 10,
  "ajudanteId": 1,
  "dataInicio": "2026-03-24T13:30:00",
  "dataFim": "2026-03-24T14:10:00",
  "status": "FINALIZADO_USUARIO"
}
```

### 15) Deletar chat
- **HTTP:** `DELETE`
- **Rota:** `/api/chats/{id}`
- **Descricao:** remove chat por ID.

#### Payload de exemplo
- Nao se aplica.

#### Resposta esperada
- `204 No Content` sem corpo.

---

## Mensagem

### 16) Criar mensagem
- **HTTP:** `POST`
- **Rota:** `/api/mensagens`
- **Descricao:** cria mensagem vinculada a um chat por ID.

#### Payload de exemplo
```json
{
  "chatId": 100,
  "remetenteId": 10,
  "texto": "Estou precisando conversar.",
  "dataEnvio": "2026-03-24T13:35:00"
}
```

#### Resposta esperada (201)
```json
{
  "id": 500,
  "chatId": 100,
  "remetenteId": 10,
  "texto": "Estou precisando conversar.",
  "dataEnvio": "2026-03-24T13:35:00"
}
```

### 17) Buscar mensagem por ID
- **HTTP:** `GET`
- **Rota:** `/api/mensagens/{id}`
- **Descricao:** retorna uma mensagem por identificador.

#### Payload de exemplo
- Nao se aplica.

#### Resposta esperada (200)
```json
{
  "id": 500,
  "chatId": 100,
  "remetenteId": 10,
  "texto": "Estou precisando conversar.",
  "dataEnvio": "2026-03-24T13:35:00"
}
```

### 18) Listar mensagens
- **HTTP:** `GET`
- **Rota:** `/api/mensagens`
- **Descricao:** retorna lista de mensagens.

#### Payload de exemplo
- Nao se aplica.

#### Resposta esperada (200)
```json
[
  {
    "id": 500,
    "chatId": 100,
    "remetenteId": 10,
    "texto": "Estou precisando conversar.",
    "dataEnvio": "2026-03-24T13:35:00"
  }
]
```

### 19) Atualizar mensagem
- **HTTP:** `PUT`
- **Rota:** `/api/mensagens/{id}`
- **Descricao:** atualiza mensagem existente.

#### Payload de exemplo
```json
{
  "chatId": 100,
  "remetenteId": 10,
  "texto": "Obrigado pela escuta.",
  "dataEnvio": "2026-03-24T13:50:00"
}
```

#### Resposta esperada (200)
```json
{
  "id": 500,
  "chatId": 100,
  "remetenteId": 10,
  "texto": "Obrigado pela escuta.",
  "dataEnvio": "2026-03-24T13:50:00"
}
```

### 20) Deletar mensagem
- **HTTP:** `DELETE`
- **Rota:** `/api/mensagens/{id}`
- **Descricao:** remove mensagem por ID.

#### Payload de exemplo
- Nao se aplica.

#### Resposta esperada
- `204 No Content` sem corpo.

---

## Formatos de erro atuais

### Erro de validacao (400)
```json
{
  "campo": "mensagem de validacao"
}
```

### Recurso nao encontrado (404)
```json
{
  "timestamp": "2026-03-24T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Recurso nao encontrado com ID: 999",
  "path": "API Endpoint"
}
```
