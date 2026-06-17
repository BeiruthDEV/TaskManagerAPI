# SOLID

## SRP - Single Responsibility Principle

O projeto aplica SRP ao separar responsabilidades:

- controller: entrada HTTP;
- DTOs: contrato externo;
- use cases: operacoes de aplicacao, uma responsabilidade por arquivo;
- repository port: contrato de persistencia;
- adapter JPA: detalhe de infraestrutura;
- mapper: conversao entre modelos;
- exception handler: tratamento de erros;
- `PageQuery` e `PageResult`: paginacao na linguagem da aplicacao.

Essa divisao evita que o controller faca validacao de dominio, persistencia, mapeamento JPA e resposta HTTP ao mesmo tempo. Cada classe tem um motivo principal para mudar: DTO muda quando muda contrato HTTP; mapper muda quando muda persistencia; use case muda quando muda fluxo de aplicacao.

Limitacao assumida: o dominio ainda e simples. Em uma evolucao futura, regras como concluir tarefa, reabrir tarefa ou calcular risco poderiam migrar para metodos comportamentais do proprio `Task`.

## OCP - Open/Closed Principle

A porta `TaskRepository` permite trocar a infraestrutura de persistencia sem alterar os use cases. O sistema pode evoluir de H2/JPA para PostgreSQL/JPA, ou para um repositorio em memoria nos testes BDD, mantendo a camada de aplicacao estavel.

Tipos proprios de paginacao (`PageQuery`, `PageResult`) reforcam OCP: novas implementacoes da porta nao precisam adotar Spring Data; basta retornar `PageResult`.

Melhoria futura: aplicar Strategy para regras variaveis, como risco de tarefa, notificacao e calculo de produtividade.

## LSP - Liskov Substitution Principle

`TaskRepositoryAdapter` (JPA) e `InMemoryTaskRepository` (testes BDD) substituem `TaskRepository` sem que os use cases percebam diferenca. Ambos implementam o mesmo contrato em termos de `PageQuery`/`PageResult`, sem dependencia de Spring Data na application.

Melhoria futura: criar contratos de teste para garantir que diferentes implementacoes da porta tenham o mesmo comportamento observavel.

## ISP - Interface Segregation Principle

A aplicacao separa operacoes em use cases especificos. Isso evita uma unica classe grande com todas as responsabilidades.

Exemplos reais:

- `CreateTaskUseCase` nao precisa conhecer metodos de exclusao;
- `DeleteTaskUseCase` nao precisa conhecer dashboard;
- `TaskDashboardUseCase` nao precisa conhecer validacao HTTP;
- o controller usa `TaskService` como fachada, sem conhecer cada detalhe de infraestrutura.

Limitacao assumida: a porta `TaskRepository` ainda agrupa leitura e escrita. Isso nao invalida o criterio no escopo atual, porque o modulo e pequeno; se crescer, pode ser dividido em `TaskQueryPort` e `TaskCommandPort`.

## DIP - Dependency Inversion Principle

Os use cases dependem de `TaskRepository`, uma abstracao da application, e nao de `JpaRepository`. A camada application tambem deixou de depender de `org.springframework.data.domain.Page` e `Pageable`; passou a usar `PageQuery`/`PageResult` proprios. A conversao entre tipos proprios e tipos Spring Data ocorre exclusivamente em `TaskRepositoryAdapter` na infrastructure.

Resultado: a regra "detalhes dependem de abstracoes" vale tambem para paginacao.

## DI - Dependency Injection

A injecao de dependencia e usada de forma consistente por construtor. Controllers, use cases, facade, adapters e repositories sao conectados pelo Spring, mas a regra de negocio nao instancia diretamente seus detalhes. Isso facilita testes com mocks em `TaskUseCaseTest` e repository em memoria nos steps BDD.

## Pontos de evolucao

- `@Service` e `@Transactional` permanecem em use cases por decisao pragmatica;
- a porta `TaskRepository` pode ser dividida se o modulo crescer;
- regras de dominio podem migrar para metodos mais comportamentais em `Task`.

## Conclusao academica

SOLID esta bem demonstrado no backend atual. As limitacoes listadas sao pontos de evolucao natural, nao falhas que anulam o requisito: o projeto ja mostra SRP por camadas, DIP por porta/adapters, ISP por use cases pequenos e DI por construtor.
