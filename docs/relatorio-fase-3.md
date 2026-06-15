# Relatorio da Fase 3 - SOLID e casos de uso

Data: 2026-06-08

## 1. Resumo da fase

Nesta fase, a camada `application` foi reorganizada para demonstrar SOLID de forma mais explicita, principalmente por meio da extracao de casos de uso pequenos. O comportamento da API foi preservado: os endpoints continuam no controller existente, os DTOs publicos nao foram alterados e nenhuma funcionalidade nova foi adicionada.

O `TaskService` foi mantido como uma fachada simples para preservar a integracao atual da camada `presentation`, enquanto as operacoes principais foram extraidas para classes de caso de uso.

## 2. Problemas de responsabilidade encontrados

Antes desta fase, `TaskService` concentrava responsabilidades demais:

- listava tarefas paginadas;
- buscava tarefa por id;
- criava tarefa;
- atualizava parcialmente tarefa;
- removia tarefa;
- filtrava tarefas por status e prioridade;
- resolvia erro de tarefa inexistente;
- registrava logs das operacoes de escrita.

Essa concentracao dificultava a demonstracao de SRP e deixava a camada `application` menos expressiva em relacao aos casos de uso reais do sistema.

## 3. Alteracoes aplicadas na camada application

Foram criados casos de uso em `src/main/java/com/taskmanager/api/application/usecase`:

- `CreateTaskUseCase`
- `UpdateTaskUseCase`
- `DeleteTaskUseCase`
- `FindTaskUseCase`
- `ListTasksUseCase`

O `TaskService` passou a delegar para esses casos de uso:

- `create` delega para `CreateTaskUseCase`;
- `update` delega para `UpdateTaskUseCase`;
- `delete` delega para `DeleteTaskUseCase`;
- `findById` delega para `FindTaskUseCase`;
- `findAll` e `filter` delegam para `ListTasksUseCase`.

Os commands existentes foram preservados:

- `CreateTaskCommand`
- `UpdateTaskCommand`

Nenhum novo payload publico foi criado.

## 4. Casos de uso criados ou ajustados

### CreateTaskUseCase

Responsabilidade:

- criar uma tarefa a partir de `CreateTaskCommand`;
- aplicar os defaults do modelo de dominio;
- salvar por meio da porta `TaskRepository`.

### UpdateTaskUseCase

Responsabilidade:

- buscar a tarefa existente;
- aplicar apenas campos informados no `UpdateTaskCommand`;
- preservar o comportamento atual de ignorar campos `null`;
- salvar a tarefa alterada.

### DeleteTaskUseCase

Responsabilidade:

- buscar a tarefa existente;
- remover a tarefa por meio da porta `TaskRepository`.

### FindTaskUseCase

Responsabilidade:

- buscar uma tarefa por id;
- lancar `TaskNotFoundException` quando ela nao existir.

### ListTasksUseCase

Responsabilidade:

- listar tarefas paginadas;
- aplicar o filtro existente por status e prioridade.

## 5. Como SOLID foi considerado

### SRP - Single Responsibility Principle

Cada caso de uso passou a representar uma operacao clara da aplicacao. O `TaskService` deixou de concentrar toda a regra de orquestracao e ficou como fachada para compatibilidade com o controller.

### OCP - Open/Closed Principle

A estrutura permite adicionar novas operacoes de tarefa criando novos casos de uso, sem alterar uma classe central grande. O controller tambem pode trocar de fachada para use cases diretamente em fase futura, se isso for desejado.

### LSP - Liskov Substitution Principle

Nao foram criadas hierarquias artificiais ou herancas desnecessarias. A fase evitou introduzir abstracoes com contratos que poderiam quebrar substituicao.

### ISP - Interface Segregation Principle

Os casos de uso dependem da porta de aplicacao `TaskRepository`, que representa o contrato de persistencia do agregado de tarefas. A porta ainda concentra operacoes de leitura e escrita; isso foi mantido para evitar refatoracao maior nesta fase. Um refinamento futuro pode dividir essa porta em contratos menores de leitura e escrita.

### DIP - Dependency Inversion Principle

Os casos de uso dependem de `application.port.TaskRepository`, nao de `SpringDataTaskRepository`, `TaskRepositoryAdapter` ou qualquer classe de `infrastructure`. A implementacao concreta continua isolada na camada `infrastructure`.

## 6. Arquivos alterados

Arquivos de producao:

- `src/main/java/com/taskmanager/api/application/TaskService.java`
- `src/main/java/com/taskmanager/api/application/usecase/CreateTaskUseCase.java`
- `src/main/java/com/taskmanager/api/application/usecase/UpdateTaskUseCase.java`
- `src/main/java/com/taskmanager/api/application/usecase/DeleteTaskUseCase.java`
- `src/main/java/com/taskmanager/api/application/usecase/FindTaskUseCase.java`
- `src/main/java/com/taskmanager/api/application/usecase/ListTasksUseCase.java`

Arquivos de teste:

- `src/test/java/com/taskmanager/api/application/TaskServiceTest.java`
- `src/test/java/com/taskmanager/api/application/usecase/TaskUseCaseTest.java`

Documentacao:

- `docs/relatorio-fase-3.md`

## 7. Testes criados ou ajustados

O teste existente de `TaskService` foi ajustado para a nova composicao por casos de uso.

Foi criado `TaskUseCaseTest`, cobrindo diretamente:

- criacao de tarefa com defaults;
- atualizacao parcial;
- busca inexistente com `TaskNotFoundException`;
- remocao de tarefa existente;
- filtro por status e prioridade.

## 8. Evidencias de preservacao de endpoints e comportamento

- `TaskController` nao foi alterado nesta fase.
- A rota base continua sendo `/api/tasks`.
- Os DTOs publicos continuam em `presentation/task/dto`.
- Os commands internos foram preservados.
- Nenhum payload publico foi alterado.
- Nenhuma funcionalidade nova foi adicionada.
- A suite automatizada foi executada com sucesso.

## 9. Verificacao de dependencias da application

Foi verificado que a camada `application` nao importa classes de:

- `infrastructure`;
- `presentation`.

Os casos de uso importam apenas:

- commands da propria application;
- porta `TaskRepository`;
- modelos/exceptions de `domain`;
- anotacoes de framework usadas para wiring/transacao no monolito atual.

## 10. Limitacoes e melhorias futuras

- A porta `TaskRepository` ainda pode ser refinada em interfaces menores de leitura e escrita para reforcar ISP.
- A camada `application` ainda possui anotacoes Spring como `@Service` e `@Transactional`; uma fase futura pode avaliar configuracao externa para reduzir acoplamento com framework.
- Ainda faltam testes especificos de controller/API para garantir contratos HTTP de forma mais explicita.
- BDD, Docker, deploy e microsservicos continuam fora do escopo desta fase.

## 11. Proxima fase recomendada

A proxima fase recomendada e criar testes da camada `presentation`, principalmente para `TaskController`, cobrindo:

- endpoints existentes;
- validacoes de request;
- respostas 400 e 404;
- preservacao dos contratos publicos da API.

Isso cria uma base melhor antes de novas refatoracoes ou da introducao de BDD.
