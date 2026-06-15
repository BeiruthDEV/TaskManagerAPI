# BDD

## Estrutura atual

O projeto possui BDD com Cucumber:

- `src/test/resources/features/tasks.feature`;
- `src/test/java/com/taskmanager/api/bdd/CucumberTest.java`;
- `src/test/java/com/taskmanager/api/bdd/TaskStepDefinitions.java`.

## Cenarios atuais

Os cenarios atuais cobrem:

- criar tarefa com sucesso;
- listar tarefas cadastradas;
- buscar tarefa existente;
- tentar buscar tarefa inexistente;
- atualizar tarefa existente;
- excluir tarefa existente.

Os steps usam a camada de aplicacao por meio de `TaskService` e um repository em memoria. Isso torna os cenarios rapidos e focados no comportamento principal.

## Limitacao atual

Os cenarios ainda nao exercitam a API HTTP real. Eles validam comportamento de negocio, mas nao validam serializacao, status HTTP, validacao de request ou contrato REST.

## Cenarios BDD futuros

Para o Trackio empresarial, adicionar:

- gestor cria uma tarefa para um funcionario;
- funcionario visualiza suas tarefas;
- gestor cria projeto e associa tarefas;
- tarefa muda de coluna no Kanban;
- dashboard mostra tarefas atrasadas;
- usuario sem permissao nao acessa dados de outra empresa;
- comentario e registrado no historico da tarefa;
- anexo e enviado para uma tarefa.
