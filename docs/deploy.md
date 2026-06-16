# Deploy

## Estado atual

O deploy publico ainda nao foi realizado. O projeto possui Dockerfile, Docker Compose, profile `docker` com PostgreSQL local e profile `prod` preparado para PostgreSQL externo, mas ainda nao ha URL publicada em servidor ou plataforma cloud.

O deploy sera executado na fase final da entrega, depois da revisao documental e da validacao local com testes e Docker Compose.

Link publico atual:

```text
Pendente
```

## Plataformas sugeridas

Opcoes possiveis:

- Render;
- Railway;
- Fly.io;
- VPS;
- Azure;
- AWS;
- Google Cloud.

Para a entrega academica, Render ou Railway tendem a ser opcoes simples para publicar a API rapidamente.

## Plano de deploy

1. Escolher plataforma para a fase final.
2. Configurar build do projeto.
3. Definir `SPRING_PROFILES_ACTIVE=prod`.
4. Configurar banco PostgreSQL persistente.
5. Publicar backend.
6. Validar `/`, `/api/tasks` e Swagger.
7. Registrar link publico no README e neste documento.

## Variaveis esperadas

- `SPRING_PROFILES_ACTIVE`;
- `SPRING_DATASOURCE_URL`;
- `SPRING_DATASOURCE_USERNAME`;
- `SPRING_DATASOURCE_PASSWORD`;
- `SPRING_JPA_HIBERNATE_DDL_AUTO`;
- segredo JWT, quando autenticacao existir.

Exemplo para ambiente de producao:

```text
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/trackio
SPRING_DATASOURCE_USERNAME=usuario
SPRING_DATASOURCE_PASSWORD=senha
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

## Comandos uteis antes do deploy

Validar testes:

```powershell
.\mvnw.cmd test
```

Validar Compose local:

```powershell
docker compose config
docker compose up -d --build
```

## Evidencias necessarias para entrega

- link publico ativo;
- print ou log de deploy;
- comandos de build/start;
- endpoints testados;
- observacao sobre limitacoes do ambiente publicado.

## O que nao deve ser afirmado ainda

- Nao afirmar que o sistema ja esta publicado.
- Nao informar link ficticio.
- Nao afirmar que o ambiente de producao ja foi validado.
- Nao afirmar que ha infraestrutura cloud ativa antes da fase final de deploy.
