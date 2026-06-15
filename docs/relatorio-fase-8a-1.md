# Relatorio da Fase 8A.1 - Frontend funcional e reducao de mocks

## Resumo da fase

A Fase 8A.1 simplificou a tela principal de tarefas para deixar o frontend mais funcional, objetivo e alinhado ao estado real do backend.

O foco passou a ser:

- resumo de tarefas;
- busca;
- filtro por status;
- tabela unica;
- criacao, edicao e exclusao de tarefas usando a API real.

Nao foram alterados endpoints, payloads, regras de negocio, testes, Dockerfile ou `docker-compose.yml`.

## Elementos mockados ou decorativos encontrados

Foram identificados elementos de interface que sugeriam funcionalidades ainda nao implementadas no backend:

- menus de Payrolls;
- menus de Employees;
- menus de Invoices;
- menus de Recruitment & Hiring;
- menus de Integration;
- menu de Help & Center;
- menu de Performance;
- item de Calendar;
- card de Announcement;
- botoes de topo para informacoes, mensagens e notificacoes;
- perfil fixo de usuario;
- alternancia de visualizacao List/Kanban/Calendar;
- subtarefas na sidebar por status.

Esses elementos deixavam a tela com aparencia de produto maior do que o sistema atual realmente entrega.

## Elementos removidos ou ocultados

Foram removidos da tela principal:

- menus laterais sem funcionalidade real;
- card de anuncio;
- busca global duplicada;
- botoes de topo sem acao real;
- perfil fixo;
- visualizacoes Kanban e Calendar;
- multiplas tabelas agrupadas por status;
- botoes de "View All" e collapse por grupo;
- acao rapida de concluir fora do formulario.

A sidebar foi reduzida para:

- Dashboard;
- Tasks;
- Projects;
- Settings.

Somente `Tasks` permanece ativo. Os demais itens ficam visualmente desabilitados, para nao simular navegacao inexistente.

## Integracoes feitas com a API

Foi criada uma camada simples de comunicacao em:

- `src/main/resources/static/task-api.js`

Ela centraliza as chamadas para:

- `GET /api/tasks?page=0&size=100`
- `POST /api/tasks`
- `PUT /api/tasks/{id}`
- `DELETE /api/tasks/{id}`

A tela carrega dados reais da API e aplica busca/filtro no frontend sobre a lista retornada.

## Telas e componentes alterados

Arquivos alterados:

- `src/main/resources/static/index.html`
- `src/main/resources/static/app.js`
- `src/main/resources/static/styles.css`

Arquivo criado:

- `src/main/resources/static/task-api.js`

Documentacao criada:

- `docs/relatorio-fase-8a-1.md`

## Melhorias visuais aplicadas

A tela foi simplificada para uma estrutura mais enxuta:

```text
Topo:
Tasks
Gerencie as tarefas do time

Resumo:
Total | Pendentes | Em andamento | Concluidas

Area principal:
Busca | Filtro por status | Nova tarefa

Tabela:
Titulo | Status | Prioridade | Progresso | Acoes
```

Tambem foram adicionados estados visuais para:

- carregando;
- erro ao buscar tarefas;
- lista vazia;
- lista com tarefas.

## Funcionalidades que ficaram ativas

Permanecem ativas porque possuem suporte real no backend:

- listar tarefas;
- buscar localmente por titulo, descricao, status ou prioridade;
- filtrar por status;
- criar tarefa;
- editar tarefa;
- excluir tarefa;
- visualizar resumo por status.

## Funcionalidades desabilitadas por falta de backend

Ficaram apenas como itens desabilitados na sidebar:

- Dashboard;
- Projects;
- Settings.

Nao foram criadas telas ou fluxos falsos para essas areas.

## Validacoes executadas

Checagem sintatica dos arquivos JavaScript:

```powershell
node --check src/main/resources/static/app.js
node --check src/main/resources/static/task-api.js
```

Resultado:

- sucesso;
- sem erros de sintaxe.

Testes do backend:

```powershell
.\mvnw.cmd test
```

Resultado:

- Build: sucesso;
- Testes executados: 32;
- Falhas: 0;
- Erros: 0;
- Ignorados: 0.

Validacao via Docker Compose:

```powershell
docker compose up --build -d
```

Resultado:

- imagem buildada com sucesso;
- container `taskmanager-api` iniciado com sucesso;
- aplicacao disponivel em `http://localhost:8081`.

Validacoes HTTP:

- `GET http://localhost:8081/`: retornou o HTML atualizado;
- `GET http://localhost:8081/task-api.js`: status 200;
- `GET http://localhost:8081/app.js`: status 200;
- `GET http://localhost:8081/styles.css`: status 200;
- `GET http://localhost:8081/api/tasks`: retornou dados reais da API.

Foi tentada uma validacao visual com Playwright via ambiente automatizado, mas o pacote `playwright` nao esta instalado no runtime disponivel. Por isso, a validacao visual automatizada ficou registrada como limitacao.

## Limitacoes

- Ainda nao existe comando separado de build/test para frontend, pois o frontend e estatico e servido pelo Spring Boot.
- Dashboard, Projects e Settings ainda nao possuem backend/telas reais.
- A interface ainda usa H2 em memoria com dados carregados pelo backend no perfil `dev`.
- Nao foi criada autenticacao.
- Nao foi criada navegacao real entre modulos.
- Nao foi implementado Kanban ou calendario, pois isso nao faz parte do suporte atual da API.

## Proxima fase recomendada

A proxima fase recomendada e criar uma fase pequena de validacao de contrato HTTP da camada `presentation`, com testes para a API real de tarefas:

- listagem;
- criacao;
- atualizacao;
- exclusao;
- validacoes 400;
- erro 404.

Outra alternativa pequena e criar documentacao visual da tela atual, registrando quais funcionalidades estao implementadas e quais permanecem planejadas.
