# TDD e Testes Unitarios

## Testes existentes

O projeto possui 55 testes automatizados em 9 suites cobrindo:

| Suite | Tipo | Foco |
|---|---|---|
| `TaskTest` | unitario domain | comportamento da entidade `Task` e defaults |
| `TaskFactoryTest` | unitario application | criacao de `Task` a partir de `CreateTaskCommand` |
| `TaskUseCaseTest` | unitario application | use cases de Create/Update/Delete/Find/List/Kanban/Dashboard com `TaskRepository` mockado |
| `TaskServiceTest` | unitario application | facade `TaskService` orquestrando use cases |
| `TaskRepositoryTest` | integracao infrastructure | `@DataJpaTest` no repository com H2 |
| `TaskControllerTest` | integracao presentation | `@WebMvcTest` com MockMvc cobrindo endpoints, validacoes e erros |
| `TaskManagerApiApplicationTests` | smoke | carga do contexto Spring |
| `CucumberTest` + `TaskStepDefinitions` | BDD | 6 cenarios em portugues sobre fluxo de tarefas |

## O que os testes validam

- defaults de tarefa quando campos opcionais sao `null`;
- criacao, atualizacao parcial, busca por id e exclusao;
- erro `TaskNotFoundException` para tarefa inexistente;
- listagem paginada usando `PageQuery`/`PageResult`;
- filtro por status e prioridade, isolados e combinados;
- agregacoes Kanban e dashboard com totais, percentuais e tarefas atrasadas;
- persistencia JPA real em H2;
- contratos HTTP do controller: status codes, headers, JSON body, validacao 400 e 404 com payload padronizado;
- preservacao do contrato JSON de paginacao para o frontend;
- enums invalidos em query param e em body;
- fluxo de negocio via Cucumber.

## Relacao com TDD

O repositorio nao tenta inventar um historico que nao existe. A defesa correta e: o backend possui uma suite automatizada forte, e as refatoracoes principais foram conduzidas preservando comportamento por meio de testes.

Em especial, a introducao de `PageQuery`/`PageResult` seguiu um fluxo de teste/refatoracao:

1. ajustar o teste que descrevia o comportamento esperado (`shouldListTasksWhenPageQueryIsProvided`);
2. refatorar a porta `TaskRepository` para o novo tipo;
3. propagar mudanca por `ListTasksUseCase`, `TaskService`, `TaskRepositoryAdapter` e `TaskController`;
4. rodar a suite e garantir 55 testes verdes;
5. consolidar a documentacao.

Esse fluxo demonstra a pratica esperada do TDD em uma evolucao incremental: comportamento descrito por testes, mudanca em passos pequenos e suite protegendo controller, application, dominio, repository e BDD.

## Padrao de testes

- **Unit**: Mockito + JUnit 5 + AssertJ. Repository mockado, sem Spring context.
- **JPA**: `@DataJpaTest` com H2.
- **MockMvc**: `@WebMvcTest(TaskController.class)` + `@MockBean TaskService` + `@Import GlobalExceptionHandler`.
- **BDD**: Cucumber JUnit Platform Suite com features em `src/test/resources/features` e steps com `InMemoryTaskRepository`.

## Testes que ainda faltam

- testes automatizados da interface (Playwright/Cypress);
- testes de contrato OpenAPI;
- testes para autenticacao, quando existir;
- testes para organizacao, usuarios e projetos, quando existirem;
- contract test entre frontend e API.

## Defesa academica

O ponto forte da entrega e a cobertura do backend: 55 testes passando em suites unitarias, integracao JPA, MockMvc e Cucumber. O ponto limitado e que ainda nao ha teste automatizado do frontend nem evidencia historica completa de todos os ciclos red/green/refactor desde o primeiro commit. Mesmo assim, para a avaliacao, o projeto demonstra uma base real de testes e usa esses testes para sustentar refatoracoes arquiteturais sem quebrar o contrato da API.
