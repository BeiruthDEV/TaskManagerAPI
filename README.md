# Trackio - TaskManagerAPI

Trackio e um sistema em evolucao para gestao empresarial de demandas, tarefas, responsaveis, prazos e produtividade. O repositorio atual contem uma API REST em Java/Spring Boot, uma interface web estatica para o modulo de tarefas e documentacao academica para demonstrar Clean Code, SOLID, Design Patterns, TDD, BDD, Arquitetura Limpa, Docker, microsservicos planejados e deploy.

## Problema escolhido

Muitas empresas ainda controlam demandas por planilhas, WhatsApp, e-mails soltos, anotacoes manuais ou quadros Trello sem padronizacao. Isso dificulta a visao de responsaveis, prazos, prioridades, status e produtividade.

O Trackio busca resolver esse problema com uma plataforma interna simples, visual e profissional para organizar o trabalho da empresa.

## Solucao proposta

A solucao proposta e um sistema empresarial de gestao de demandas. No estado atual, o foco implementado e o modulo de tarefas. A evolucao planejada inclui organizacoes, usuarios, funcionarios, equipes, projetos, Kanban, dashboard, comentarios, anexos e historico.

Do ponto de vista comercial, o Trackio e pensado como software de valor fechado/licenca de uso, nao como SaaS mensal obrigatorio. A empresa pode pagar uma vez pelo escopo contratado e usar o sistema por tempo indeterminado. Suporte continuo, hospedagem, manutencao e novas funcionalidades podem ser cobrados separadamente.

## Estado atual do projeto

O projeto hoje possui:

- API REST para tarefas.
- Frontend estatico servido pelo Spring Boot em `/`.
- Listagem real de tarefas via `/api/tasks`.
- Criacao, edicao e exclusao de tarefas pela interface.
- Sidebar com modulos ativos e secao de roadmap.
- Documentacao academica em `docs/`.
- Testes unitarios, teste de repository e cenarios BDD.
- Dockerfile e docker-compose.yml para execucao da API em container.

O projeto ainda nao possui:

- login/autenticacao;
- usuarios e funcionarios reais;
- organizacoes/empresas;
- equipes/departamentos;
- projetos como modulo proprio;
- Kanban funcional;
- dashboard funcional;
- comentarios, anexos e historico;
- microsservicos executaveis separados;
- deploy publicado em servidor/cloud.

## Funcionalidades implementadas

Modulo Tasks:

- criar tarefa;
- listar tarefas paginadas;
- buscar tarefa por id;
- atualizar tarefa;
- excluir tarefa;
- filtrar tarefas por status e prioridade;
- campos: titulo, descricao, responsavel, projeto, progresso, status, prioridade e prazo;
- tratamento global de erros;
- validacao de payloads;
- documentacao OpenAPI/Swagger.

Frontend atual:

- tela principal de tarefas;
- cards de resumo;
- busca local;
- filtro por status;
- tabela de tarefas reais;
- estados de loading, erro, vazio e sucesso;
- roadmap visual para modulos futuros, sem simular funcionalidades inexistentes.

## Funcionalidades planejadas

- autenticacao e autorizacao;
- organizacao/empresa;
- funcionarios/membros;
- equipes/departamentos;
- projetos;
- Kanban;
- dashboard;
- comentarios;
- anexos;
- historico de atividades;
- notificacoes;
- relatorios;
- deploy em cloud;
- evolucao para microsservicos quando fizer sentido.

## Arquitetura atual

A estrutura principal segue uma organizacao inspirada em Arquitetura Limpa:

```text
src/main/java/com/taskmanager/api
  domain
  application
  infrastructure
  presentation
```

- `domain`: modelo de dominio, enums e excecoes.
- `application`: comandos, portas, factory, use cases e facade de aplicacao.
- `infrastructure`: configuracoes, persistencia JPA, adapter e mapper.
- `presentation`: controllers, DTOs e tratamento de excecoes HTTP.

Ainda existem pontos em evolucao, como uso de anotacoes Spring em classes de application e uso de `Page`/`Pageable` na porta de repository.

## Tecnologias

