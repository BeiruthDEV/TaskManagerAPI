# Justificativas Tecnicas

## Java 17

Java 17 e uma versao LTS, estavel e adequada para projetos academicos e empresariais. Permite uso de records, boa performance e amplo suporte em cloud.

## Spring Boot

Spring Boot acelera a criacao de APIs REST, integra validacao, injecao de dependencia, configuracao e testes. E uma escolha madura para sistemas empresariais.

## Spring Data JPA

JPA reduz codigo repetitivo de persistencia e permite evoluir de H2 para PostgreSQL mantendo boa parte da estrutura.

## H2 Database

H2 foi escolhido para desenvolvimento local simples e testes por velocidade e baixa configuracao. A limitacao e que nao representa completamente um banco de producao.

## PostgreSQL

PostgreSQL foi adicionado ao Docker Compose para aproximar o ambiente containerizado de um banco real e persistente. O uso atual e local, via profile `docker`; producao deve usar o profile `prod` com variaveis fornecidas pela plataforma de deploy.

## Swagger/OpenAPI

Swagger facilita documentar e testar endpoints. Isso ajuda tanto na entrega academica quanto na demonstracao comercial.

## Docker

Docker padroniza execucao e reduz diferencas entre ambientes. O Dockerfile atual empacota a API, e o Compose simplifica a execucao local com API, PostgreSQL, volume persistente e `.env.example`.

## Cucumber BDD

Cucumber permite descrever comportamento em linguagem proxima do negocio. Os cenarios atuais cobrem fluxos principais de tarefas.

## Arquitetura em camadas

A separacao entre `domain`, `application`, `infrastructure` e `presentation` facilita manutencao, testes e futura extracao para microsservicos.

## Frontend estatico

O frontend estatico foi escolhido para entregar uma interface simples e direta sem aumentar a complexidade com framework SPA. Isso e adequado para o momento atual, mas pode evoluir para React, Vue ou outro framework caso o produto cresca.

## Limitacoes assumidas

- sem autenticacao por enquanto;
- sem deploy publico;
- sem microsservicos executaveis;
- sem migrations versionadas;
- frontend sem testes automatizados.
