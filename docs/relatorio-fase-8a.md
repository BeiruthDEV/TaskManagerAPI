# Relatorio da Fase 8A - Modelagem da Divisao em Microsservicos

## Resumo da fase

A Fase 8A criou a modelagem tecnica da divisao futura do TaskManagerAPI em microsservicos. Nenhum microsservico real foi implementado nesta etapa.

O projeto permanece como monolito modular organizado em Clean Architecture, com API principal em `/api/tasks`, testes automatizados e Docker Compose para execucao da API.

Foram criados dois documentos:

- `docs/microsservicos.md`
- `docs/relatorio-fase-8a.md`

## Motivo de nao quebrar o monolito nesta etapa

A quebra imediata do monolito nao seria recomendada neste momento porque o dominio atual ainda e pequeno e concentrado em tarefas.

Os conceitos de projeto e usuario aparecem no modelo atual como campos textuais:

- `projectName`
- `assignee`

Isso indica potenciais contextos futuros, mas ainda nao representa agregados maduros o suficiente para justificar servicos independentes.

Outros fatores de risco:

- ainda nao ha autenticacao implementada;
- ainda nao ha gateway;
- ainda nao ha mensageria;
- ainda nao ha banco externo por servico;
- ainda nao ha contratos entre servicos;
- a extracao aumentaria complexidade operacional;
- a suite atual protege o monolito, mas ainda nao cobre integracoes distribuidas.

Por isso, a abordagem escolhida foi documentar a decomposicao e preparar uma migracao incremental.

## Divisao proposta

A modelagem proposta considera os seguintes microsservicos:

| Microsservico | Papel na arquitetura futura |
| --- | --- |
| `api-gateway` | Entrada unica, roteamento e politicas transversais. |
| `task-service` | Gerenciamento de tarefas, status, prioridade, progresso e prazo. |
| `project-service` | Gerenciamento de projetos, quadros e membros. |
| `user-service` | Dados cadastrais, perfis e preferencias de usuarios. |
| `notification-service` | Notificacoes geradas por eventos de tarefas. |
| `auth-service` | Autenticacao, autorizacao e emissao de tokens. |

O primeiro candidato natural para extracao futura e o `task-service`, porque o monolito atual ja possui camadas organizadas em torno de tarefas:

- `domain/model/Task`
- `application/usecase`
- `application/port/TaskRepository`
- `infrastructure/persistence`
- `presentation/task`

## Bounded contexts sugeridos

Foram definidos os seguintes limites de contexto:

- `Task Management`: tarefas e ciclo de vida da tarefa.
- `Project Collaboration`: projetos, quadros e membros.
- `User Profile`: usuarios e dados cadastrais.
- `Identity and Access`: login, tokens, roles e permissoes.
- `Notification`: notificacoes e entrega de mensagens.

Esses contextos permitem evoluir a solucao sem misturar responsabilidades de negocio.

## Comunicacao proposta

### REST sincrono

REST e indicado para validacoes e consultas imediatas:

- gateway roteando chamadas para servicos internos;
- `task-service` consultando `project-service` para validar projeto;
- `task-service` consultando `user-service` para validar responsavel;
- `project-service` consultando `user-service` para validar membros.

### Eventos assincronos

Eventos sao indicados apenas quando uma acao nao precisa bloquear a resposta ao usuario.

Eventos propostos:

- `TaskCreated`
- `TaskUpdated`
- `TaskAssigned`
- `TaskCompleted`
- `TaskDeleted`

Consumidor inicial sugerido:

- `notification-service`

Esses eventos nao foram implementados nesta fase.

## Relacao com o criterio de microsservicos da prova

Esta fase contribui para o criterio de microsservicos ao demonstrar:

- analise do dominio atual;
- proposta de divisao por responsabilidades;
- definicao de bounded contexts;
- comunicacao sincrona e assincrona planejada;
- propriedade de dados por servico;
- estrategia incremental de migracao;
- justificativa tecnica para evitar distribuicao prematura.

A evidencia desta fase e arquitetural/documental. A implementacao real pode ser feita depois, em uma fase menor e revisavel.

## Como a arquitetura atual ajuda

A Clean Architecture aplicada nas fases anteriores reduz o acoplamento e facilita uma extracao futura porque:

- dominio nao depende de presentation ou infrastructure;
- application depende de porta `TaskRepository`;
- infrastructure implementa detalhes de persistencia;
- presentation concentra API HTTP;
- casos de uso ja representam operacoes claras do negocio.

Essa separacao cria uma fronteira mais nitida para uma futura extracao do `task-service`.

## Riscos tecnicos

- Extrair servicos cedo demais pode criar complexidade sem ganho funcional.
- Chamadas REST entre muitos servicos podem gerar acoplamento temporal e falhas em cadeia.
- Eventos assincronos exigem idempotencia, rastreabilidade e observabilidade.
- Banco por servico exige migracoes, consistencia eventual e novas estrategias de teste.
- Um gateway mal definido pode concentrar regra de negocio indevida.
- Separar `auth-service` e `user-service` exige cuidado com dados sensiveis e responsabilidades.
- A interface estatica atual pode precisar ser adaptada quando houver gateway ou multiplos servicos.

## Proximos passos recomendados

A proxima fase recomendada e uma Fase 8B pequena, com escopo limitado.

Opcao mais segura:

- criar contratos propostos para os servicos em documentacao ou OpenAPI;
- manter o monolito funcionando;
- nao mover codigo ainda;
- preparar os endpoints esperados de `task-service`, `project-service` e `user-service`.

Opcao com evidencia executavel, se exigida pela avaliacao:

- criar uma extracao minima/simulada apenas do `task-service`;
- preservar os endpoints atuais;
- validar via testes e Docker Compose;
- evitar banco por servico nesta primeira extracao.

## Arquivos alterados

- `docs/microsservicos.md`
- `docs/relatorio-fase-8a.md`

## Validacao

Como esta fase alterou apenas documentacao, nao houve necessidade tecnica de executar a suite de testes. Nenhum arquivo de producao, teste, Maven ou Docker foi alterado.
