Feature: Gerenciamento de tarefas
  Como uma equipe pequena
  Quero gerenciar tarefas
  Para acompanhar o trabalho do time

  Scenario: Criar uma tarefa com sucesso
    Given nao existem tarefas cadastradas
    When eu crio uma tarefa com titulo "Preparar apresentacao"
    Then a tarefa deve ser criada com titulo "Preparar apresentacao"
    And o status da tarefa criada deve ser "PENDENTE"

  Scenario: Listar tarefas cadastradas
    Given existe uma tarefa cadastrada com titulo "Revisar API"
    When eu listo as tarefas
    Then a lista deve conter 1 item
    And a lista deve conter a tarefa "Revisar API"

  Scenario: Buscar uma tarefa existente
    Given existe uma tarefa cadastrada com titulo "Escrever testes"
    When eu busco a tarefa existente
    Then a tarefa encontrada deve ter titulo "Escrever testes"

  Scenario: Tentar buscar uma tarefa inexistente
    Given nao existem tarefas cadastradas
    When eu tento buscar a tarefa com id 999
    Then devo receber erro de tarefa nao encontrada contendo "999"

  Scenario: Atualizar uma tarefa existente
    Given existe uma tarefa cadastrada com titulo "Implementar API"
    When eu atualizo a tarefa existente para status "CONCLUIDO"
    Then a tarefa atualizada deve ter status "CONCLUIDO"

  Scenario: Excluir uma tarefa existente
    Given existe uma tarefa cadastrada com titulo "Remover tarefa"
    When eu excluo a tarefa existente
    Then a lista deve conter 0 item
