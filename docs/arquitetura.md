# Arquitetura

## Organizacao atual

O projeto esta organizado em pacotes inspirados em Arquitetura Limpa:

```text
com.taskmanager.api
  domain
  application
  infrastructure
  presentation
```

## domain

Contem o modelo de dominio e regras basicas:

- `Task`;
- `TaskStatus`;
- `TaskPriority`;
- `TaskNotFoundException`.

Essa camada nao depende diretamente de controller, banco de dados ou DTO HTTP.

## application

Contem os casos de uso e contratos de aplicacao:

- `CreateTaskUseCase`;
- `UpdateTaskUseCase`;
- `DeleteTaskUseCase`;
- `FindTaskUseCase`;
- `ListTasksUseCase`;
- `TaskRepository`;
- `TaskFactory`;
- `TaskService`.

Essa camada coordena as regras de aplicacao e depende de uma porta (`TaskRepository`) em vez de depender diretamente de Spring Data JPA.

## infrastructure

Contem detalhes tecnicos:

- entidade JPA;
- repository Spring Data;
- adapter de persistencia;
- mapper JPA;
- configuracoes de desenvolvimento;
- configuracao OpenAPI.

## presentation

Contem a camada de entrada HTTP:

- `TaskController`;
- DTOs de request/response;
- `GlobalExceptionHandler`;
- `ErrorResponse`.

## Aderencia a Arquitetura Limpa

Pontos positivos:

- dominio separado de infraestrutura;
- controller separado dos use cases;
- adapter JPA implementa porta de aplicacao;
- DTOs nao sao usados como entidade de dominio;
- mapper converte entidade JPA para dominio.

## Pontos em evolucao

Ainda ha acoplamentos que podem ser reduzidos:

- `application` usa anotacoes Spring como `@Service` e `@Transactional`;
- a porta `TaskRepository` ainda recebe `Page` e `Pageable`, tipos do Spring Data;
- `TaskService` atua como facade, mas tambem esta anotado como bean Spring;
- dominio ainda e simples e possui poucas regras comportamentais.

## Direcao recomendada

Nas proximas fases, a arquitetura pode ser fortalecida com:

- tipos proprios de paginacao na application;
- interfaces de use case;
- regras mais ricas no dominio;
- controller tests para proteger contratos;
- separacao futura por contexto: auth, organization, task, report e notification.
