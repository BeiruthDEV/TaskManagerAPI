# Relatorio da Fase 4 - Design Patterns

Data: 2026-06-08

## 1. Resumo da fase

Nesta fase, foram analisados os Design Patterns ja presentes no projeto e foi implementado apenas um padrao novo: Factory Pattern. A decisao foi manter a fase pequena e evitar padroes artificiais.

O projeto agora demonstra pelo menos quatro padroes de forma clara:

- Repository Pattern;
- Facade Pattern;
- Mapper Pattern;
- Factory Pattern.

Nao foram alterados endpoints publicos, payloads, regras de negocio, Docker, BDD, microsservicos ou deploy.

## 2. Design Patterns identificados no projeto

### Repository Pattern

Localizacao:

- `src/main/java/com/taskmanager/api/application/port/TaskRepository.java`
- `src/main/java/com/taskmanager/api/infrastructure/persistence/TaskRepositoryAdapter.java`
- `src/main/java/com/taskmanager/api/infrastructure/persistence/SpringDataTaskRepository.java`

O padrao ja estava presente desde a reorganizacao em camadas. A porta `TaskRepository` define o contrato esperado pela application, enquanto `TaskRepositoryAdapter` implementa esse contrato usando Spring Data JPA.

### Facade Pattern

Localizacao:

- `src/main/java/com/taskmanager/api/application/TaskService.java`

O `TaskService` funciona como uma fachada simples para a camada `presentation`. O controller chama uma unica entrada de application, e essa fachada delega para os casos de uso especificos.

### Mapper Pattern

Localizacao:

- `src/main/java/com/taskmanager/api/infrastructure/persistence/TaskJpaMapper.java`

O mapper separa o modelo de dominio (`Task`) da entidade JPA (`TaskJpaEntity`). Isso evita expor detalhes de persistencia para o dominio.

## 3. Design Pattern implementado nesta fase

### Factory Pattern

Localizacao:

- `src/main/java/com/taskmanager/api/application/factory/TaskFactory.java`

Foi criada a `TaskFactory` para centralizar a criacao de `Task` a partir de `CreateTaskCommand`. Antes, essa montagem estava dentro de `CreateTaskUseCase`.

Com a factory:

- `CreateTaskUseCase` fica focado em orquestrar a criacao e salvar a tarefa;
- a montagem do objeto de dominio fica em um componente dedicado;
- os defaults do dominio continuam preservados pelo construtor de `Task`;
- nenhuma regra nova foi adicionada.

## 4. Justificativa tecnica por padrao

### Repository Pattern

Justificativa:

- Isola a persistencia da camada `application`.
- Permite que casos de uso dependam de uma abstracao.
- Mantem JPA e Spring Data na camada `infrastructure`.

Contribuicao:

- Clean Code: separa acesso a dados da regra de aplicacao.
- SOLID: reforca DIP.
- Arquitetura Limpa: impede que application dependa diretamente de infraestrutura concreta.

### Facade Pattern

Justificativa:

- Preserva uma entrada simples para o `TaskController`.
- Evita que o controller conheca todos os casos de uso individualmente.
- Mantem a evolucao interna da application sem alterar a presentation.

Contribuicao:

- Clean Code: reduz acoplamento do controller com detalhes internos.
- SOLID: ajuda a manter responsabilidades claras entre presentation e application.
- Arquitetura Limpa: presentation continua chamando application, sem acessar infrastructure.

### Mapper Pattern

Justificativa:

- Separa `TaskJpaEntity` de `Task`.
- Evita que anotacoes JPA entrem no modelo de dominio.
- Centraliza conversoes entre persistencia e dominio.

Contribuicao:

- Clean Code: remove conversoes espalhadas.
- SOLID: reduz responsabilidade de adapters e entidades.
- Arquitetura Limpa: protege o dominio de detalhes tecnicos.

### Factory Pattern

Justificativa:

- Centraliza a criacao de `Task` a partir de `CreateTaskCommand`.
- Reduz responsabilidade de `CreateTaskUseCase`.
- Cria um ponto claro para futuras regras de montagem sem espalhar construtores pela application.

Contribuicao:

- Clean Code: deixa a intencao da criacao mais explicita.
- SOLID: reforca SRP no caso de uso de criacao.
- Arquitetura Limpa: a factory fica na application e depende apenas de command da application e modelo de domain.

## 5. Strategy Pattern avaliado

O Strategy Pattern foi avaliado para a filtragem em `ListTasksUseCase`, mas nao foi implementado nesta fase.

Motivo:

- A regra atual de filtro e pequena.
- Criar strategies agora adicionaria classes sem ganho proporcional.
- A fase pediu para evitar padroes artificiais.

Esse padrao pode ser reavaliado se os filtros crescerem, por exemplo com responsavel, projeto, prazo, ordenacao ou combinacoes mais complexas.

## 6. Arquivos alterados

Arquivos de producao:

- `src/main/java/com/taskmanager/api/application/factory/TaskFactory.java`
- `src/main/java/com/taskmanager/api/application/usecase/CreateTaskUseCase.java`

Arquivos de teste:

- `src/test/java/com/taskmanager/api/application/TaskServiceTest.java`
- `src/test/java/com/taskmanager/api/application/usecase/TaskUseCaseTest.java`
- `src/test/java/com/taskmanager/api/application/factory/TaskFactoryTest.java`

Documentacao:

- `docs/relatorio-fase-4.md`

## 7. Testes criados ou ajustados

Criado:

- `TaskFactoryTest`

Cobertura adicionada:

- criacao de `Task` com todos os campos vindos de `CreateTaskCommand`;
- preservacao dos defaults de dominio quando campos opcionais nao sao informados.

Ajustados:

- `TaskServiceTest`;
- `TaskUseCaseTest`.

Os ajustes apenas refletem a nova dependencia de `CreateTaskUseCase` em `TaskFactory`.

## 8. Evidencia de preservacao de endpoints e payloads

- `TaskController` nao foi alterado nesta fase.
- A rota base continua `/api/tasks`.
- Os endpoints existentes permanecem:
  - `GET /api/tasks`
  - `GET /api/tasks/{id}`
  - `POST /api/tasks`
  - `PUT /api/tasks/{id}`
  - `DELETE /api/tasks/{id}`
  - `GET /api/tasks/filter`
- DTOs publicos nao foram alterados.
- Nenhuma funcionalidade nova foi adicionada.

## 9. Respeito a Clean Architecture

- `domain` nao depende de `application`, `infrastructure` ou `presentation`.
- `application` nao depende de `infrastructure` ou `presentation`.
- `infrastructure` continua implementando a porta de repository.
- `presentation` continua chamando a fachada `TaskService`.
- A nova `TaskFactory` fica em `application` e depende apenas de command da application e modelo de domain.

## 10. Limitacoes e melhorias futuras

- O Strategy Pattern pode ser considerado quando a regra de filtro crescer.
- A porta `TaskRepository` ainda pode ser dividida em interfaces menores de leitura e escrita.
- A application ainda usa algumas anotacoes Spring para wiring/transacao, ponto que pode ser refinado futuramente.
- Ainda faltam testes especificos da camada `presentation`.

## 11. Proxima fase recomendada

A proxima fase recomendada e criar testes de controller/API para proteger os contratos publicos antes de introduzir BDD ou Docker.

Escopo sugerido:

- testar endpoints principais de `/api/tasks`;
- cobrir validacoes de request;
- cobrir respostas 400 e 404;
- garantir que os payloads publicos continuam estaveis.
