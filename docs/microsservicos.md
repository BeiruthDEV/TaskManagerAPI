# Modelagem de Microsservicos

## Visao geral

O estado atual do TaskManagerAPI e um monolito modular organizado em camadas de Clean Architecture. A aplicacao possui um unico processo Spring Boot, um unico deploy e uma API publica principal em `/api/tasks`.

Esta documentacao nao afirma que microsservicos ja foram implementados. Ela apresenta uma proposta tecnica de decomposicao futura para demonstrar o criterio academico de microsservicos de forma responsavel e incremental.

A divisao proposta considera o dominio de uma plataforma de gerenciamento de tarefas para equipes pequenas, com evolucao natural para projetos, usuarios, autenticacao e notificacoes.

## Estado atual

O monolito atual contem:

- dominio central de tarefas;
- casos de uso de criacao, listagem, busca, atualizacao e exclusao;
- persistencia JPA via H2;
- API REST em `/api/tasks`;
- interface estatica servida pelo proprio Spring Boot;
- testes unitarios, BDD e Docker Compose.

Campos atuais que indicam possiveis contextos futuros:

- `assignee`: aponta para usuario/responsavel;
- `projectName`: aponta para projeto/quadro;
- `status`, `priority` e `progress`: pertencem ao ciclo de vida da tarefa;
- `createdAt` e `updatedAt`: podem disparar eventos de auditoria/notificacao em fases futuras.

## Arquitetura proposta

Diagrama textual da arquitetura alvo:

```text
Clientes Web/Mobile
        |
        v
   api-gateway
        |
        +------------------ REST ------------------+
        |                                          |
        v                                          v
 task-service                               project-service
        |                                          |
        +------------------ REST ------------------+
        |                                          |
        v                                          v
  user-service                               auth-service
        |
        +------------- eventos assincronos --------+
                                                 |
                                                 v
                                      notification-service

Cada servico deve evoluir para possuir seu proprio banco de dados.
```

## Bounded contexts propostos

### Task Management

Responsavel por tarefas, status, prioridade, progresso, prazo e regras do ciclo de vida da tarefa.

Servico candidato: `task-service`.

### Project Collaboration

Responsavel por projetos, quadros, agrupamentos de tarefas e participacao de usuarios em projetos.

Servico candidato: `project-service`.

### Identity and Access

Responsavel por autenticacao, autorizacao, emissao de tokens e politicas de acesso.

Servicos candidatos: `auth-service` e parte do `user-service`.

### User Profile

Responsavel por dados cadastrais dos usuarios, preferencias e informacoes de perfil.

Servico candidato: `user-service`.

### Notification

Responsavel por notificacoes causadas por eventos do dominio, como criacao, atualizacao, atribuicao e conclusao de tarefas.

Servico candidato: `notification-service`.

## Tabela de servicos propostos

| Microsservico | Responsabilidade | Dados principais | Endpoints esperados | Comunicacao com outros servicos |
| --- | --- | --- | --- | --- |
| `api-gateway` | Entrada unica da solucao, roteamento, agregacao simples de respostas e aplicacao de politicas transversais. | Rotas, configuracoes de seguranca, metadados de roteamento. | `GET /api/tasks/**`, `GET /api/projects/**`, `GET /api/users/**`, `POST /api/auth/login`. | Encaminha chamadas REST para os servicos internos. Pode validar token emitido pelo `auth-service`. |
| `task-service` | Gerenciar tarefas, status, prioridade, progresso, prazo e atribuicao logica. | Task, status, priority, progress, dueDate, assigneeId, projectId, timestamps. | `GET /tasks`, `GET /tasks/{id}`, `POST /tasks`, `PUT /tasks/{id}`, `DELETE /tasks/{id}`, `GET /tasks/filter`. | Consulta `project-service` para validar projeto e `user-service` para validar responsavel, se necessario. Publica eventos para `notification-service`. |
| `project-service` | Gerenciar projetos, quadros e vinculo de usuarios com projetos. | Project, board, membership, project settings. | `GET /projects`, `GET /projects/{id}`, `POST /projects`, `PUT /projects/{id}`, `DELETE /projects/{id}`, `GET /projects/{id}/members`. | Consulta `user-service` para validar membros. Pode ser consultado pelo `task-service`. |
| `user-service` | Gerenciar usuarios, perfis e dados cadastrais. | User, profile, email, displayName, preferences. | `GET /users`, `GET /users/{id}`, `POST /users`, `PUT /users/{id}`, `GET /users/{id}/profile`. | Fornece dados para `task-service`, `project-service` e `notification-service`. Nao deve emitir tokens. |
| `notification-service` | Enviar notificacoes sobre criacao, atualizacao, atribuicao e conclusao de tarefas. | Notification, notificationTemplate, deliveryStatus, userChannel. | `GET /notifications`, `GET /notifications/{id}`, `POST /notifications/test`, `PUT /notifications/{id}/read`. | Consome eventos publicados por `task-service` e consulta `user-service` para dados de destino. |
| `auth-service` | Autenticar usuarios, autorizar acesso e emitir tokens. | Credentials, roles, permissions, refresh tokens, sessions. | `POST /auth/login`, `POST /auth/refresh`, `POST /auth/logout`, `GET /auth/me`. | Valida credenciais associadas ao usuario. Fornece tokens usados pelo `api-gateway` e pelos servicos internos. |

