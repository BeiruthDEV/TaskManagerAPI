# Trackio - TaskManagerAPI

Trackio e um sistema em evolucao para gestao empresarial de demandas, tarefas, responsaveis, prazos e produtividade. O repositorio atual contem uma API REST em Java/Spring Boot, uma interface web estatica para o modulo de tarefas e documentacao academica para demonstrar Clean Code, SOLID, Design Patterns, TDD, BDD, Arquitetura Limpa, Docker, microsservicos planejados e deploy.

## Link do sistema publicado

Aplicacao publicada na Railway:

```text
https://taskmanagerapi-production-b8b0.up.railway.app/
```

Endpoints publicos principais:

- Frontend: https://taskmanagerapi-production-b8b0.up.railway.app/
- API tarefas: https://taskmanagerapi-production-b8b0.up.railway.app/api/tasks
- Dashboard: https://taskmanagerapi-production-b8b0.up.railway.app/api/tasks/dashboard
- Kanban: https://taskmanagerapi-production-b8b0.up.railway.app/api/tasks/kanban
- Swagger: https://taskmanagerapi-production-b8b0.up.railway.app/swagger-ui.html
- OpenAPI: https://taskmanagerapi-production-b8b0.up.railway.app/v3/api-docs

## Itens obrigatorios da entrega

| Item exigido | Status | Evidencia |
|---|---|---|
| Descricao do problema escolhido | Atendido | `README.md`; `docs/proposta.md`; `docs/proposta-sistema-gestao-demandas.md` |
| Divisao da solucao em microsservicos | Atendido com justificativa | `docs/microsservicos.md`; bounded contexts Auth, Organization, Task, Report e Notification |
| Organizacao do projeto utilizando Arquitetura Limpa | Atendido | `docs/arquitetura.md`; pacotes `domain`, `application`, `infrastructure`, `presentation`; ports/adapters |
| Aplicacao dos principios SOLID | Atendido | `docs/solid.md`; use cases pequenos; porta `TaskRepository`; `TaskRepositoryAdapter`; DI por construtor |
| Aplicacao de Design Patterns adequados ao contexto da solucao, no minimo 4 | Atendido | `docs/design-patterns.md`; Repository, Adapter, Mapper, Factory, Facade/Service, DTO, Dependency Injection, Global Exception Handler |
| Evidencias de Clean Code | Atendido | `docs/clean-code.md`; DTOs; use cases; mapper; exception handler; validacoes; nomes claros |
| Testes criados com TDD | Atendido | `docs/tdd.md`; `src/test`; 55 testes automatizados; testes de dominio, use cases, service, repository e controller |
| Cenarios de comportamento usando BDD | Atendido | `docs/bdd.md`; `src/test/resources/features/tasks.feature`; Cucumber |
| Configuracao com Docker ou Docker Compose | Atendido | `Dockerfile`; `docker-compose.yml`; `.env.example`; `docs/docker.md` |
| Sistema publicado e ativo em servidor/plataforma cloud | Atendido | Railway: https://taskmanagerapi-production-b8b0.up.railway.app/; `docs/deploy.md`; `docs/relatorio-fase-deploy.md` |
| Link de acesso ao sistema publicado | Atendido | https://taskmanagerapi-production-b8b0.up.railway.app/ |
| Justificativa tecnica das escolhas realizadas | Atendido | `docs/justificativas-tecnicas.md`; `docs/checklist-entrega.md`; `docs/dados-demo.md` |

## Criterios de avaliacao

