# Design Patterns

## Repository Pattern

Usado em `TaskRepository` para abstrair persistencia. Os use cases dependem da interface, enquanto `TaskRepositoryAdapter` implementa o acesso real via Spring Data JPA.

Beneficio: reduz acoplamento com banco e facilita testes com mocks ou repository em memoria.

## Adapter Pattern

Usado em `TaskRepositoryAdapter`. Ele adapta `SpringDataTaskRepository` para a porta de aplicacao `TaskRepository`.

Beneficio: infraestrutura fica substituivel sem alterar use cases.

## Mapper Pattern

Usado em `TaskJpaMapper` para converter `TaskJpaEntity` em `Task` e vice-versa.

Beneficio: evita misturar modelo de dominio com modelo de persistencia.

## Factory Pattern

Usado em `TaskFactory`, que cria `Task` a partir de `CreateTaskCommand`.

Beneficio: centraliza a criacao da entidade de dominio e preserva defaults.

## Facade/Service Pattern

Usado em `TaskService`, que atua como fachada para os use cases de tarefas.

Beneficio: controller chama uma interface mais simples em vez de conhecer todos os casos de uso diretamente.

## DTO Pattern

Usado em `TaskCreateDTO`, `TaskUpdateDTO` e `TaskResponseDTO`.

Beneficio: separa contrato HTTP do dominio e permite validacao especifica para entrada e saida.

## Dependency Injection

Usado pelo Spring para injetar controllers, services, use cases, adapters e repositories.

Beneficio: reduz acoplamento de construcao e facilita testes.

## Global Exception Handler

Usado em `GlobalExceptionHandler`.

Beneficio: centraliza respostas de erro e evita duplicacao em controllers.

## Patterns recomendados para evolucao

- Strategy: regras de risco, notificacao e produtividade.
- Specification/Query Object: filtros complexos de tarefas, projetos e usuarios.
- Observer/Event: historico de atividades e notificacoes.
- Builder: criacao de entidades mais complexas, como projeto e usuario.
