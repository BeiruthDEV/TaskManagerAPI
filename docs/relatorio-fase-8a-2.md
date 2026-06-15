# Relatorio da Fase 8A.2 - Sidebar com roadmap de modulos futuros

## 1. Resumo da fase

A Fase 8A.2 reintroduziu a visao futura de plataforma empresarial na sidebar sem recriar a navegacao poluida antiga e sem apresentar funcionalidades inexistentes como prontas.

A tela de Tasks continua sendo a area funcional principal, consumindo dados reais de `/api/tasks`. Os modulos futuros foram agrupados em uma secao separada chamada `Roadmap`, com badge discreto `Em breve` e estado de placeholder para indicar que ainda nao estao disponiveis nesta versao.

## 2. Modulos ativos

- `Tasks`: modulo funcional principal, com tabela, resumo, busca, filtro, criacao, edicao e exclusao usando a API real.
- `Dashboard`, `Projects` e `Settings`: mantidos como itens principais de `Core`, ainda desabilitados para nao simular telas prontas.

## 3. Modulos planejados

Secao `Roadmap`:

- `Calendar`
- `Performance`
- `Employees`
- `Invoices`
- `Payrolls`
- `Recruitment & Hiring`
- `Integration`
- `Help & Center`

Todos aparecem com badge `Em breve`.

## 4. Diferenciacao entre funcionalidade pronta e roadmap

A UI separa os itens em duas secoes:

- `Core`: navegacao principal do produto.
- `Roadmap`: modulos planejados para evolucao futura.

Os itens de roadmap:

- usam opacidade menor;
- exibem badge `Em breve`;
- nao abrem telas mockadas;
- nao criam dados fake;
- ao clicar, exibem um estado unico de `Modulo em desenvolvimento`.

O placeholder inclui:

- nome do modulo;
- status `Em breve`;
- descricao curta do proposito do modulo;
- aviso de que o modulo ainda nao esta disponivel nesta versao.

## 5. Arquivos alterados

- `src/main/resources/static/index.html`
  - adicionada a divisao `Core` e `Roadmap` na sidebar;
  - adicionados os oito modulos planejados;
  - criado painel simples para modulo em desenvolvimento.

- `src/main/resources/static/app.js`
  - adicionados metadados dos modulos planejados;
  - adicionada navegacao de estado entre Tasks e placeholders de roadmap;
  - preservado o carregamento real de tarefas por `TaskApi.list()`.

- `src/main/resources/static/styles.css`
  - ajustada a largura e rolagem da sidebar;
  - adicionados estilos discretos para itens de roadmap e badges;
  - adicionado estilo do placeholder de modulo em desenvolvimento.

- `docs/relatorio-fase-8a-2.md`
  - documentacao da fase.

## 6. Validacoes executadas

### Sintaxe JavaScript

```powershell
node --check src/main/resources/static/app.js
node --check src/main/resources/static/task-api.js
```

Resultado:

- `app.js`: OK
- `task-api.js`: OK

### Testes automatizados do backend

```powershell
.\mvnw.cmd test
```

Resultado:

- `Tests run: 32`
- `Failures: 0`
- `Errors: 0`
- `Skipped: 0`
- `BUILD SUCCESS`

### Docker Compose

Comando solicitado:

```powershell
docker compose down
docker compose build --no-cache
docker compose up -d
```

Resultado:

- bloqueado porque o Docker Desktop daemon nao estava ativo;
- erro observado: `failed to connect to the docker API at npipe:////./pipe/dockerDesktopLinuxEngine`;
- `com.docker.service` estava `Stopped`;
- tentativa de `Start-Service com.docker.service` falhou por permissao do Windows.

Para continuar a validacao HTTP, a aplicacao foi iniciada localmente em `8081` com Spring Boot.

### Endpoints HTTP

```powershell
GET http://localhost:8081/
GET http://localhost:8081/api/tasks?page=0&size=100
```

Resultado:

- `/`: HTTP 200;
- `/api/tasks?page=0&size=100`: `totalElements = 11`, `contentCount = 11`;
- primeira tarefa real: `Migrate server to new infrastructure`.

### Validacao visual automatizada

Playwright CLI foi usado contra `http://localhost:8081/`.

Resultados observados:

- tela `Tasks` carregou com 11 linhas reais na tabela;
- card `Total` exibiu `11`;
- cards de status nao ficaram zerados apos o carregamento;
- 8 modulos futuros aparecem em `Roadmap`;
- todos os modulos futuros exibem badge `Em breve`;
- clique em `Calendar` abriu apenas o placeholder de modulo em desenvolvimento;
- placeholder exibiu nome, status, descricao e aviso de indisponibilidade;
- nao existe card `Announcement`;
- nao existe botao `Kanban` na tela de tarefas;
- nao foram encontrados dados mockados antigos.

As capturas temporarias foram usadas apenas para inspecao visual durante a validacao e nao foram mantidas como artefatos versionados.

## 7. Limitacoes

- O Docker Compose nao foi executado ate o fim porque o Docker daemon estava indisponivel no ambiente local.
- `Dashboard`, `Projects` e `Settings` continuam sem tela funcional e permanecem desabilitados para evitar simulacao indevida.
- O roadmap usa metadados estaticos apenas para nome, status e descricao dos modulos planejados; nao ha integracao real para esses modulos.

## 8. Proxima fase recomendada

FASE 8A.3: revisar responsividade e polimento fino da sidebar, incluindo comportamento mobile, truncamento de nomes longos e possivel estado informativo para itens Core ainda desabilitados.
