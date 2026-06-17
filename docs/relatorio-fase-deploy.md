# Relatorio da Fase de Deploy

## Resumo

O deploy publico do Trackio / TaskManagerAPI foi realizado na plataforma Railway em **17/06/2026**.

A aplicacao esta publicada e acessivel em:

```text
https://taskmanagerapi-production-b8b0.up.railway.app/
```

## Plataforma usada

- Plataforma: Railway
- Aplicacao: Trackio / TaskManagerAPI
- Tipo de aplicacao: Spring Boot API com frontend estatico servido pela propria aplicacao

## URL publica

```text
https://taskmanagerapi-production-b8b0.up.railway.app/
```

## Endpoints validados

Os seguintes endpoints foram validados no ambiente publicado:

| Endpoint | Resultado |
|---|---|
| `/` | HTTP 200 |
| `/api/tasks` | HTTP 200 |
| `/api/tasks/dashboard` | HTTP 200 |
| `/api/tasks/kanban` | HTTP 200 |
| `/swagger-ui.html` | HTTP 200 |
| `/v3/api-docs` | HTTP 200 |

## Dados de demonstracao

O ambiente publicado usa dados ficticios da **Atlas Solucoes Empresariais** para demonstracao. Esses dados simulam responsaveis, projetos, tarefas, prazos, prioridades, status e indicadores para que a interface possa ser visualizada como se estivesse sendo usada por uma empresa real.

Esses dados nao representam clientes reais. Em producao, os dados seriam cadastrados pelos usuarios da empresa contratante.

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

## Resultado

O criterio de deploy esta atendido: a aplicacao esta publicada, acessivel publicamente, com Swagger/OpenAPI disponivel e endpoints principais respondendo com sucesso.

## Limitacoes restantes

- Nao ha autenticacao nesta versao.
- Nao ha modulos cadastrais reais de organizacao, funcionarios, equipes e projetos.
- Nao ha microsservicos executaveis separados.
- Nao ha migrations com Flyway/Liquibase.