## Responsabilidades detalhadas

### api-gateway

O `api-gateway` seria a porta de entrada da plataforma. Ele evitaria que clientes precisem conhecer enderecos de servicos internos.

Responsabilidades:

- roteamento para servicos internos;
- validacao basica de autenticacao;
- padronizacao de headers;
- agregacao simples quando necessario;
- ponto unico para CORS, rate limit e logs de borda em fases futuras.

Nao deve conter regra de negocio de tarefas, projetos ou usuarios.

### task-service

O `task-service` e o primeiro candidato natural para extracao, porque o monolito atual ja tem uma camada de dominio e application centrada em tarefas.

Responsabilidades:

- criar tarefas;
- listar tarefas;
- buscar tarefa por id;
- atualizar tarefa;
- excluir tarefa;
- filtrar por status e prioridade;
- controlar status, prioridade, progresso e prazo;
- publicar eventos de dominio em fases futuras.

Dados:

- `Task`;
- `TaskStatus`;
- `TaskPriority`;
- `progress`;
- `dueDate`;
- referencias futuras para `projectId` e `assigneeId`.

### project-service

O `project-service` separaria a ideia de projeto do campo textual `projectName` existente atualmente.

Responsabilidades:

- cadastrar projetos;
- gerenciar membros do projeto;
- organizar quadros/listas;
- fornecer dados de projeto para tarefas.

Dados:

- projeto;
- quadro;
- membros;
- configuracoes do projeto.

### user-service

O `user-service` substituiria o uso de responsavel como texto livre (`assignee`) por uma referencia controlada a usuario.

Responsabilidades:

- cadastrar usuarios;
- manter dados de perfil;
- fornecer dados de responsavel para tarefas e notificacoes.

Dados:

- usuario;
- nome de exibicao;
- email;
- preferencias de notificacao.

### notification-service

O `notification-service` deve ser extraido somente quando houver eventos reais a notificar.

Responsabilidades:

- consumir eventos de tarefas;
- criar notificacoes;
- controlar status de entrega/leitura;
- enviar mensagens por canais futuros.

Dados:

- notificacao;
- template;
- canal;
- status de entrega.

### auth-service

O `auth-service` deve ser separado do `user-service` para manter autenticacao/autorizacao isoladas de dados cadastrais.

Responsabilidades:

- autenticar credenciais;
- emitir tokens;
- renovar tokens;
- invalidar sessoes;
- definir roles e permissoes.

Dados:

- credenciais;
- roles;
- permissoes;
- tokens;
- sessoes.

## Comunicacao entre servicos

### Comunicacao REST sincrona

REST sincrono faz sentido para consultas e validacoes imediatas:

- `api-gateway` chama `task-service`, `project-service`, `user-service` e `auth-service`;
- `task-service` pode consultar `project-service` para validar se um projeto existe;
- `task-service` pode consultar `user-service` para validar se um responsavel existe;
- `project-service` pode consultar `user-service` para validar membros.

Essa comunicacao deve ser usada com cuidado para evitar cadeias longas de chamadas.

### Comunicacao assincrona por eventos

Eventos assincronos fazem sentido quando uma acao nao precisa bloquear a resposta ao usuario:

- `TaskCreated`;
- `TaskUpdated`;
- `TaskAssigned`;
- `TaskCompleted`;
- `TaskDeleted`.

O principal consumidor inicial seria o `notification-service`.

Nesta fase, eventos sao apenas uma proposta de evolucao. Eles nao foram implementados.

## Dados por servico

Em uma arquitetura de microsservicos madura, cada servico deve possuir seus proprios dados e nao compartilhar tabelas diretamente.

Proposta futura:

