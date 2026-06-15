# Clean Code

## Evidencias atuais

O projeto demonstra Clean Code principalmente pela separacao de responsabilidades e pelos nomes claros.

Exemplos:

- `TaskController` recebe requisicoes HTTP.
- `TaskCreateDTO`, `TaskUpdateDTO` e `TaskResponseDTO` representam contratos externos.
- `CreateTaskUseCase`, `UpdateTaskUseCase`, `DeleteTaskUseCase`, `FindTaskUseCase` e `ListTasksUseCase` separam operacoes de aplicacao.
- `TaskRepositoryAdapter` isola o acesso JPA.
- `TaskJpaMapper` concentra conversoes entre entidade JPA e dominio.
- `GlobalExceptionHandler` padroniza tratamento de erros.

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

## Pontos de melhoria

- reduzir acoplamento da application com Spring;
- evitar `Page`/`Pageable` na porta de repository;
- aumentar comportamento dentro do dominio;
- adicionar testes de controller;
- padronizar encoding dos documentos antigos que ainda aparecem com caracteres quebrados em alguns arquivos.
