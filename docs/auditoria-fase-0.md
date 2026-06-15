# Auditoria tecnica - Fase 0

Data da auditoria: 2026-06-08

## 1. Resumo do estado atual do projeto

O projeto TaskManagerAPI, documentado como Trackio no README, e uma aplicacao monolitica pequena para gerenciamento de tarefas. A aplicacao expoe uma API REST com Spring Boot, persiste tarefas via Spring Data JPA em banco H2 e tambem entrega uma interface estatica simples a partir de `src/main/resources/static`.

O codigo atual esta funcional e possui uma organizacao inicial por feature/pacote `task`, com controller, service, repository, entidade, DTOs e enums. Tambem existe tratamento global de excecoes, configuracao OpenAPI/Swagger e carregamento de dados de demonstracao no perfil `dev`.

Foi executado `.\mvnw.cmd test` durante a auditoria. Resultado: build com sucesso, 6 testes executados, 0 falhas, 0 erros, 0 ignorados.

## 2. Tecnologias identificadas

- Linguagem: Java 17 configurado no `pom.xml`.
- Framework principal: Spring Boot 3.3.5.
- API HTTP: Spring Web, modelo MVC com controllers REST.
- Persistencia: Spring Data JPA / Hibernate.
- Banco de dados: H2 em memoria no perfil `dev` e H2 em arquivo no perfil `file`.
- Validacao: Jakarta Bean Validation via `spring-boot-starter-validation`.
- Documentacao de API: springdoc-openapi / Swagger UI.
- Build: Maven com Maven Wrapper (`mvnw`, `mvnw.cmd`).
- Testes: JUnit 5, AssertJ, Mockito e testes JPA via `spring-boot-starter-test`.
- Frontend: HTML, CSS e JavaScript estatico em `src/main/resources/static`.
- Containerizacao: nao identificada.
- CI/CD: nao identificado workflow de pipeline.
- Deploy: nao identificada configuracao de deploy.

## 3. Estrutura atual do projeto

