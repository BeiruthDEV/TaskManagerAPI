# Relatorio da Fase 6 - BDD com cenarios de comportamento

Data: 2026-06-08

## 1. Resumo da fase

Nesta fase, foram adicionadas evidencias praticas de BDD usando Cucumber, JUnit Platform e cenarios em Gherkin para os principais comportamentos do sistema de tarefas.

Os cenarios foram implementados de forma incremental e estavel, exercitando a camada `application` por meio da fachada `TaskService` e de um repositório fake em memoria nos steps. Essa escolha evita dependencias externas, banco real ou servidor HTTP durante os testes BDD, mantendo foco no comportamento de negocio.

Nao foram alterados endpoints publicos, payloads publicos ou funcionalidades existentes.

## 2. Dependencias adicionadas

Foram adicionadas dependencias de teste no `pom.xml`:

- `io.cucumber:cucumber-java`
- `io.cucumber:cucumber-junit-platform-engine`
- `org.junit.platform:junit-platform-suite`

Tambem foi adicionada a propriedade:

- `cucumber.version`

As dependencias foram adicionadas apenas com escopo de teste.

## 3. Estrutura criada

Arquivo de feature:

- `src/test/resources/features/tasks.feature`

Runner/configuracao:

- `src/test/java/com/taskmanager/api/bdd/CucumberTest.java`

Step definitions:

- `src/test/java/com/taskmanager/api/bdd/TaskStepDefinitions.java`

## 4. Cenarios BDD criados

O arquivo `tasks.feature` cobre os seguintes cenarios:

- criar uma tarefa com sucesso;
- listar tarefas cadastradas;
- buscar uma tarefa existente;
- tentar buscar uma tarefa inexistente;
- atualizar uma tarefa existente;
- excluir uma tarefa existente.

Os cenarios foram escritos em linguagem simples, com Given/When/Then, para expressar comportamento esperado do sistema de forma mais proxima do usuario.

## 5. Justificativa dos cenarios escolhidos

Os cenarios representam o fluxo principal de gerenciamento de tarefas:

- criar: entrada basica de trabalho no sistema;
- listar: consulta do estado atual;
- buscar existente: recuperacao de detalhe;
- buscar inexistente: comportamento de erro esperado;
- atualizar: mudanca de status/andamento;
- excluir: remocao do trabalho cadastrado.

Esses comportamentos sao centrais para o dominio atual e suficientes para demonstrar BDD sem duplicar todos os testes unitarios.

## 6. Como os cenarios demonstram comportamento do sistema

Os steps executam `TaskService`, que e a fachada da camada `application`, usando os casos de uso reais:

- `CreateTaskUseCase`
- `ListTasksUseCase`
- `FindTaskUseCase`
- `UpdateTaskUseCase`
- `DeleteTaskUseCase`

Para evitar infraestrutura pesada, foi criado um `InMemoryTaskRepository` dentro dos steps, implementando a porta `TaskRepository`. Assim, os cenarios validam o comportamento da aplicacao sem depender de JPA, H2 ou servidor web.

## 7. Arquivos alterados

Arquivos alterados:

- `pom.xml`

Arquivos criados:

- `src/test/resources/features/tasks.feature`
- `src/test/java/com/taskmanager/api/bdd/CucumberTest.java`
- `src/test/java/com/taskmanager/api/bdd/TaskStepDefinitions.java`
- `docs/relatorio-fase-6.md`

## 8. Resultado dos testes

Comando executado:

```bash
.\mvnw.cmd test
```

Resultado apos adicionar BDD:

- Build: sucesso.
- Testes totais executados: 32.
- Cenarios Cucumber executados: 6.
- Falhas: 0.
- Erros: 0.
- Ignorados: 0.

## 9. Limitacoes e melhorias futuras

- Os cenarios BDD exercitam a camada `application`, nao a API HTTP diretamente.
- Ainda faltam testes de `TaskController` com MockMvc para proteger contratos HTTP de forma explicita.
- Em uma fase futura, os cenarios podem ser conectados a MockMvc se a prioridade for validar a API fim a fim.
- Ainda nao foi adicionada medicao de cobertura com JaCoCo.

## 10. Proxima fase recomendada

A proxima fase recomendada e criar testes da camada `presentation` com MockMvc, cobrindo:

- endpoints publicos de `/api/tasks`;
- validacoes de request;
- respostas 400 e 404;
- preservacao dos payloads publicos.

Depois disso, o projeto estara mais preparado para iniciar Docker/Docker Compose ou evoluir os cenarios BDD para API HTTP.
