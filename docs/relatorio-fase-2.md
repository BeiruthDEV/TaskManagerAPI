# Relatorio da Fase 2 - Preparacao da Arquitetura Limpa

Data: 2026-06-08

## 1. Resumo do que foi reorganizado

Nesta fase, o monolito Spring Boot foi reorganizado em pacotes inspirados em Arquitetura Limpa, sem criar microsservicos, sem adicionar funcionalidades e sem alterar os endpoints existentes.

O principal ajuste foi separar responsabilidades que antes estavam concentradas no pacote `com.taskmanager.api.task`. O modelo de negocio passou para `domain`, a orquestracao de casos de uso ficou em `application`, a persistencia JPA foi isolada em `infrastructure` e a camada HTTP foi movida para `presentation`.

A aplicacao continua expondo as mesmas rotas em `/api/tasks`, usando os mesmos payloads publicos e o mesmo banco H2 configurado anteriormente.

## 2. Estrutura anterior identificada

Antes desta fase, a estrutura Java principal estava concentrada assim:

```text
src/main/java/com/taskmanager/api/
|-- TaskManagerApiApplication.java
|-- config/
|   |-- DevDataLoader.java
|   `-- OpenApiConfig.java
|-- exception/
|   |-- ErrorResponse.java
|   |-- GlobalExceptionHandler.java
|   `-- TaskNotFoundException.java
`-- task/
    |-- dto/
    |   |-- TaskCreateDTO.java
    |   |-- TaskResponseDTO.java
    |   `-- TaskUpdateDTO.java
    |-- Task.java
    |-- TaskController.java
    |-- TaskPriority.java
    |-- TaskRepository.java
    |-- TaskService.java
    `-- TaskStatus.java
```

Responsabilidades observadas:

- `TaskController`: recebia requisicoes HTTP, validava DTOs e chamava o service.
- `TaskService`: orquestrava busca, criacao, atualizacao, remocao, filtro, mapeamento para DTO e acesso ao repository.
- `TaskRepository`: interface Spring Data JPA concreta.
- `Task`: entidade JPA e modelo de negocio ao mesmo tempo.
- DTOs: contratos HTTP de entrada e saida.
- `GlobalExceptionHandler`: traducao de excecoes para respostas HTTP.
- `DevDataLoader`: carga tecnica de dados de demonstracao no perfil `dev`.
- `OpenApiConfig`: metadados de documentacao Swagger/OpenAPI.

## 3. Nova estrutura aplicada

Depois da reorganizacao:

```text
src/main/java/com/taskmanager/api/
|-- TaskManagerApiApplication.java
|-- application/
|   |-- TaskService.java
|   |-- command/
|   |   |-- CreateTaskCommand.java
|   |   `-- UpdateTaskCommand.java
|   `-- port/
|       `-- TaskRepository.java
|-- domain/
|   |-- exception/
|   |   `-- TaskNotFoundException.java
|   `-- model/
|       |-- Task.java
|       |-- TaskPriority.java
|       `-- TaskStatus.java
|-- infrastructure/
|   |-- config/
|   |   |-- DevDataLoader.java
|   |   `-- OpenApiConfig.java
|   `-- persistence/
|       |-- SpringDataTaskRepository.java
|       |-- TaskJpaEntity.java
|       |-- TaskJpaMapper.java
|       `-- TaskRepositoryAdapter.java
`-- presentation/
    |-- exception/
    |   |-- ErrorResponse.java
    |   `-- GlobalExceptionHandler.java
    `-- task/
        |-- TaskController.java
        `-- dto/
            |-- TaskCreateDTO.java
            |-- TaskResponseDTO.java
            `-- TaskUpdateDTO.java
