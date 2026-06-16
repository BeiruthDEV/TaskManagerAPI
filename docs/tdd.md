# TDD e Testes Unitarios

## Testes existentes

O projeto possui testes automatizados para:

- dominio: `TaskTest`;
- factory: `TaskFactoryTest`;
- use cases: `TaskUseCaseTest`;
- facade/service: `TaskServiceTest`;
- repository JPA: `TaskRepositoryTest`;
- controller/API HTTP: `TaskControllerTest`;
- contexto Spring: `TaskManagerApiApplicationTests`;
- BDD: `CucumberTest` e `TaskStepDefinitions`.

## O que os testes validam

Os testes atuais validam:

- defaults de tarefa;
- criacao de tarefa;
- atualizacao parcial;
- busca por id;
- erro para tarefa inexistente;
- exclusao;
- listagem paginada;
- filtro por status e prioridade;
- persistencia JPA;
- contratos HTTP principais do controller de tarefas;
- validacoes HTTP para payloads invalidos;
- respostas 404 para tarefas inexistentes;
- preenchimento de campos de auditoria.

## Relacao com TDD

Os testes foram usados para proteger comportamento antes e durante as refatoracoes para camadas e use cases. A estrategia recomendada para as proximas fases e manter o ciclo:

1. escrever teste que descreve o comportamento esperado;
2. implementar o minimo necessario;
3. refatorar mantendo testes passando.

## Testes que ainda faltam

- testes de contrato do frontend com a API;
- testes automatizados da interface;
- testes para autenticacao;
- testes para organizacao, usuarios, projetos, Kanban e dashboard.