- `task-service`: banco de tarefas;
- `project-service`: banco de projetos e membros;
- `user-service`: banco de usuarios e perfis;
- `auth-service`: banco de credenciais, roles e sessoes;
- `notification-service`: banco de notificacoes;
- `api-gateway`: sem banco de dominio, no maximo configuracao/cache tecnico.

O estado atual ainda usa H2 em memoria no monolito. A separacao fisica de bancos deve ocorrer apenas depois da separacao logica e da criacao de contratos estaveis.

## Como a Clean Architecture atual ajuda a extracao

A organizacao atual em `domain`, `application`, `infrastructure` e `presentation` facilita a futura extracao porque:

- regras de tarefa estao concentradas no dominio e nos casos de uso;
- a persistencia esta isolada por uma porta `TaskRepository`;
- o controller HTTP esta separado da regra de aplicacao;
- o mapper JPA separa modelo de dominio de entidade de banco;
- a fachada `TaskService` preserva um ponto de entrada simples para a camada HTTP.

Com isso, uma extracao futura do `task-service` pode reaproveitar boa parte do nucleo de tarefa sem levar junto todo o restante do monolito.

## Por que nao quebrar o monolito agora

Uma extracao direta neste momento seria arriscada porque:

- o dominio ainda e pequeno e centrado em tarefas;
- `assignee` e `projectName` ainda sao campos textuais, nao agregados separados;
- nao ha autenticacao real implementada;
- nao ha banco externo por servico;
- nao ha mensageria configurada;
- nao ha gateway implementado;
- a complexidade operacional aumentaria antes de existir necessidade tecnica real;
- os testes atuais validam bem o monolito, mas ainda nao cobrem contratos entre servicos.

Por isso, a decisao mais segura e manter o monolito modular e documentar a decomposicao para uma migracao incremental.

## Estrategia de migracao incremental

### Etapa 1 - Monolito modular atual

Manter a estrutura Clean Architecture existente e continuar fortalecendo testes, documentacao e Docker.

Estado atual:

- um unico Spring Boot;
- um unico deploy;
- API `/api/tasks`;
- H2 em memoria;
- Docker Compose com servico unico da API.

### Etapa 2 - Separacao logica por contexto

Evoluir o modelo sem criar processos separados:

- substituir `projectName` por conceito interno de projeto;
- substituir `assignee` textual por conceito interno de usuario;
- criar contratos internos para projeto e usuario;
- manter tudo no monolito enquanto os limites amadurecem.

### Etapa 3 - Extracao do primeiro servico

Extrair primeiro o `task-service`, porque ele ja esta mais bem delimitado.

Passos possiveis:

- criar modulo ou aplicacao separada para tarefas;
- mover domain/application/infrastructure/presentation de tarefas;
- manter endpoints equivalentes aos atuais;
- preservar testes unitarios e BDD;
- validar API via Docker Compose.

### Etapa 4 - Comunicacao entre servicos

Adicionar REST sincrono para consultas necessarias:

- `task-service` consulta `project-service`;
- `task-service` consulta `user-service`;
- `api-gateway` roteia as chamadas publicas.

Eventos devem ser introduzidos apenas quando houver consumidor real, como notificacoes.

### Etapa 5 - Banco por servico

Migrar de H2/monolito para bancos separados:

- PostgreSQL para `task-service`;
- PostgreSQL ou outro banco relacional para `project-service` e `user-service`;
- banco proprio para notificacoes;
- migracoes versionadas por servico.

Essa etapa deve ser feita depois de contratos e testes estarem estaveis.

## Justificativa tecnica

A proposta atende ao criterio academico de microsservicos porque demonstra:

- identificacao de bounded contexts;
- responsabilidade clara por servico;
- estrategia de comunicacao;
- propriedade de dados por servico;
- plano de migracao incremental;
- consciencia dos riscos de distribuicao prematura.

Ao mesmo tempo, preserva a qualidade atual do projeto ao nao fragmentar uma aplicacao ainda pequena antes de haver necessidade tecnica concreta.

## Proximos passos

A proxima fase recomendada e uma Fase 8B pequena, ainda controlada, com uma das seguintes alternativas:

- criar uma simulacao documental/estrutural de servicos sem mover codigo real;
- criar contratos OpenAPI propostos para `task-service`, `project-service` e `user-service`;
- preparar um perfil de Docker Compose futuro com nomes de servicos planejados;
- extrair apenas um esqueleto minimo de `task-service`, se a avaliacao exigir evidencia executavel de microsservicos.
