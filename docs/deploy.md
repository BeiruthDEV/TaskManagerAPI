# Deploy

## Estado atual

O deploy publico foi realizado na Railway em **17/06/2026**.

A aplicacao esta publicada e acessivel em:

```text
https://taskmanagerapi-production-b8b0.up.railway.app/
```

## Plataforma usada

- Plataforma: Railway
- Aplicacao: Trackio / TaskManagerAPI
- Tipo: API Spring Boot servindo backend REST e frontend estatico

## Endpoints validados

Foram validados os seguintes endpoints no ambiente publicado:

| Endpoint | Resultado |
|---|---|
| `/` | HTTP 200 |
| `/api/tasks` | HTTP 200 |
| `/api/tasks/dashboard` | HTTP 200 |
| `/api/tasks/kanban` | HTTP 200 |
| `/swagger-ui.html` | HTTP 200 |
| `/v3/api-docs` | HTTP 200 |

## Dados de demonstracao

O ambiente publicado usa dados ficticios da **Atlas Solucoes Empresariais** para demonstracao. Esses dados permitem visualizar a interface com tarefas, responsaveis, projetos, prazos, prioridades, status e indicadores como se o sistema estivesse em uso por uma empresa real.

Esses dados nao representam clientes reais. Em uma instalacao real, os dados seriam cadastrados pelos usuarios da empresa contratante.

## Comandos de validacao usados

Validacao local:

```powershell
.\mvnw.cmd test
node --check src/main/resources/static/app.js
node --check src/main/resources/static/task-api.js
```

Validacao do ambiente publicado:

```powershell
$base='https://taskmanagerapi-production-b8b0.up.railway.app'
$paths=@('/','/api/tasks','/api/tasks/dashboard','/api/tasks/kanban','/swagger-ui.html','/v3/api-docs')
foreach($path in $paths){
  $r=Invoke-WebRequest -Uri ($base+$path) -Method GET -UseBasicParsing -TimeoutSec 30
  "$($r.StatusCode) $path"
}
```

Resultado observado:

```text
200 /
200 /api/tasks
200 /api/tasks/dashboard
200 /api/tasks/kanban
200 /swagger-ui.html
200 /v3/api-docs
```

## Variaveis esperadas

O profile de producao deve receber variaveis de ambiente pela plataforma:

```text
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=...
SPRING_DATASOURCE_USERNAME=...
SPRING_DATASOURCE_PASSWORD=...
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

## Evidencias para entrega

- URL publica ativa: `https://taskmanagerapi-production-b8b0.up.railway.app/`
- Plataforma: Railway
- Endpoints principais validados com HTTP 200
- Swagger/OpenAPI disponivel em `/swagger-ui.html` e `/v3/api-docs`
- Dados demo ficticios documentados em `docs/dados-demo.md`

## Limitacoes do ambiente publicado

- Ainda nao ha autenticacao.
- Ainda nao ha modulos reais de organizacao, funcionarios, equipes e projetos.
- Ainda nao ha microsservicos executaveis separados.
- Ainda nao ha migrations com Flyway/Liquibase.