| Criterio | Pontuacao | Status | Evidencias no projeto |
|---|---:|---|---|
| Descricao do problema e proposta da solucao | 0,8 | Atendido | `README.md`; `docs/proposta.md`; `docs/proposta-sistema-gestao-demandas.md`; `docs/requisitos.md` |
| Aplicacao de Clean Code | 0,8 | Atendido | `docs/clean-code.md`; DTOs em `src/main/java/.../presentation/task/dto`; use cases em `src/main/java/.../application/usecase`; `TaskJpaMapper`; `GlobalExceptionHandler` |
| Uso correto dos principios SOLID | 1,0 | Atendido | `docs/solid.md`; `TaskRepository` como porta; use cases pequenos; `TaskRepositoryAdapter`; injecao por construtor |
| Aplicacao adequada de Design Patterns | 0,8 | Atendido | `docs/design-patterns.md`; Repository, Adapter, Mapper, Factory, Facade/Service, DTO, DI e Global Exception Handler |
| Arquitetura Limpa e organizacao das camadas | 1,0 | Atendido | `docs/arquitetura.md`; pacotes `domain`, `application`, `infrastructure`, `presentation`; ports/adapters; `PageQuery`/`PageResult` na application |
| Divisao adequada em microsservicos | 0,8 | Atendido com justificativa | `docs/microsservicos.md`; bounded contexts Auth, Organization, Task, Report e Notification; monolito modular preparado para extracao futura |
| Uso de TDD e testes unitarios | 0,8 | Atendido | `docs/tdd.md`; `src/test`; 55 testes automatizados; testes de dominio, factory, use cases, service, repository e controller |
| Uso de BDD e cenarios de comportamento | 0,8 | Atendido | `docs/bdd.md`; `src/test/resources/features/tasks.feature`; `CucumberTest`; `TaskStepDefinitions` |
| Docker/Docker Compose | 0,5 | Atendido | `docs/docker.md`; `Dockerfile`; `docker-compose.yml`; `.env.example` |
| Deploy ativo em servidor/cloud | 0,5 | Atendido | Railway: https://taskmanagerapi-production-b8b0.up.railway.app/; `docs/deploy.md`; `docs/relatorio-fase-deploy.md` |
| Clareza da explicacao e justificativas tecnicas | 0,2 | Atendido | `docs/justificativas-tecnicas.md`; `docs/checklist-entrega.md`; `docs/dados-demo.md`; README com evidencias e links |

## Observacao sobre microsservicos

A implementacao atual e um **monolito modular**. Nao ha microsservicos executaveis separados nesta versao.

A divisao em microsservicos foi documentada como proposta arquitetural em `docs/microsservicos.md`. Os bounded contexts planejados sao:

- Auth;
- Organization;
- Task;
- Report;
- Notification.

Essa escolha evita complexidade prematura de rede, deploy independente, observabilidade distribuida e consistencia eventual em um projeto academico de escopo controlado. O codigo foi organizado em camadas e ports/adapters para permitir extracao futura quando houver necessidade real ou exigencia especifica.

## Dados de demonstracao

A empresa **Atlas Solucoes Empresariais** e ficticia. Os dados de tarefas, responsaveis, projetos, prazos, status, prioridades e indicadores sao mockados/semeados para demonstracao.

O objetivo e permitir que o professor visualize o sistema como se uma empresa real estivesse usando a aplicacao, sem depender de uma tela vazia ou de cadastro manual durante a apresentacao.

Documento completo: `docs/dados-demo.md`.

## Como validar localmente

Validacao local:

```powershell
.\mvnw.cmd test
node --check src/main/resources/static/app.js
node --check src/main/resources/static/task-api.js
docker compose config
docker compose up -d --build
```

Validacao publica rapida:

```powershell
$base='https://taskmanagerapi-production-b8b0.up.railway.app'
$paths=@('/','/api/tasks','/api/tasks/dashboard','/api/tasks/kanban','/swagger-ui.html','/v3/api-docs')
foreach($path in $paths){
  $r=Invoke-WebRequest -Uri ($base+$path) -Method GET -UseBasicParsing -TimeoutSec 30
  "$($r.StatusCode) $path"
}
```

## Problema escolhido

Muitas empresas ainda controlam demandas por planilhas, WhatsApp, e-mails soltos, anotacoes manuais ou quadros Trello sem padronizacao. Isso dificulta a visao de responsaveis, prazos, prioridades, status e produtividade.

O Trackio busca resolver esse problema com uma plataforma interna simples, visual e profissional para organizar o trabalho da empresa.

## Solucao proposta

A solucao proposta e um sistema empresarial de gestao de demandas. No estado atual, o foco implementado e o modulo de tarefas. A evolucao planejada inclui organizacoes, usuarios, funcionarios, equipes, projetos, Kanban, dashboard, comentarios, anexos e historico.

Do ponto de vista comercial, o Trackio e pensado como software de valor fechado/licenca de uso, nao como SaaS mensal obrigatorio. A empresa pode pagar uma vez pelo escopo contratado e usar o sistema por tempo indeterminado. Suporte continuo, hospedagem, manutencao e novas funcionalidades podem ser cobrados separadamente.

## Estado atual do projeto

O projeto hoje possui:

- API REST para tarefas.
- Frontend estatico servido pelo Spring Boot em `/`.
- Listagem real de tarefas via `/api/tasks`.
- Criacao, edicao e exclusao de tarefas pela interface.
- Sidebar com modulos ativos e secao de roadmap.
- Endpoint real de Kanban para tarefas agrupadas por status.
- Endpoint real de dashboard com indicadores basicos de tarefas.
- Documentacao academica em `docs/`.
- Testes unitarios, teste de repository e cenarios BDD.
- Dockerfile e docker-compose.yml para execucao da API em container.

O projeto ainda nao possui:

