# Relatorio da Fase 5 - TDD e testes unitarios explicitos

Data: 2026-06-08

## 1. Resumo da fase

Nesta fase, a prioridade foi fortalecer as evidencias de TDD e testes unitarios na camada de application e no dominio, sem alterar endpoints, payloads publicos ou funcionalidades existentes.

Nao houve alteracao em codigo de producao. As mudancas ficaram restritas a testes automatizados e documentacao.

## 2. Testes existentes antes da fase

Antes desta fase, a suite tinha 13 testes passando, distribuidos principalmente em:

- `TaskServiceTest`;
- `TaskUseCaseTest`;
- `TaskFactoryTest`;
- `TaskRepositoryTest`;
- `TaskManagerApiApplicationTests`.

Esses testes ja cobriam criacao com defaults, atualizacao parcial, busca inexistente, delete existente, filtro por status/prioridade, factory e persistencia basica.

## 3. Lacunas de teste identificadas

Foram identificadas lacunas nos seguintes comportamentos:

- criacao de tarefa com todos os dados validos informados;
- atualizacao de tarefa inexistente;
- busca de tarefa existente;
- exclusao de tarefa inexistente;
- listagem paginada de tarefas;
- comportamento da fachada `TaskService` alem de criar/atualizar/buscar inexistente;
- regras basicas do modelo de dominio `Task`;
- preservacao de defaults quando setters recebem valores nulos.

## 4. Testes criados ou ajustados

### TaskUseCaseTest

Foram adicionados ou ajustados testes unitarios para os casos de uso:

- `shouldCreateTaskWhenCommandIsValid`;
- `shouldCreateTaskPreservingExpectedDefaultsWhenStatusAndPriorityAreNull`;
- `shouldUpdateTaskWhenTaskExists`;
- `shouldThrowExceptionWhenUpdatingTaskDoesNotExist`;
- `shouldFindTaskWhenTaskExists`;
- `shouldThrowExceptionWhenTaskDoesNotExist`;
- `shouldDeleteTaskWhenTaskExists`;
- `shouldThrowExceptionWhenDeletingTaskDoesNotExist`;
- `shouldListTasksWhenPageableIsProvided`;
- `shouldFilterTasksByStatusAndPriority`;
- `shouldListAllTasksWhenNoFilterIsProvided`.

### TaskServiceTest

Foram ampliados os testes da fachada:

- `shouldCreateTaskWhenCommandIsValid`;
- `shouldUpdateTaskWhenTaskExists`;
- `shouldFindTaskWhenTaskExists`;
- `shouldThrowExceptionWhenTaskDoesNotExist`;
- `shouldDeleteTaskWhenTaskExists`;
- `shouldListTasksWhenPageableIsProvided`;
- `shouldFilterTasksWhenStatusAndPriorityAreProvided`.

### TaskTest

Foi criado teste de dominio:

- `shouldCreateTaskWithDefaultStatusPriorityAndProgress`;
- `shouldCreateTaskWhenValuesAreProvided`;
- `shouldPreserveDefaultsWhenSettersReceiveNullValues`.

### TaskFactoryTest

Foi mantida a cobertura existente da factory:

- criacao de `Task` a partir de `CreateTaskCommand`;
- preservacao dos defaults quando campos opcionais nao sao informados.

## 5. Comportamentos cobertos

A suite agora cobre explicitamente:

- criacao de tarefa com dados validos;
- criacao preservando status, prioridade e progresso padrao;
- atualizacao de tarefa existente;
- tentativa de atualizar tarefa inexistente;
- busca de tarefa existente;
- tentativa de buscar tarefa inexistente;
- exclusao de tarefa existente;
- tentativa de excluir tarefa inexistente;
- listagem paginada;
- filtro por status e prioridade;
- listagem sem filtro;
- comportamento da `TaskFactory`;
- comportamento da fachada `TaskService`;
- regras basicas do modelo de dominio `Task`.

## 6. Como a fase demonstra TDD

Embora esta fase tenha sido aplicada sobre codigo ja existente, ela reforca a pratica de TDD para as proximas etapas porque:

- explicita o comportamento esperado antes de novas refatoracoes;
- cria uma rede de seguranca para casos de uso de application;
- documenta cenarios de erro e sucesso;
- reduz o risco de alterar contratos ou regras atuais sem perceber;
- usa testes unitarios rapidos com mocks para application, sem depender de infrastructure.

Nenhum codigo de producao precisou ser alterado, pois os novos testes confirmaram o comportamento atual.

## 7. Relacao com Clean Code, SOLID e Arquitetura Limpa

### Clean Code

- Nomes dos testes descrevem comportamento esperado.
- Testes pequenos e objetivos facilitam leitura e manutencao.
- Cenarios foram separados por responsabilidade.

### SOLID

- Os testes validam os casos de uso pequenos criados na Fase 3.
- A fachada `TaskService` e testada como delegadora/orquestradora.
- A application continua dependendo da porta `TaskRepository`.

### Arquitetura Limpa

- Testes de application usam mock da porta, sem acessar JPA diretamente.
- Testes de dominio validam o modelo sem framework.
- Testes de infrastructure continuam isolados em `TaskRepositoryTest`.

## 8. Arquivos alterados

Arquivos de teste alterados:

- `src/test/java/com/taskmanager/api/application/TaskServiceTest.java`
- `src/test/java/com/taskmanager/api/application/usecase/TaskUseCaseTest.java`

Arquivos de teste criados:

- `src/test/java/com/taskmanager/api/domain/model/TaskTest.java`

Documentacao criada:

- `docs/relatorio-fase-5.md`

## 9. Resultado final dos testes

Comando executado:

```bash
.\mvnw.cmd test
```

Resultado:

- Build: sucesso.
- Testes executados: 26.
- Falhas: 0.
- Erros: 0.
- Ignorados: 0.

## 10. Limitacoes e melhorias futuras

- Ainda faltam testes especificos da camada `presentation` com `TaskController`.
- Ainda nao ha testes BDD em Gherkin/Cucumber.
- A cobertura nao foi medida com ferramenta como JaCoCo nesta fase.
- O teste de repository continua sendo um teste de fatia JPA, nao unitario puro, o que e adequado para infrastructure.

## 11. Proxima fase recomendada

A proxima fase recomendada e testar a camada `presentation`, cobrindo:

- endpoints principais de `/api/tasks`;
- validacoes de request;
- respostas 400 e 404;
- preservacao dos payloads publicos.

Depois disso, o projeto fica mais preparado para introduzir BDD.
