# Microsservicos

## Estado atual e limite

O Trackio **nao possui microsservicos executaveis separados** nesta entrega. O sistema e um **monolito modular** Spring Boot organizado em camadas inspiradas em Arquitetura Limpa (`domain`, `application`, `infrastructure`, `presentation`).

Essa decisao e intencional e justificada para a entrega academica:

1. Antes de dividir em servicos separados, e necessario estabilizar dominio, testes, contratos, Docker, deploy e banco de producao.
2. Microsservicos prematuros adicionam complexidade operacional: rede, observabilidade, consistencia eventual, versionamento de contratos e deploy independente.
3. A estrutura modular atual ja antecipa a divisao: cada bounded context pode ser extraido sem reescrita total.

Portanto, este documento e uma **proposta arquitetural fundamentada de divisao futura**, e nao evidencia de servicos rodando.

Na defesa academica, a frase correta e: "a solucao foi dividida conceitualmente em bounded contexts e preparada para extracao progressiva, mas a implementacao entregue permanece como monolito modular por decisao tecnica".

## Bounded contexts identificados

A analise do dominio Trackio revela cinco contextos com responsabilidades coesas e baixo acoplamento:

| Servico | Responsabilidade | Dados que possui | Eventos que publica |
|---|---|---|---|
| **Auth Service** | login, registro, tokens JWT, roles, permissoes, refresh | usuarios, credenciais, sessoes | `UserAuthenticated`, `UserCreated`, `RoleChanged` |
| **Organization Service** | empresas, funcionarios, membros, equipes, departamentos, hierarquia | organizations, members, teams | `MemberAdded`, `TeamCreated`, `OrgUpdated` |
| **Task Service** | tarefas, status, prioridade, prazo, progresso, comentarios, anexos, historico | tasks, comments, attachments, activity_log | `TaskCreated`, `TaskCompleted`, `TaskOverdue`, `TaskAssigned` |
| **Report Service** | dashboard, produtividade, indicadores agregados, relatorios | snapshots agregados, materialized views | `ReportGenerated` |
| **Notification Service** | alertas in-app, e-mail, push, preferencias | notifications, channels, preferences | `NotificationDelivered`, `NotificationRead` |

## Justificativa da divisao

**Auth Service** isola seguranca e credenciais, que exigem auditoria, patch independente e politica de retencao propria.

**Organization Service** concentra dados estruturais da empresa: hierarquia, papeis, equipes e departamentos.

**Task Service** e o modulo operacional principal, recebe maior carga de escrita e e o candidato mais provavel a precisar de escala independente.

**Report Service** consulta dados consolidados e gera indicadores sem sobrecarregar o fluxo transacional.

**Notification Service** evolui notificacoes de forma independente, com filas, templates, rate limiting e canais diferentes.

## Comunicacao proposta

- **Sincrona (REST)** para consultas entre contextos quando latencia importa, por exemplo `Task Service` consultando responsaveis no `Organization Service`.
- **Assincrona (events)** para reacoes desacopladas, por exemplo `TaskCompleted` atualizando relatorios e gerando notificacoes.
- **API Gateway** unico na entrada com roteamento por prefixo (`/auth`, `/orgs`, `/tasks`, `/reports`, `/notifications`).

## Como o monolito atual prepara a extracao

A separacao em `domain`, `application`, `infrastructure` e `presentation` ajuda extracao futura porque:

- regras de aplicacao ja estao em use cases independentes;
- persistencia ja passa por uma porta (`TaskRepository`);
- controllers ficam em presentation;
- DTOs separam contrato HTTP do dominio;
- paginacao usa tipos proprios (`PageQuery`/`PageResult`), sem amarrar use cases a Spring Data;
- adapter JPA pode ser substituido por adapter HTTP cliente quando um servico for extraido.

Essa preparacao evita uma extracao artificial. Em vez de criar varios projetos pequenos sem necessidade real, o Trackio primeiro estabiliza regras, testes, contratos HTTP, Docker e persistencia. A separacao fisica pode ser feita quando houver motivo tecnico: escala independente, times separados, SLA diferente, necessidade de isolamento de dados ou exigencia academica explicita.

## Limite explicito desta entrega

Esta documentacao nao deve ser lida como afirmacao de que o projeto ja tem microsservicos. O correto e afirmar que:

- existe uma modelagem detalhada de microsservicos com bounded contexts, comunicacao e dados;
- a implementacao atual e monolito modular preparado para extracao;
- a divisao em servicos e uma evolucao planejada e justificada;
- o Docker Compose atual sobe API + PostgreSQL, nao varios microsservicos de negocio;
- a extracao deve ocorrer apenas quando houver necessidade real ou exigencia academica especifica.

## Proximos passos para uma futura fase

1. Documentar contratos OpenAPI por servico.
2. Validar bounded contexts com Event Storming.
3. Extrair primeiro o Notification Service, por ser naturalmente assincrono.
4. Adicionar banco por servico somente em fase madura.
5. Introduzir API Gateway apenas quando houver mais de um servico real em producao.
6. Adicionar observabilidade distribuida antes de extrair o segundo servico.
