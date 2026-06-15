# Relatorio da Fase 7 - Docker e Docker Compose

## Resumo da fase

A Fase 7 adicionou a infraestrutura minima para executar o TaskManagerAPI em container Docker, sem alterar regras de negocio, endpoints publicos, payloads ou arquitetura de codigo.

Foram criados:

- `Dockerfile`
- `.dockerignore`
- `docker-compose.yml`

Tambem foi validada a execucao local dos testes e a inicializacao da aplicacao via Docker Compose.

## Como a aplicacao roda atualmente

O projeto e uma aplicacao Spring Boot empacotada como arquivo JAR pelo Maven.

Execucao local atual:

```powershell
.\mvnw.cmd spring-boot:run
```

Execucao dos testes:

```powershell
.\mvnw.cmd test
```

## Porta padrao da API

Nao ha configuracao explicita de `server.port` nos arquivos `application.properties`, `application-dev.properties` ou `application-file.properties`.

Por isso, a aplicacao usa a porta padrao do Spring Boot:

- Porta interna da API: `8080`

No Docker Compose desta fase, a porta interna `8080` foi exposta no host como `8081`, porque a porta local `8080` ja estava ocupada por um processo Java durante a validacao.

URL local validada:

```text
http://localhost:8081
```

Endpoint consultado para validacao:

```text
http://localhost:8081/api/tasks
```

## Banco utilizado

O projeto utiliza H2.

Configuracoes identificadas:

- Perfil padrao: `dev`
- Banco do perfil `dev`: H2 em memoria
- URL: `jdbc:h2:mem:taskmanager;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
- Console H2: `/h2-console`

Como o banco atual e H2 em memoria e nao ha uso de PostgreSQL, MySQL ou outro banco externo nesta fase, o `docker-compose.yml` foi mantido simples, com apenas o servico da API.

Um servico de banco externo podera ser adicionado em fase posterior, caso o projeto evolua para persistencia containerizada com PostgreSQL ou MySQL.

## Estrategia escolhida para Docker

Foi utilizado um Dockerfile multi-stage simples:

1. Imagem de build com Maven e Java 17.
2. Imagem final com JRE 17 para executar o JAR.

A estrategia multi-stage foi escolhida para permitir que a imagem seja construida de forma reproduzivel sem depender de um build local previo e, ao mesmo tempo, evitar que ferramentas de build fiquem na imagem final de execucao.

## Explicacao do Dockerfile

O `Dockerfile` realiza os seguintes passos:

- Usa `maven:3.9.9-eclipse-temurin-17` para compilar o projeto.
- Copia `pom.xml` e `src`.
- Executa `mvn -B -DskipTests package`.
- Usa `eclipse-temurin:17-jre-alpine` como imagem final.
- Cria um usuario nao privilegiado chamado `app`.
- Copia o JAR gerado para `/app/app.jar`.
- Expoe a porta interna `8080`.
- Define `SPRING_PROFILES_ACTIVE=dev`.
- Inicia a aplicacao com `java -jar /app/app.jar`.

## Explicacao do docker-compose.yml

O `docker-compose.yml` define um servico:

- `taskmanager-api`

Configuracoes principais:

- Build a partir do `Dockerfile` local.
- Container nomeado como `taskmanager-api`.
- Perfil Spring ativo: `dev`.
- Porta do host `8081` apontando para a porta interna `8080`.
- Politica de reinicio: `unless-stopped`.

## Comandos Docker

Buildar e iniciar a aplicacao:

```powershell
docker compose up --build
```

Buildar e iniciar em segundo plano:

```powershell
docker compose up --build -d
```

Verificar containers:

```powershell
docker compose ps
```

Ver logs:

```powershell
docker compose logs -f taskmanager-api
```

Parar os containers:

```powershell
docker compose stop
```

Parar e remover containers/rede criados pelo Compose:

```powershell
docker compose down
```

## Arquivos alterados

- `Dockerfile`
- `.dockerignore`
- `docker-compose.yml`
- `docs/relatorio-fase-7.md`

## Resultado dos testes

Comando executado:

```powershell
.\mvnw.cmd test
```

Resultado:

- Build: sucesso
- Testes executados: 32
- Falhas: 0
- Erros: 0
- Ignorados: 0

## Resultado do Docker Compose

Primeira tentativa:

- O build da imagem foi concluido com sucesso.
- A subida do container falhou porque a porta local `8080` estava ocupada por um processo Java.

Ajuste aplicado:

- A porta interna da aplicacao permaneceu `8080`.
- A porta local do Compose foi alterada para `8081`.

Validacao final:

```powershell
docker compose up --build -d
```

Resultado:

- Imagem buildada com sucesso.
- Container `taskmanager-api` iniciado com sucesso.
- Mapeamento ativo: `0.0.0.0:8081->8080/tcp`.
- API validada em `http://localhost:8081/api/tasks`.

## Relacao com o enunciado academico

Esta fase adiciona evidencia pratica de Docker e Docker Compose ao projeto.

A configuracao criada demonstra:

- Empacotamento da aplicacao Spring Boot em imagem Docker.
- Execucao containerizada da API.
- Exposicao controlada de porta.
- Separacao entre build e runtime.
- Uso de Compose para orquestrar a execucao local.

## Limitacoes e melhorias futuras

- O banco H2 em memoria nao preserva dados apos reiniciar o container.
- Ainda nao ha servico de banco externo no Compose.
- Ainda nao ha healthcheck no container.
- Ainda nao ha perfil especifico de producao para Docker.
- Ainda nao ha deploy em servidor ou cloud.

## Proxima fase recomendada

A proxima fase recomendada e preparar a base de deploy ou evoluir a configuracao Docker para um ambiente mais proximo de producao.

Uma fase pequena e revisavel seria:

- criar um perfil `docker` ou `prod` com configuracoes externas por variaveis de ambiente;
- avaliar PostgreSQL em Docker Compose;
- manter H2 apenas para desenvolvimento/testes locais;
- documentar os comandos de execucao em ambiente containerizado.
