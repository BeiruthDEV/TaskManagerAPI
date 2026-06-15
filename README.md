# Trackio - TaskManagerAPI

Trackio e uma API REST para gerenciamento de tarefas, desenvolvida com Java, Spring Boot, Spring Data JPA e H2 Database. O projeto sera adaptado incrementalmente para uma entrega academica com demonstracao pratica de Clean Code, SOLID, Design Patterns, TDD, BDD, Arquitetura Limpa, Microsservicos, Docker/Docker Compose e Deploy.

## Problema escolhido

Equipes pequenas frequentemente precisam organizar atividades, responsaveis, prioridades, status, prazos e progresso sem depender de ferramentas complexas demais para o seu contexto. O problema escolhido e a falta de uma plataforma simples, objetiva e evolutiva para acompanhar tarefas de um time pequeno.

## Proposta da solucao

A proposta e evoluir o Trackio como uma plataforma de gerenciamento de tarefas para equipes pequenas. No estado atual, o sistema permite cadastrar, listar, atualizar, filtrar e remover tarefas. Nas proximas fases, o projeto sera preparado como estudo academico, com documentacao, testes, arquitetura mais clara, containerizacao e deploy demonstravel.

## Escopo atual do sistema

O sistema atualmente contempla tarefas com:

- Titulo.
- Descricao.
- Responsavel.
- Projeto.
- Progresso.
- Status.
- Prioridade.
- Data limite.

Endpoints principais:

| Metodo | Rota | Descricao |
|---|---|---|
| POST | `/api/tasks` | Cria uma nova tarefa |
| GET | `/api/tasks?page=0&size=10` | Lista tarefas com paginacao |
| GET | `/api/tasks/{id}` | Busca uma tarefa pelo ID |
| PUT | `/api/tasks/{id}` | Atualiza uma tarefa |
| DELETE | `/api/tasks/{id}` | Remove uma tarefa |
| GET | `/api/tasks/filter?status=PENDENTE&priority=ALTA` | Filtra tarefas por status e prioridade |

## Tecnologias utilizadas

- Java 17 configurado no `pom.xml`.
- Spring Boot 3.3.5.
- Spring Web.
- Spring Data JPA / Hibernate.
- H2 Database.
- Jakarta Bean Validation.
- springdoc-openapi / Swagger UI.
- Maven e Maven Wrapper.
- JUnit 5, AssertJ e Mockito.
- HTML, CSS e JavaScript estatico.

## Como rodar o projeto atualmente

Pre-requisitos:

- Java 17 ou superior.
- Maven ou Maven Wrapper.

No Windows:

```bash
.\mvnw.cmd spring-boot:run
```

A aplicacao fica disponivel em:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

H2 Console no perfil padrao de desenvolvimento:

```text
http://localhost:8080/h2-console
```

## Como executar os testes

```bash
.\mvnw.cmd test
```

Na auditoria da Fase 0 e na documentacao da Fase 1, os testes existentes foram executados com sucesso.

## Status atual da adequacao ao enunciado

| Criterio | Estado atual | Adaptacao planejada |
|---|---|---|
| Clean Code | Ha nomes claros, DTOs, validacoes e tratamento global de excecoes. | Reduzir responsabilidades, duplicacoes e melhorar organizacao interna. |
| SOLID | Ha separacao inicial entre controller, service e repository. | Introduzir contratos e reduzir acoplamento com framework e persistencia. |
| Design Patterns | Ja aparecem Repository, DTO, Dependency Injection e Exception Handler. | Demonstrar patterns de forma intencional, como Use Case, Mapper, Ports/Adapters e Specification quando aplicavel. |
| TDD | Existem testes unitarios e de repository. | Ampliar testes antes de refatorar comportamento. |
| BDD | Ainda nao implementado. | Criar cenarios Gherkin/Cucumber para fluxos principais. |
| Arquitetura Limpa | Ainda nao implementada formalmente. | Separar dominio, aplicacao, infraestrutura e interfaces de entrada. |
| Microsservicos | Ainda nao implementado. | Avaliar extracao apenas apos estabilizar arquitetura, testes e Docker. |
| Docker/Docker Compose | Ainda nao implementado. | Criar Dockerfile e compose em fase propria. |
| Deploy | Ainda nao implementado. | Definir alvo de servidor/cloud e publicar versao demonstravel. |

## Evidencias existentes

- API REST funcional para tarefas.
- Persistencia local com H2.
- Swagger/OpenAPI configurado.
- Testes automatizados em `src/test`.
- Relatorio de auditoria tecnica em `docs/auditoria-fase-0.md`.
- Documentacao academica inicial em `docs/proposta-academica.md` e `docs/roadmap-academico.md`.

## Roadmap resumido

1. Fase 0: auditoria tecnica do estado atual. Concluida.
2. Fase 1: documentacao academica base. Em andamento nesta entrega.
3. Fase 2: baseline de qualidade com testes de controller/API.
4. Fase 3: limpeza incremental de aplicacao e mapeamentos.
5. Fase 4: preparacao para Arquitetura Limpa.
6. Fase 5: BDD com cenarios executaveis.
7. Fase 6: migracoes de banco e perfil de persistencia mais realista.
8. Fase 7: Docker e Docker Compose.
9. Fase 8: CI/CD.
10. Fase 9: Deploy.
11. Fase 10: avaliacao de microsservicos, se necessario para o enunciado.

## Documentacao complementar

- [Auditoria tecnica - Fase 0](docs/auditoria-fase-0.md)
- [Proposta academica](docs/proposta-academica.md)
- [Roadmap academico](docs/roadmap-academico.md)

## Licenca

Este projeto esta sob a licenca MIT.