- Java 17.
- Spring Boot 3.3.5.
- Spring Web.
- Spring Data JPA.
- H2 Database.
- PostgreSQL para execucao via Docker/profile de producao.
- Jakarta Bean Validation.
- Swagger/OpenAPI via springdoc.
- Maven Wrapper.
- JUnit 5, AssertJ e Mockito.
- Cucumber BDD.
- Docker e Docker Compose.
- HTML, CSS e JavaScript estatico.

## Como rodar localmente com H2

Pre-requisitos:

- Java 17 ou superior.
- Maven Wrapper incluido no repositorio.

O profile padrao e `dev`, usando H2 em memoria:

```powershell
.\mvnw.cmd spring-boot:run
```

A aplicacao fica disponivel em:

```text
http://localhost:8080
```

H2 Console:

```text
http://localhost:8080/h2-console
```

## Como rodar com Docker

Pre-requisitos:

- Docker Desktop ou Docker Engine ativo.

O Docker Compose sobe a API com o profile `docker` e um PostgreSQL persistente:

```powershell
docker compose down
docker compose build
docker compose up -d
```

A API fica acessivel em:

```text
http://localhost:8081
```

O PostgreSQL fica disponivel no host em:

```text
localhost:5432
database: trackio
user: trackio
password: trackio
```

O volume `taskmanager-postgres-data` preserva os dados entre reinicios dos containers. Para remover tambem os dados persistidos:

```powershell
docker compose down -v
```

## Endpoints principais

| Metodo | Rota | Descricao |
|---|---|---|
| `GET` | `/` | Interface web estatica |
| `GET` | `/api/tasks?page=0&size=10` | Lista tarefas com paginacao |
| `GET` | `/api/tasks/{id}` | Busca uma tarefa por id |
| `POST` | `/api/tasks` | Cria uma tarefa |
| `PUT` | `/api/tasks/{id}` | Atualiza uma tarefa |
| `DELETE` | `/api/tasks/{id}` | Remove uma tarefa |
| `GET` | `/api/tasks/filter?status=PENDENTE&priority=ALTA` | Filtra tarefas |

## Swagger/OpenAPI

Quando a aplicacao esta rodando:

```text
http://localhost:8080/swagger-ui.html
```

No Docker Compose:

```text
http://localhost:8081/swagger-ui.html
```

## Testes

Executar:

```powershell
.\mvnw.cmd test
```

Estado real:

- testes unitarios: existentes;
- testes de use cases: existentes;
- testes de service/facade: existentes;
- testes de dominio: existentes;
- teste de repository JPA: existente;
- BDD com Cucumber: existente;
- testes de controller/API HTTP: ainda pendentes;
- testes automatizados do frontend: ainda pendentes.

## Status academico

| Criterio | Status atual |
|---|---|
| Clean Code | Parcialmente atendido, com boa separacao de responsabilidades e nomes claros |
| SOLID | Parcialmente atendido, com use cases e porta de repository |
| Design Patterns | Atendido com Repository, Adapter, Mapper, Factory, Facade/Service, DTO, DI e Exception Handler |
| TDD | Parcialmente atendido por testes unitarios existentes |
| BDD | Atendido no fluxo basico de tarefas com Cucumber |
| Arquitetura Limpa | Parcialmente atendida pela estrutura em camadas |
| Microsservicos | Documentado como proposta futura; nao ha servicos separados executaveis |
| Docker | Atendido com Dockerfile, Compose, PostgreSQL e volume persistente |
| Deploy | Pendente |
| Justificativas tecnicas | Documentadas em `docs/` |

## Documentacao principal

- [Proposta](docs/proposta.md)
- [Requisitos](docs/requisitos.md)
- [Arquitetura](docs/arquitetura.md)
- [Clean Code](docs/clean-code.md)
- [SOLID](docs/solid.md)
- [Design Patterns](docs/design-patterns.md)
- [TDD](docs/tdd.md)
- [BDD](docs/bdd.md)
- [Microsservicos](docs/microsservicos.md)
- [Docker](docs/docker.md)
- [Deploy](docs/deploy.md)
- [Justificativas tecnicas](docs/justificativas-tecnicas.md)
- [Roadmap](docs/roadmap.md)
- [Checklist da entrega](docs/checklist-entrega.md)

## Licenca

Este projeto esta sob a licenca MIT.
