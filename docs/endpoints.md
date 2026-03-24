# Endpoints Atuais da API

## Base
- Base path: `/api/usuarios`
- Controller implementado: `UsuarioController`
- Formato de conteudo: `application/json`

## 1) Criar usuario
- **HTTP:** `POST`
- **Rota:** `/api/usuarios`
- **Descricao:** cria um novo usuario e retorna o recurso com links HATEOAS.

### Payload de exemplo
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

### Resposta esperada (201)
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

### Respostas de erro comuns
- `400 Bad Request`: erro de validacao (retorno em mapa campo -> mensagem).
- `404 Not Found`: cidade nao encontrada.

---

## 2) Buscar usuario por ID
- **HTTP:** `GET`
- **Rota:** `/api/usuarios/{id}`
- **Descricao:** retorna um usuario por identificador com links HATEOAS.

### Payload de exemplo
- Nao se aplica.

### Resposta esperada (200)
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

### Respostas de erro comuns
- `404 Not Found`: usuario nao encontrado.

---

## 3) Listar usuarios
- **HTTP:** `GET`
- **Rota:** `/api/usuarios`
- **Descricao:** retorna colecao de usuarios com links HATEOAS.

### Payload de exemplo
- Nao se aplica.

### Resposta esperada (200)
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
        },
        "_links": {
          "self": {
            "href": "http://localhost:8080/api/usuarios/10"
          }
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

---

## 4) Atualizar usuario
- **HTTP:** `PUT`
- **Rota:** `/api/usuarios/{id}`
- **Descricao:** atualiza os dados de um usuario existente e retorna o recurso com links HATEOAS.

### Payload de exemplo
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

### Resposta esperada (200)
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

### Respostas de erro comuns
- `400 Bad Request`: erro de validacao.
- `404 Not Found`: usuario ou cidade nao encontrados.

---

## 5) Deletar usuario
- **HTTP:** `DELETE`
- **Rota:** `/api/usuarios/{id}`
- **Descricao:** remove usuario por ID.

### Payload de exemplo
- Nao se aplica.

### Resposta esperada
- `204 No Content` sem corpo.

### Respostas de erro comuns
- `404 Not Found`: usuario nao encontrado.

---

## Formatos de erro atuais

### Erro de validacao (400)
```json
{
  "nomeReal": "O nome e obrigatorio",
  "dataNascimento": "Formato invalido para data de nascimento. Use DD-MM-AAAA"
}
```

### Recurso nao encontrado (404)
```json
{
  "timestamp": "2026-03-24T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Usuario nao encontrado com ID: 999",
  "path": "API Endpoint"
}
```

## Pendencias e inconsistencias identificadas (seguindo o codigo atual)
- O `PUT /api/usuarios/{id}` recebe `@Valid UsuarioDTO` com campos obrigatorios, entao o endpoint funciona como atualizacao completa, apesar do service ter trechos de logica para atualizacao parcial.
- Entrada de `dataNascimento` no DTO usa `DD-MM-AAAA`; na resposta da entidade, a data tende a sair no formato ISO (`AAAA-MM-DD`) por serializacao padrao de `LocalDate`.
- O campo `path` no erro `404` e fixo como `"API Endpoint"` no handler global, nao refletindo a rota real.
- O rel de `_embedded` em listagem HATEOAS pode variar conforme convencoes do Spring/HAL.