- login/autenticacao;
- usuarios e funcionarios reais;
- organizacoes/empresas;
- equipes/departamentos;
- projetos como modulo proprio;
- comentarios, anexos e historico;
- microsservicos executaveis separados.

## Funcionalidades implementadas

Modulo Tasks:

- criar tarefa;
- listar tarefas paginadas;
- buscar tarefa por id;
- atualizar tarefa;
- excluir tarefa;
- filtrar tarefas por status e prioridade;
- consultar Kanban real agrupado por status;
- consultar dashboard basico com indicadores reais;
- campos: titulo, descricao, responsavel, projeto, progresso, status, prioridade e prazo;
- tratamento global de erros;
- validacao de payloads;
- documentacao OpenAPI/Swagger.

Frontend atual:

- tela principal de tarefas;
- tela dashboard com indicadores da API;
- tela Kanban alimentada pelo endpoint de tarefas agrupadas por status;
- cards de resumo;
- busca local;
- filtro por status;
- tabela de tarefas reais;
- estados de loading, erro, vazio e sucesso;
- roadmap visual para modulos futuros, sem simular funcionalidades inexistentes.

Observacao: Kanban e dashboard ja existem como endpoints e tambem possuem telas no frontend. O escopo ainda continua concentrado no modulo de tarefas; projetos, equipes, usuarios e organizacoes permanecem como modulos futuros.

## Funcionalidades planejadas

- autenticacao e autorizacao;
- organizacao/empresa;
- funcionarios/membros;
- equipes/departamentos;
- projetos;
- evolucao da tela Kanban;
- evolucao do dashboard;
- comentarios;
- anexos;
- historico de atividades;
- notificacoes;
- relatorios;
- evolucao para microsservicos quando fizer sentido.

## Deploy publico

A aplicacao esta publicada e acessivel na Railway.

```text
https://taskmanagerapi-production-b8b0.up.railway.app/
```

Deploy realizado em 17/06/2026.

Endpoints publicos validados:

- `/`
- `/api/tasks`
- `/api/tasks/dashboard`
- `/api/tasks/kanban`
- `/swagger-ui.html`
- `/v3/api-docs`

O ambiente publicado usa dados ficticios da **Atlas Solucoes Empresariais** para demonstracao. Esses dados nao representam clientes reais.

## Arquitetura atual

A estrutura principal segue uma organizacao inspirada em Arquitetura Limpa:

```text
src/main/java/com/taskmanager/api
  domain
  application
  infrastructure
  presentation
```

- `domain`: modelo de dominio, enums e excecoes.
- `application`: comandos, portas, factory, use cases e facade de aplicacao.
- `infrastructure`: configuracoes, persistencia JPA, adapter e mapper.
- `presentation`: controllers, DTOs e tratamento de excecoes HTTP.

O projeto foi mantido como monolito modular para evitar complexidade prematura, mas a divisao por camadas deixa claro o caminho de evolucao. A camada `application` usa `PageQuery` e `PageResult` proprios, sem expor `Page`/`Pageable` do Spring Data. Esses tipos de framework ficam isolados no adapter de infraestrutura (`TaskRepositoryAdapter`).

Ainda existem pontos em evolucao, como uso de anotacoes Spring (`@Service`/`@Transactional`) em classes de application. Essa foi uma decisao pragmatica para aproveitar injecao de dependencia e transacoes declarativas do Spring Boot sem criar configuracao manual excessiva nesta entrega.

## Dados de demonstracao

Para a apresentacao, o profile `dev` carrega dados ficticios de uma empresa chamada **Atlas Solucoes Empresariais**. Esses dados sao criados pelo `DevDataLoader.java` e simulam setores, responsaveis, projetos, tarefas, prazos, prioridades, status e indicadores para que o professor veja o frontend como se ele estivesse em uso por uma empresa real.

Os dados demo nao representam clientes reais. Em producao, as tarefas seriam cadastradas pelos usuarios da empresa contratante. A explicacao completa esta em [Dados de demonstracao](docs/dados-demo.md).

## Tecnologias

- Java 17.
- Spring Boot 3.3.5.
- Spring Web.
- Spring Data JPA.
- H2 Database.
- PostgreSQL para execucao via Docker/profile de producao.
- Jakarta Bean Validation.
- Swagger/OpenAPI via springdoc.
- Maven Wrapper.
- JUnit 5, AssertJ e Mockito.
- Cucumber BDD.
- Docker e Docker Compose.
- HTML, CSS e JavaScript estatico.

## Como rodar localmente com H2

Pre-requisitos:

- Java 17 ou superior.
- Maven Wrapper incluido no repositorio.

O profile padrao e `dev`, usando H2 em memoria:

