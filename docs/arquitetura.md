# Arquitetura

## Organizacao atual

O projeto esta organizado como um **monolito modular** com pacotes inspirados em Clean Architecture. A entrega nao tenta simular varios servicos executaveis dentro do mesmo repositorio; ela concentra o escopo em um modulo de tarefas bem separado em camadas:

```text
com.taskmanager.api
  domain
  application
    pagination       (PageQuery, PageResult - tipos proprios)
    command          (CreateTaskCommand, UpdateTaskCommand)
    factory          (TaskFactory)
    port             (TaskRepository - porta de saida)
    usecase          (Create/Update/Delete/Find/List/Kanban/Dashboard)
    TaskService      (facade)
  infrastructure
    persistence      (TaskJpaEntity, TaskJpaMapper, SpringDataTaskRepository, TaskRepositoryAdapter)
    config           (DevDataLoader, OpenApiConfig)
  presentation
    task             (TaskController, DTOs)
    exception        (GlobalExceptionHandler, ErrorResponse)
```

## Domain

Contem o modelo de dominio e regras basicas:

- `Task`;
- `TaskStatus`;
- `TaskPriority`;
- `TaskNotFoundException`.

Essa camada nao depende de controller, banco de dados, DTO HTTP nem framework web.

## Application

Contem os casos de uso, contratos de aplicacao e tipos proprios:

- `CreateTaskUseCase`, `UpdateTaskUseCase`, `DeleteTaskUseCase`, `FindTaskUseCase`, `ListTasksUseCase`, `KanbanTasksUseCase`, `TaskDashboardUseCase`;
- `TaskRepository` como porta de saida;
- `TaskFactory`;
- `TaskService` como facade;
- `PageQuery` e `PageResult` para paginacao sem depender de Spring Data.

Essa camada coordena as regras de aplicacao e depende apenas da porta `TaskRepository` e de tipos proprios. Ela nao depende mais de `org.springframework.data.domain.Page` nem de `Pageable`. A remocao desses tipos fortalece a ideia de ports/adapters: os use cases nao precisam saber se a paginacao vem de Spring Data, de outro ORM, de uma API externa ou de uma implementacao em memoria usada em testes.

As anotacoes Spring (`@Service`, `@Transactional`) foram mantidas por decisao pragmatica. Em um projeto Spring Boot pequeno, isso reduz configuracao manual, preserva injecao de dependencia e transacao declarativa, e nao muda a regra central da arquitetura: a application continua dependendo de abstracoes proprias, nao de detalhes de persistencia. Uma evolucao futura pode mover essa montagem para `@Bean` em `infrastructure`.

## Infrastructure

Contem detalhes tecnicos:

- entidade JPA (`TaskJpaEntity`);
- repository Spring Data (`SpringDataTaskRepository`);
- adapter de persistencia (`TaskRepositoryAdapter`) que converte `PageQuery` para `PageRequest` e `Page<Task>` para `PageResult<Task>`;
- mapper JPA (`TaskJpaMapper`);
- dados de demonstracao (`DevDataLoader`);
- configuracao OpenAPI.

Toda dependencia de Spring Data (`Page`, `Pageable`, `PageRequest`) fica isolada nessa camada.

## Presentation

Contem a camada de entrada HTTP:

- `TaskController`, que recebe `page`/`size`, monta `PageQuery` e devolve `TaskPageResponseDTO`;
- DTOs de request/response, incluindo `TaskPageResponseDTO` para preservar o contrato JSON consumido pelo frontend;
- `GlobalExceptionHandler`;
- `ErrorResponse`.

## Aderencia a Clean Architecture

Pontos positivos:

- dominio separado de infraestrutura;
- controller separado dos use cases;
- adapter JPA implementa porta de aplicacao;
- DTOs nao sao usados como entidade de dominio;
- mapper converte entidade JPA para dominio;
- application sem tipos Spring Data;
- paginacao expressa em tipos proprios (`PageQuery`, `PageResult`);
- conversao entre Spring Data e tipos proprios isolada em `TaskRepositoryAdapter`.

## Defesa academica

Para a apresentacao, o ponto principal e explicar que Arquitetura Limpa foi aplicada de forma incremental e adequada ao tamanho do projeto:

- o sistema atual e um monolito modular, nao uma arquitetura distribuida;
- `domain` contem o modelo e nao conhece HTTP, JPA nem banco;
- `application` contem use cases, commands, factory, porta e paginacao propria;
- `infrastructure` adapta detalhes externos, como JPA e Spring Data;
- `presentation` concentra HTTP, validacao e DTOs;
- a dependencia aponta para dentro: controller chama application, infrastructure implementa a porta, e use cases dependem da abstracao `TaskRepository`.

Assim, a arquitetura atende ao criterio academico sem afirmar pureza absoluta. O limite assumido e o uso pragmatico de anotacoes Spring na camada de aplicacao.

## Pontos ainda em evolucao

- `application` continua usando `@Service` e `@Transactional` como decorators de framework;
- dominio ainda e simples e possui poucas regras comportamentais;
- nao ha interfaces explicitas de use case, pois cada use case e uma classe concreta.

## Direcao recomendada

Nas proximas fases, a arquitetura pode evoluir com:

- interfaces explicitas de use case;
- regras mais ricas no dominio (`Task.markCompleted()`, invariantes);
- migracao de `@Service`/`@Transactional` para configuracao via `@Bean` em `infrastructure`;
- separacao futura por contexto: auth, organization, task, report e notification.