```text
.
|-- .github/
|   `-- modernize/java-upgrade/
|-- .mvn/
|-- assets/
|-- docs/
|   `-- images/
|-- src/
|   |-- main/
|   |   |-- java/com/taskmanager/api/
|   |   |   |-- TaskManagerApiApplication.java
|   |   |   |-- config/
|   |   |   |-- exception/
|   |   |   `-- task/
|   |   |       |-- dto/
|   |   |       |-- Task.java
|   |   |       |-- TaskController.java
|   |   |       |-- TaskPriority.java
|   |   |       |-- TaskRepository.java
|   |   |       |-- TaskService.java
|   |   |       `-- TaskStatus.java
|   |   `-- resources/
|   |       |-- static/
|   |       |-- application.properties
|   |       |-- application-dev.properties
|   |       `-- application-file.properties
|   `-- test/
|       `-- java/com/taskmanager/api/
|           |-- TaskManagerApiApplicationTests.java
|           `-- task/
|               |-- TaskRepositoryTest.java
|               `-- TaskServiceTest.java
|-- pom.xml
|-- README.md
|-- HELP.md
|-- LICENSE
|-- mvnw
`-- mvnw.cmd
```

Pontos de entrada principais:

- Aplicacao: `TaskManagerApiApplication.main`.
- API REST: `TaskController`, rota base `/api/tasks`.
- Frontend estatico: `src/main/resources/static/index.html`.
- Swagger UI: `/swagger-ui.html`.
- H2 Console: `/h2-console` nos perfis H2 configurados.

## 4. Problemas encontrados

- Arquitetura ainda acoplada ao framework: dominio, persistencia, casos de uso e DTOs HTTP ficam no mesmo pacote de feature, sem separacao explicita de camadas de Arquitetura Limpa.
- Entidade `Task` acumula responsabilidades de dominio e persistencia JPA, alem de depender diretamente de anotacoes de infraestrutura.
- `TaskService` concentra regras de criacao, atualizacao parcial, busca, filtragem, mapeamento para DTO e acesso direto ao repository Spring Data.
- Nao ha interfaces de caso de uso, portas de entrada/saida ou contratos que isolem a regra de negocio do Spring/JPA.
- Nao ha BDD identificado: inexistem Cucumber, Gherkin, specs executaveis ou cenarios de comportamento.
- Nao ha Dockerfile nem docker-compose.
- Nao ha configuracao visivel de deploy para ambiente externo.
- Nao ha workflow de CI/CD em `.github/workflows`.
- Nao ha migracoes de banco com Flyway ou Liquibase; o projeto usa `spring.jpa.hibernate.ddl-auto=update`, pratico em desenvolvimento, mas fraco para demonstracao de evolucao controlada de schema.
- O H2 Console esta com `web-allow-others=true` nos perfis locais, o que deve ser revisto antes de qualquer ambiente publicado.
- Dados de demonstracao estao hardcoded em `DevDataLoader`, bom para demo, mas ainda sem isolamento claro por seed/script.
- README apresenta caracteres quebrados em alguns trechos, sugerindo problema de encoding em documentacao.
- Nao ha cobertura de testes de controller/API HTTP, validacoes de erro, contrato OpenAPI ou testes end-to-end da interface estatica.
- Nao ha configuracao de cobertura, qualidade estatica ou metricas de teste, como JaCoCo, Checkstyle, SpotBugs ou PMD.
- O projeto roda localmente usando Java 25 na maquina auditada, embora o `pom.xml` declare Java 17. Os testes passam, mas a demonstracao academica deve padronizar a versao do runtime.

## 5. O que ja atende ao enunciado

- Clean Code: ha nomes claros em grande parte das classes e metodos, controllers enxutos, uso de DTOs, validacoes declarativas e tratamento centralizado de excecoes.
- SOLID: existe alguma separacao de responsabilidade entre controller, service e repository; dependencias sao injetadas por construtor.
- Design Patterns: uso de Repository por Spring Data JPA, DTO, Controller/Service, Dependency Injection e Global Exception Handler.
- TDD: existem testes automatizados para service e repository, alem de smoke test de contexto Spring.
- Arquitetura em camadas: existe uma separacao inicial em controller, service, repository e entidade.
- Documentacao: README com instrucoes de execucao, endpoints e exemplos; Swagger/OpenAPI configurado.
- Banco local: H2 em memoria e H2 em arquivo para desenvolvimento/demonstracao.

## 6. O que falta para atender ao enunciado

- Clean Code: reduzir duplicacoes de mapeamento e atualizacao, padronizar mensagens/encoding e isolar dados de demo.
- SOLID: introduzir contratos de use case/portas e diminuir dependencia direta de classes de aplicacao em detalhes de infraestrutura.
- Design Patterns: demonstrar patterns de forma intencional e documentada, por exemplo Repository Port, Mapper, Use Case/Interactor, Specification para filtros ou Strategy quando houver variacao real de comportamento.
- TDD: ampliar a suite antes das refatoracoes, principalmente controller, validacoes, erros e regras de dominio.
- BDD: adicionar cenarios Gherkin/Cucumber ou alternativa equivalente para fluxos essenciais da API.
- Arquitetura Limpa: separar dominio, application/use cases, adapters/infrastructure e entrypoints, sem quebrar funcionalidade.
- Microsservicos: ainda nao ha decomposicao; deve ser considerado apenas depois de estabilizar arquitetura, testes e containerizacao.
- Docker/Docker Compose: criar Dockerfile, compose para app e banco, perfis e variaveis de ambiente.
- Deploy: definir alvo simples de deploy, health check e pipeline minimo.
- CI/CD: adicionar workflow para build/test e, depois, build de imagem/deploy.

## 7. Plano de execucao dividido em fases

### Fase 1 - Baseline de qualidade e testes

- Corrigir problemas de encoding da documentacao, se necessario.
- Adicionar testes de controller com `@WebMvcTest` ou `MockMvc`.
- Cobrir validacoes de request, respostas 404/400 e fluxo feliz dos endpoints.
- Registrar cobertura minima e comando padrao de verificacao.
- Nao mudar arquitetura ainda, apenas proteger comportamento existente.

### Fase 2 - Limpeza incremental de aplicacao

- Extrair mapeamentos de entidade/DTO para um mapper dedicado.
- Reduzir responsabilidades do `TaskService`.
- Padronizar defaults e regras de atualizacao em metodos de dominio ou componentes pequenos.
- Manter endpoints e contratos HTTP estaveis.

### Fase 3 - Preparacao para Arquitetura Limpa

- Introduzir pacotes conceituais de dominio, aplicacao e infraestrutura de forma incremental.
- Criar portas/interfaces para persistencia e casos de uso.
- Adaptar Spring Data JPA como adapter de saida, sem trocar banco ainda.
- Manter testes passando a cada pequeno passo.

### Fase 4 - BDD

- Adicionar Cucumber ou ferramenta equivalente.
- Escrever cenarios para criar, listar, atualizar, filtrar e remover tarefas.
- Conectar os cenarios a testes de API em ambiente local.

### Fase 5 - Banco e migracoes

- Introduzir Flyway ou Liquibase.
- Trocar `ddl-auto=update` por migracoes controladas.
- Preparar perfil para PostgreSQL mantendo H2 para testes locais, se fizer sentido.

### Fase 6 - Docker e Docker Compose

- Criar Dockerfile multi-stage para a API.
- Criar docker-compose com app e banco.
- Externalizar configuracoes via variaveis de ambiente.
- Documentar execucao local containerizada.

### Fase 7 - CI/CD

- Criar workflow de GitHub Actions para build e testes.
- Adicionar etapa de validacao de qualidade/cobertura.
- Depois, incluir build de imagem Docker.

### Fase 8 - Deploy

- Escolher alvo simples de deploy.
- Configurar health check, profile de producao, variaveis e banco externo.
- Publicar uma versao demonstravel.

### Fase 9 - Microsservicos, somente se ainda exigido

- Avaliar fronteiras reais de dominio.
- Extrair um servico pequeno apenas se houver justificativa academica clara.
- Demonstrar comunicacao e observabilidade basicas sem superdimensionar o projeto.

## 8. Riscos tecnicos

- Refatorar para Arquitetura Limpa sem testes suficientes pode quebrar contratos HTTP ja documentados.
- Criar microsservicos cedo demais pode aumentar complexidade sem ganho real para o escopo academico.
- Usar H2 e `ddl-auto=update` pode mascarar problemas que apareceriam em PostgreSQL ou outro banco de producao.
- Deploy sem padronizar Java, profiles e variaveis pode gerar divergencia entre ambiente local e ambiente publicado.
- Dockerizar antes de organizar configuracoes pode congelar decisoes ruins de runtime.
- A interface estatica consome a API local; mudancas em contrato HTTP podem quebrar a demo visual.
- Logs muito verbosos em testes indicam configuracao de profile/logging que pode atrapalhar CI.

## 9. Recomendacao da proxima fase

A proxima fase recomendada e a Fase 1: criar uma base de seguranca com testes de controller/API e validacoes, sem alterar arquitetura nem dependencias principais alem do que for estritamente necessario.

Escopo sugerido para uma unica execucao revisavel:

- Adicionar testes para `TaskController` cobrindo criar tarefa, buscar por id, atualizar, deletar e erro 404.
- Adicionar testes de validacao para payload invalido.
- Garantir que `.\mvnw.cmd test` continue passando.
- Nao mover pacotes, nao criar microsservicos, nao dockerizar ainda.