```powershell
.\mvnw.cmd spring-boot:run
```

A aplicacao fica disponivel em:

```text
http://localhost:8080
```

H2 Console:

```text
http://localhost:8080/h2-console
```

## Como rodar com Docker

Pre-requisitos:

- Docker Desktop ou Docker Engine ativo.

O Docker Compose sobe a API com o profile `docker` e um PostgreSQL persistente:

Crie o arquivo local de ambiente a partir do exemplo:

```powershell
Copy-Item .env.example .env
```

Depois suba os containers:

```powershell
docker compose down
docker compose build
docker compose up -d
```

A API fica acessivel em:

```text
http://localhost:8081
```

O PostgreSQL fica disponivel no host em:

```text
localhost:5432
database: trackio
user: trackio
password: trackio
```

Esses valores podem ser ajustados no arquivo `.env`.

O volume `taskmanager-postgres-data` preserva os dados entre reinicios dos containers. Para remover tambem os dados persistidos:

```powershell
docker compose down -v
```

## Endpoints principais

| Metodo | Rota | Descricao |
|---|---|---|
| `GET` | `/` | Interface web estatica |
| `GET` | `/api/tasks?page=0&size=10` | Lista tarefas com paginacao |
| `GET` | `/api/tasks/{id}` | Busca uma tarefa por id |
| `POST` | `/api/tasks` | Cria uma tarefa |
| `PUT` | `/api/tasks/{id}` | Atualiza uma tarefa |
| `DELETE` | `/api/tasks/{id}` | Remove uma tarefa |
| `GET` | `/api/tasks/filter?status=PENDENTE&priority=ALTA` | Filtra tarefas |
| `GET` | `/api/tasks/kanban` | Retorna tarefas agrupadas por status para Kanban |
| `GET` | `/api/tasks/dashboard` | Retorna indicadores basicos de tarefas |

## Swagger/OpenAPI

Quando a aplicacao esta rodando:

```text
http://localhost:8080/swagger-ui.html
```

No Docker Compose:

```text
http://localhost:8081/swagger-ui.html
```

Ambiente publicado:

```text
https://taskmanagerapi-production-b8b0.up.railway.app/swagger-ui.html
```

## Testes

Executar:

```powershell
.\mvnw.cmd test
```

Estado real:

- testes unitarios: existentes;
- testes de use cases: existentes;
- testes de service/facade: existentes;
- testes de dominio: existentes;
- teste de repository JPA: existente;
- testes de controller/API HTTP com MockMvc: existentes;
- BDD com Cucumber: existente;
- testes automatizados do frontend: ainda pendentes.

## Status academico

| Criterio | Status atual |
|---|---|
| Clean Code | Atendido com boa separacao de responsabilidades; dominio simples e evolucao futura documentada |
| SOLID | Atendido com justificativa: SRP, DIP, ISP e DI demonstrados em use cases, porta e adapters |
| Design Patterns | Atendido com Repository, Adapter, Mapper, Factory, Facade/Service, DTO, DI e Exception Handler |
| TDD | Atendido com justificativa: 55 testes automatizados e ciclos de teste/refatoracao documentados |
| BDD | Atendido no fluxo basico de tarefas com Cucumber |
| Arquitetura Limpa | Atendida com justificativa: monolito modular em camadas, ports/adapters e limite pragmatico do Spring |
| Microsservicos | Atendido como modelagem arquitetural; nao ha servicos separados executaveis nesta versao |
| Docker | Atendido com Dockerfile, Compose, PostgreSQL e volume persistente |
| Deploy | Atendido na Railway com URL publica validada |
| Justificativas tecnicas | Documentadas em `docs/` |

## Documentacao principal

- [Proposta](docs/proposta.md)
- [Proposta detalhada do sistema](docs/proposta-sistema-gestao-demandas.md)
- [Requisitos](docs/requisitos.md)
- [Arquitetura](docs/arquitetura.md)
- [Clean Code](docs/clean-code.md)
- [SOLID](docs/solid.md)
- [Design Patterns](docs/design-patterns.md)
- [TDD](docs/tdd.md)
- [BDD](docs/bdd.md)
- [Microsservicos](docs/microsservicos.md)
- [Dados de demonstracao](docs/dados-demo.md)
- [Docker](docs/docker.md)
- [Deploy](docs/deploy.md)
- [Relatorio de deploy](docs/relatorio-fase-deploy.md)
- [Justificativas tecnicas](docs/justificativas-tecnicas.md)
- [Roadmap](docs/roadmap.md)
- [Checklist da entrega](docs/checklist-entrega.md)

## Licenca

Este projeto esta sob a licenca MIT.
