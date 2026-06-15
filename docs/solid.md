# SOLID

## SRP - Single Responsibility Principle

O projeto aplica SRP ao separar responsabilidades:

- controller: entrada HTTP;
- DTOs: contrato externo;
- use cases: operacoes de aplicacao;
- repository port: contrato de persistencia;
- adapter JPA: detalhe de infraestrutura;
- mapper: conversao entre modelos;
- exception handler: tratamento de erros.

Melhoria futura: enriquecer o dominio para que regras de tarefa fiquem menos concentradas nos use cases.

## OCP - Open/Closed Principle

A existencia da porta `TaskRepository` permite trocar a infraestrutura de persistencia sem alterar os use cases. O sistema pode evoluir de H2/JPA para PostgreSQL/JPA mantendo a camada de aplicacao relativamente estavel.

Melhoria futura: aplicar Strategy para regras variaveis, como risco de tarefa, notificacao e calculo de produtividade.

## LSP - Liskov Substitution Principle

`TaskRepositoryAdapter` pode substituir qualquer implementacao de `TaskRepository` esperada pelos use cases. Nos testes BDD, um repository em memoria tambem implementa a mesma porta.

Melhoria futura: criar contratos de teste para garantir que diferentes implementacoes da porta tenham o mesmo comportamento.

## ISP - Interface Segregation Principle

A aplicacao ja separa operacoes em use cases especificos. Isso evita uma unica classe gigante para todas as responsabilidades.

Melhoria futura: dividir interfaces de repository quando os modulos crescerem, evitando uma porta com metodos demais.

## DIP - Dependency Inversion Principle

Os use cases dependem de `TaskRepository`, uma abstracao da camada de aplicacao, e nao diretamente de `JpaRepository`.

Melhoria futura: remover tipos Spring (`Page`, `Pageable`, `@Transactional`) da application para tornar a inversao ainda mais pura.
