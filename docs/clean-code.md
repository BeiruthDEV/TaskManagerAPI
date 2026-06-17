# Clean Code

## Evidencias atuais

O projeto demonstra Clean Code por escolhas concretas de organizacao, nomes e separacao de responsabilidades.

Exemplos:

- `TaskController` recebe requisicoes HTTP e delega regras para application;
- `TaskCreateDTO`, `TaskUpdateDTO`, `TaskResponseDTO` e demais DTOs representam contratos externos;
- `CreateTaskUseCase`, `UpdateTaskUseCase`, `DeleteTaskUseCase`, `FindTaskUseCase`, `ListTasksUseCase`, `KanbanTasksUseCase` e `TaskDashboardUseCase` separam operacoes de aplicacao;
- `TaskRepositoryAdapter` isola acesso JPA;
- `TaskJpaMapper` concentra conversoes entre entidade JPA e dominio;
- `GlobalExceptionHandler` padroniza tratamento de erros;
- `PageQuery` e `PageResult` evitam vazar tipos Spring Data para os casos de uso;
- validacoes declarativas nos DTOs reduzem codigo repetitivo no controller;
- nomes de testes descrevem comportamento esperado, como `shouldCreateTaskWhenCommandIsValid`.

## Separacao de DTOs

Os DTOs impedem que a API exponha diretamente a entidade JPA ou a classe de dominio. Isso facilita validacao, documentacao OpenAPI e evolucao dos contratos HTTP.

## Use cases

Os use cases deixam as operacoes mais explicitas e menores. Em vez de concentrar todo o fluxo no controller, o controller delega para a camada de aplicacao.

## Repository e Mapper

O acesso a dados fica isolado em infrastructure:

- `SpringDataTaskRepository` fala com JPA;
- `TaskRepositoryAdapter` implementa a porta;
- `TaskJpaMapper` evita espalhar conversoes pelo codigo.

## Tratamento de erros

`GlobalExceptionHandler` evita repeticao de tratamento de erro em cada controller e fornece respostas padronizadas para erros de validacao, entidade nao encontrada e falhas inesperadas.

## Dados demo com contexto

Os dados de demonstracao foram concentrados no `DevDataLoader`, em vez de ficarem espalhados pelo frontend. Isso melhora a demonstracao porque o frontend consome a API real e recebe uma massa coerente de tarefas, responsaveis, projetos e indicadores.

## Pontos de melhoria

- reduzir acoplamento da application com Spring;
- aumentar comportamento dentro do dominio;
- adicionar testes automatizados da interface;
- padronizar encoding dos documentos antigos que ainda aparecem com caracteres quebrados em alguns arquivos.

## Defesa academica

Clean Code esta demonstrado por nomes expressivos, classes pequenas, DTOs separados, use cases explicitos, mappers dedicados, exception handler centralizado, validacoes declarativas e testes legiveis. O dominio simples nao e uma falha critica nesta entrega; ele reflete o escopo atual do modulo de tarefas. Em uma evolucao futura, regras mais ricas podem migrar para metodos de dominio sem alterar o contrato externo da API.