```

## 4. Explicacao das camadas

### domain

Contem os conceitos centrais do negocio:

- `Task`
- `TaskStatus`
- `TaskPriority`
- `TaskNotFoundException`

A classe `Task` deixou de ser entidade JPA. Ela agora representa o modelo de dominio sem anotacoes de persistencia ou HTTP.

### application

Contem a orquestracao dos casos de uso atuais:

- `TaskService`
- `CreateTaskCommand`
- `UpdateTaskCommand`
- porta `TaskRepository`

O service deixou de depender diretamente de Spring Data JPA e passou a depender da porta `application.port.TaskRepository`.

### infrastructure

Contem detalhes tecnicos:

- entidade JPA `TaskJpaEntity`
- repository Spring Data `SpringDataTaskRepository`
- adapter `TaskRepositoryAdapter`
- mapper `TaskJpaMapper`
- configuracoes `DevDataLoader` e `OpenApiConfig`

Essa camada adapta o banco JPA/H2 para a porta esperada pela aplicacao.

### presentation

Contem a entrada HTTP:

- `TaskController`
- DTOs de request/response
- tratamento global de excecoes HTTP

O controller preserva as rotas existentes e converte DTOs de entrada em commands de aplicacao. As respostas continuam usando `TaskResponseDTO`.

## 5. Arquivos movidos ou alterados

Arquivos reorganizados para `domain`:

- `src/main/java/com/taskmanager/api/domain/model/Task.java`
- `src/main/java/com/taskmanager/api/domain/model/TaskPriority.java`
- `src/main/java/com/taskmanager/api/domain/model/TaskStatus.java`
- `src/main/java/com/taskmanager/api/domain/exception/TaskNotFoundException.java`

Arquivos reorganizados/criados em `application`:

- `src/main/java/com/taskmanager/api/application/TaskService.java`
- `src/main/java/com/taskmanager/api/application/command/CreateTaskCommand.java`
- `src/main/java/com/taskmanager/api/application/command/UpdateTaskCommand.java`
- `src/main/java/com/taskmanager/api/application/port/TaskRepository.java`

Arquivos reorganizados/criados em `infrastructure`:

- `src/main/java/com/taskmanager/api/infrastructure/config/DevDataLoader.java`
- `src/main/java/com/taskmanager/api/infrastructure/config/OpenApiConfig.java`
- `src/main/java/com/taskmanager/api/infrastructure/persistence/TaskJpaEntity.java`
- `src/main/java/com/taskmanager/api/infrastructure/persistence/SpringDataTaskRepository.java`
- `src/main/java/com/taskmanager/api/infrastructure/persistence/TaskJpaMapper.java`
- `src/main/java/com/taskmanager/api/infrastructure/persistence/TaskRepositoryAdapter.java`

Arquivos reorganizados para `presentation`:

- `src/main/java/com/taskmanager/api/presentation/task/TaskController.java`
- `src/main/java/com/taskmanager/api/presentation/task/dto/TaskCreateDTO.java`
- `src/main/java/com/taskmanager/api/presentation/task/dto/TaskUpdateDTO.java`
- `src/main/java/com/taskmanager/api/presentation/task/dto/TaskResponseDTO.java`
- `src/main/java/com/taskmanager/api/presentation/exception/ErrorResponse.java`
- `src/main/java/com/taskmanager/api/presentation/exception/GlobalExceptionHandler.java`

Testes ajustados para os novos pacotes:

- `src/test/java/com/taskmanager/api/task/TaskServiceTest.java`
- `src/test/java/com/taskmanager/api/task/TaskRepositoryTest.java`

Documentacao criada nesta fase:

- `docs/relatorio-fase-2.md`

## 6. Justificativa tecnica das mudancas

A mudanca foi feita para aproximar o projeto de uma separacao por responsabilidades:

- O dominio nao depende mais de JPA, controller ou DTO HTTP.
- A aplicacao depende de uma porta de repository, nao do repository Spring Data concreto.
- A infraestrutura concentra os detalhes de banco e mapeamento.
- A presentation concentra rotas, DTOs e tratamento HTTP.

Essa reorganizacao cria uma base mais segura para fases futuras, porque permite refatorar regras de aplicacao e persistencia com menor impacto nos contratos HTTP.

## 7. Contribuicao para os criterios academicos

### Clean Code

- Reduz a mistura de responsabilidades no pacote antigo `task`.
- Deixa os nomes das camadas mais claros.
- Isola DTOs HTTP da regra de aplicacao.
- Torna explicito o mapeamento entre dominio e persistencia.

### SOLID

- Melhora o principio da responsabilidade unica ao separar dominio, aplicacao, infraestrutura e presentation.
- Melhora inversao de dependencia ao fazer `TaskService` depender de `TaskRepository` como porta de aplicacao.
- Facilita testes do service com mock da porta, sem dependencia direta do Spring Data JPA.

### Arquitetura Limpa

- Introduz uma direcao de dependencia mais adequada: presentation chama application, application usa domain e porta, infrastructure implementa a porta.
- Remove anotacoes JPA do modelo de dominio.
- Mantem detalhes tecnicos fora do nucleo de negocio.

## 8. Riscos e pontos que ainda precisam ser melhorados

- A porta `TaskRepository` ainda usa `Page` e `Pageable` do Spring Data para manter a fase pequena. Em uma fase futura, isso pode ser substituido por tipos proprios de paginacao da aplicacao.
- Os testes de controller/API ainda nao foram ampliados; a suite atual cobre service, repository/adapter e contexto Spring.
- Ainda nao ha BDD, Docker, deploy, CI/CD ou microsservicos.
- A entidade JPA e o modelo de dominio ainda possuem campos muito parecidos, o que e esperado nesta etapa, mas pode gerar mapeamento repetitivo.
- A atualizacao parcial ainda preserva a regra existente de ignorar campos `null`, inclusive para `dueDate`.

## 9. Verificacao executada

Comando:

```bash
.\mvnw.cmd test
```

Resultado:

- Build: sucesso.
- Testes executados: 6.
- Falhas: 0.
- Erros: 0.
- Ignorados: 0.

## 10. Proxima fase recomendada

A proxima fase recomendada e criar uma base de testes de presentation/API:

- testes para `TaskController`;
- cobertura de rotas principais;
- validacoes de request;
- respostas 400 e 404;
- garantia explicita de que os contratos HTTP continuam preservados.

Essa fase deve vir antes de novas refatoracoes, BDD, Docker, deploy ou microsservicos.
