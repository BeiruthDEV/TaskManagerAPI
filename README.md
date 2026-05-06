# Trackio

API REST ToDo com frontend de tarefas, Spring Boot 3.x, Spring Data JPA, H2, validacao, tratamento global de erros, logging e documentacao OpenAPI.

## Requisitos

- JDK 17+

## Como executar

```powershell
.\mvnw.cmd spring-boot:run
```

A API ficara disponivel em `http://localhost:8080`.

O frontend da aplicacao fica em `http://localhost:8080`.

## Banco H2

O perfil padrao (`dev`) usa banco em memoria:

- JDBC URL: `jdbc:h2:mem:taskmanager`
- User: `sa`
- Password: vazio
- Console: `http://localhost:8080/h2-console`

Para usar H2 persistente em arquivo:

```powershell
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=file
```

Os dados serao gravados em `./data/taskmanager`.

## OpenAPI

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Endpoints

| Metodo | Rota | Descricao |
| --- | --- | --- |
| `POST` | `/api/tasks` | Cria uma tarefa e retorna `201` |
| `GET` | `/api/tasks?page=0&size=10` | Lista tarefas com paginacao |
| `GET` | `/api/tasks/{id}` | Busca tarefa por id |
| `PUT` | `/api/tasks/{id}` | Atualiza uma tarefa parcial ou completamente |
| `DELETE` | `/api/tasks/{id}` | Remove uma tarefa e retorna `204` |
| `GET` | `/api/tasks/filter?status=PENDENTE&priority=ALTA` | Filtra por status e prioridade |

## Payload de criacao

```json
{
  "title": "Finalizar API ToDo",
  "description": "Implementar endpoints REST, validacoes e testes",
  "assignee": "Michael Ardi",
  "projectName": "DODO System Upgrade",
  "progress": 40,
  "status": "PENDENTE",
  "priority": "ALTA",
  "dueDate": "2026-06-30"
}
```

## Payload de atualizacao parcial

```json
{
  "status": "CONCLUIDO",
  "priority": "MEDIA"
}
```

## Valores aceitos

Status:

- `PENDENTE`
- `EM_PROGRESSO`
- `EM_REVISAO`
- `CONCLUIDO`

Prioridade:

- `BAIXA`
- `MEDIA`
- `ALTA`

## Testes

```powershell
.\mvnw.cmd test
```
