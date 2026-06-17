# Checklist da Entrega Academica

Este checklist reflete o estado real do repositorio nesta fase. A classificacao diferencia:

- **Atendido**: requisito implementado ou documentado diretamente no projeto.
- **Atendido com justificativa**: requisito demonstrado com limite tecnico assumido e defendido na documentacao.
- **Pendente**: ainda nao existe nesta versao ou depende da fase de deploy.

## Atendido

- [x] Descricao do problema escolhido.
- [x] Proposta de solucao do Trackio.
- [x] API REST para o modulo de tarefas.
- [x] Frontend estatico funcional para tarefas reais.
- [x] Dados de demonstracao documentados em `docs/dados-demo.md`.
- [x] DTOs de request/response.
- [x] Validacao de entrada.
- [x] Tratamento global de excecoes.
- [x] Swagger/OpenAPI.
- [x] Repository Pattern.
- [x] Adapter Pattern.
- [x] Mapper Pattern.
- [x] Factory Pattern.
- [x] Facade/Service Pattern.
- [x] DTO Pattern.
- [x] Dependency Injection.
- [x] Testes unitarios de dominio, factory, use cases e service.
- [x] Teste de repository JPA.
- [x] Testes de controller/API HTTP com MockMvc.
- [x] Cenarios BDD com Cucumber.
- [x] Suite automatizada com 55 testes verdes.
- [x] Dockerfile.
- [x] Docker Compose com API e PostgreSQL.
- [x] Volume persistente para PostgreSQL.
- [x] Profiles `dev`, `test`, `docker` e `prod`.
- [x] `.env.example` para execucao local com Docker.
- [x] Documentacao academica em `docs/`.

## Atendido com justificativa

- [x] **Arquitetura Limpa**: o projeto e um monolito modular com camadas `domain`, `application`, `infrastructure` e `presentation`. A application nao usa mais `Page`/`Pageable` do Spring Data; usa `PageQuery`/`PageResult` proprios. O uso de `@Service`/`@Transactional` foi mantido por pragmatismo do Spring e esta documentado em `docs/arquitetura.md`.
- [x] **SOLID**: SRP aparece na separacao controller/DTO/use case/adapter/mapper/handler; DIP aparece na porta `TaskRepository`; ISP aparece nos use cases pequenos; DI e usada por construtor. Limitacoes e evolucoes estao documentadas em `docs/solid.md`.
- [x] **TDD**: o backend possui 55 testes automatizados e as refatoracoes principais foram feitas protegidas por testes. O projeto nao promete um historico completo de todos os ciclos red/green/refactor, mas documenta o fluxo de teste/refatoracao usado em `docs/tdd.md`.
- [x] **Clean Code**: ha nomes claros, DTOs, use cases, mapper, exception handler, validacoes e separacao de responsabilidades. O dominio simples fica como evolucao futura, documentada em `docs/clean-code.md`.
- [x] **Microsservicos**: nao existem microsservicos executaveis nesta versao. O requisito e defendido por modelagem de bounded contexts, decisao de monolito modular e plano de extracao progressiva em `docs/microsservicos.md`.
- [x] **Dados demo / empresa ficticia**: a empresa Atlas Solucoes Empresariais, seus setores, responsaveis e projetos sao ficticios e servem apenas para demonstracao. Origem e reset estao em `docs/dados-demo.md`.
- [x] **Docker**: atende ao ambiente local com PostgreSQL e volume persistente. A evolucao natural e adicionar migrations com Flyway ou Liquibase.

## Pendente

- [ ] Deploy publico em servidor ou plataforma cloud.
- [ ] Link de acesso publicado.
- [ ] Evidencias do deploy: URL, prints/logs e endpoints validados.
- [ ] Flyway ou Liquibase para migrations.
- [ ] Configuracao segura de credenciais fora do ambiente local.
- [ ] Testes automatizados do frontend.
- [ ] Autenticacao.
- [ ] Organizacao/empresa real como modulo cadastral.
- [ ] Funcionarios/membros como modulo cadastral.
- [ ] Equipes/departamentos como modulo cadastral.
- [ ] Projetos como modulo proprio.
- [ ] Comentarios, anexos e historico.
- [ ] Microsservicos executaveis separados, somente se a avaliacao exigir evidencia pratica alem da proposta arquitetural.

## Proximos passos

1. Fazer deploy final com profile `prod` e PostgreSQL persistente.
2. Registrar o link publico no README e em `docs/deploy.md`.
3. Validar `/`, `/api/tasks` e Swagger no ambiente publicado.
4. Adicionar evidencias de deploy para a entrega.
5. Avaliar se a banca exige microsservicos executaveis ou se a proposta arquitetural documentada e suficiente.
6. Planejar migrations com Flyway ou Liquibase.
