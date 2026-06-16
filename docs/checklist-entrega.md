# Checklist da Entrega Academica

Este checklist reflete o estado real do repositorio nesta fase. Ele separa o que ja esta implementado, o que esta parcialmente atendido e o que ainda falta para a entrega final.

## Concluido

- [x] Descricao do problema escolhido.
- [x] Proposta de solucao do Trackio.
- [x] API REST para o modulo de tarefas.
- [x] Frontend estatico funcional para tarefas reais.
- [x] Estrutura em camadas inspirada em Arquitetura Limpa.
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
- [x] Dockerfile.
- [x] Docker Compose com API e PostgreSQL.
- [x] Volume persistente para PostgreSQL.
- [x] Profiles `dev`, `test`, `docker` e `prod`.
- [x] `.env.example` para execucao local com Docker.
- [x] Documentacao academica em `docs/`.

## Parcial

- [ ] Clean Code: boa base com nomes claros, DTOs, use cases e handlers, mas ainda pode evoluir em regras de dominio mais ricas e menor acoplamento com framework.
- [ ] SOLID: aplicado em parte por separacao de responsabilidades e porta de repository, mas `application` ainda usa anotacoes Spring e tipos do Spring Data.
- [ ] Arquitetura Limpa: estrutura existe e esta organizada, mas ainda nao esta totalmente independente de framework.
- [ ] TDD: ha boa cobertura automatizada, mas nem todo comportamento futuro nasceu estritamente por ciclo TDD documentado.
- [ ] Microsservicos: existe proposta arquitetural documentada, mas a aplicacao atual continua sendo um monolito modular.
- [ ] Docker: atende bem ao ambiente local com PostgreSQL, mas ainda falta estrategia madura de migrations e configuracao real de producao.
- [ ] Deploy: documentado e planejado, mas ainda nao executado/publicado.

## Pendente

- [ ] Deploy publico em servidor ou plataforma cloud.
- [ ] Link de acesso publicado.
- [ ] Evidencias do deploy: URL, prints/logs e endpoints validados.
- [ ] Flyway ou Liquibase para migrations.
- [ ] Configuracao segura de credenciais fora do ambiente local.
- [ ] Testes automatizados do frontend.
- [ ] Autenticacao.
- [ ] Organizacao/empresa.
- [ ] Funcionarios/membros.
- [ ] Equipes/departamentos.
- [ ] Projetos como modulo proprio.
- [ ] Kanban funcional.
- [ ] Dashboard funcional.
- [ ] Comentarios, anexos e historico.
- [ ] Microsservicos executaveis separados, somente se a avaliacao exigir evidencia pratica alem da proposta arquitetural.

## Proximos passos

1. Fazer deploy final com profile `prod` e PostgreSQL persistente.
2. Registrar o link publico no README e em `docs/deploy.md`.
3. Validar `/`, `/api/tasks` e Swagger no ambiente publicado.
4. Adicionar evidencias de deploy para a entrega.
5. Avaliar se a banca exige microsservicos executaveis ou se a proposta arquitetural documentada e suficiente.
6. Planejar migrations com Flyway ou Liquibase.
