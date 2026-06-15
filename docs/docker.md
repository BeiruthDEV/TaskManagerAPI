# Docker

## Arquivos existentes

O projeto possui:

- `Dockerfile`;
- `docker-compose.yml`.

## Dockerfile atual

O Dockerfile usa build multi-stage:

1. imagem Maven com Temurin 17 para compilar o projeto;
2. imagem JRE Alpine para executar o jar final;
3. usuario nao-root para execucao;
4. exposicao da porta `8080`;
5. profile padrao `dev`.

## Docker Compose atual

O `docker-compose.yml` sobe dois servicos:

```text
taskmanager-api
taskmanager-postgres
```

Mapeamento de portas:

```text
taskmanager-api:      8081:8080
taskmanager-postgres: 5432:5432
```

Assim, a aplicacao fica acessivel em:

```text
http://localhost:8081
```

O banco PostgreSQL fica acessivel localmente em:

```text
host: localhost
port: 5432
database: trackio
user: trackio
password: trackio
```

## Persistencia

O PostgreSQL usa o volume nomeado:

```text
taskmanager-postgres-data
```

Esse volume preserva os dados entre reinicios dos containers.

## Como executar

```powershell
docker compose down
docker compose build
docker compose up -d
```

Validar a configuracao efetiva:

```powershell
docker compose config
```

Ver logs:

```powershell
docker compose logs -f
```

Parar:

```powershell
docker compose down
```

Parar e remover tambem os dados persistidos:

```powershell
docker compose down -v
```

## Profiles

### `dev`

Usado por padrao fora do Docker. Mantem H2 em memoria e H2 Console ativo.

```powershell
.\mvnw.cmd spring-boot:run
```

### `test`

Mantem H2 em memoria para execucao de testes automatizados.

```powershell
.\mvnw.cmd test
```

### `docker`

Usado pelo Docker Compose. Configura PostgreSQL via variaveis:

```text
SPRING_PROFILES_ACTIVE=docker
SPRING_DATASOURCE_URL=jdbc:postgresql://taskmanager-postgres:5432/trackio
SPRING_DATASOURCE_USERNAME=trackio
SPRING_DATASOURCE_PASSWORD=trackio
```

### `prod`

Preparado para deploy com PostgreSQL externo. Deve receber variaveis de ambiente da plataforma:

```text
SPRING_PROFILES_ACTIVE=prod
SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/database
SPRING_DATASOURCE_USERNAME=usuario
SPRING_DATASOURCE_PASSWORD=senha
SPRING_JPA_HIBERNATE_DDL_AUTO=update
```

## Limitacoes atuais

- ainda nao possui migracoes de banco com Flyway ou Liquibase;
- credenciais do Compose sao locais e didaticas;
- profile `prod` depende de variaveis da plataforma de deploy;

## Evolucao futura

- separar variaveis em `.env.example`;
- adicionar Flyway ou Liquibase;
- validar build e execucao em CI.
