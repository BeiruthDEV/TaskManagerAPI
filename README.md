# Trackio

API REST para gerenciamento de tarefas, criada com **Spring Boot 3**, **Spring Data JPA** e **H2 Database**.  
O projeto funciona como uma base para um sistema de organização de tarefas no estilo **Trello**, permitindo cadastrar, listar, atualizar, filtrar e remover tarefas com status, prioridade, responsável, projeto, progresso e prazo.

---

## Sobre o projeto

O **Trackio** é uma aplicação de Task Management desenvolvida para organizar tarefas de forma simples e eficiente.  
A ideia do projeto é evoluir para uma plataforma completa de produtividade, onde usuários, equipes ou empresas possam gerenciar tarefas, acompanhar o desempenho do time, visualizar dashboards e organizar projetos em diferentes etapas.

Atualmente, o projeto conta com uma API REST funcional e uma interface inicial para gerenciamento das tarefas.

---

## Funcionalidades

- Criação de tarefas
- Listagem paginada de tarefas
- Busca de tarefa por ID
- Atualização parcial ou completa de tarefas
- Remoção de tarefas
- Filtro por status e prioridade
- Controle de progresso da tarefa
- Definição de responsável
- Definição de projeto
- Definição de prazo
- Banco H2 em memória para desenvolvimento
- Documentação da API com OpenAPI/Swagger

---

## Tecnologias utilizadas

- Java 17+
- Spring Boot 3
- Spring Web
- Spring Data JPA
- H2 Database
- Maven
- OpenAPI / Swagger
- HTML
- CSS
- JavaScript

---

## Estrutura principal da tarefa

Cada tarefa possui informações como:

- Título
- Descrição
- Responsável
- Nome do projeto
- Progresso
- Status
- Prioridade
- Data limite

---

## Status disponíveis

```txt
PENDENTE
EM_PROGRESSO
EM_REVISAO
CONCLUIDO
```

---

## Prioridades disponíveis

```txt
BAIXA
MEDIA
ALTA
```

---

## Como executar o projeto

### Pré-requisitos

Antes de começar, você precisa ter instalado:

- Java 17 ou superior
- Maven ou Maven Wrapper

---

### Executando com Maven Wrapper no Windows

```bash
.\mvnw.cmd spring-boot:run
```

A aplicação ficará disponível em:

```txt
http://localhost:8080
```

---

## Banco de dados H2

O perfil padrão utiliza banco de dados em memória:

```txt
JDBC URL: jdbc:h2:mem:taskmanager
User: sa
Password:
Console: http://localhost:8080/h2-console
```

---

## Executando com H2 persistente em arquivo

```bash
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=file
```

Os dados serão salvos em:

```txt
./data/taskmanager
```

---

## Documentação da API

Após iniciar o projeto, a documentação pode ser acessada em:

```txt
Swagger UI: http://localhost:8080/swagger-ui.html
OpenAPI JSON: http://localhost:8080/v3/api-docs
```

---

## Endpoints principais

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/tasks` | Cria uma nova tarefa |
| GET | `/api/tasks?page=0&size=10` | Lista tarefas com paginação |
| GET | `/api/tasks/{id}` | Busca uma tarefa pelo ID |
| PUT | `/api/tasks/{id}` | Atualiza uma tarefa |
| DELETE | `/api/tasks/{id}` | Remove uma tarefa |
| GET | `/api/tasks/filter?status=PENDENTE&priority=ALTA` | Filtra tarefas por status e prioridade |

---

## Exemplo de criação de tarefa

```json
{
  "title": "Finalizar API ToDo",
  "description": "Implementar endpoints REST, validações e testes",
  "assignee": "Michael Ardi",
  "projectName": "DODO System Upgrade",
  "progress": 40,
  "status": "PENDENTE",
  "priority": "ALTA",
  "dueDate": "2026-06-30"
}
```

---

## Exemplo de atualização parcial

```json
{
  "status": "CONCLUIDO",
  "priority": "MEDIA"
}
```

---

## Testes

Para executar os testes:

```bash
.\mvnw.cmd test
```

---

## Possíveis evoluções

- Autenticação de usuários
- Workspaces para empresas/equipes
- Quadros Kanban
- Dashboard com métricas de produtividade
- Relatórios de performance do time
- Comentários em tarefas
- Upload de anexos
- Notificações
- Controle de permissões
- Integração com PostgreSQL
- Deploy em produção

---

## Autor

Desenvolvido por **Matheus Beiruth**.

GitHub: [BeiruthDEV](https://github.com/BeiruthDEV)

---

## Licença

Este projeto está sob a licença MIT.
