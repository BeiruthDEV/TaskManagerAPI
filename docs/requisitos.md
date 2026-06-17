# Requisitos do Trackio

## MVP atual implementado

O MVP atual cobre o modulo de tarefas:

- criar tarefa;
- listar tarefas;
- buscar tarefa por id;
- atualizar tarefa;
- excluir tarefa;
- filtrar por status e prioridade;
- consultar tarefas agrupadas por status para Kanban real via API;
- consultar indicadores basicos de dashboard via API;
- visualizar tarefas em tabela no frontend;
- visualizar Kanban no frontend;
- visualizar dashboard no frontend;
- buscar tarefas localmente no frontend;
- exibir resumo por status;
- exibir roadmap de modulos futuros.

Campos atuais de tarefa:

- titulo;
- descricao;
- responsavel;
- projeto;
- progresso;
- status;
- prioridade;
- prazo.

## MVP desejado

Para uma primeira versao realmente vendavel do Trackio, o MVP desejado deve incluir:

- login;
- cadastro de usuario;
- organizacao/empresa;
- funcionarios/membros;
- equipes/departamentos;
- projetos;
- tarefas vinculadas a empresa, projeto e responsavel;
- lista de tarefas;
- Kanban;
- filtros e busca;
- tela dashboard basica;
- perfis de acesso: administrador, gestor e funcionario.

## Versao futura

Funcionalidades futuras:

- comentarios por tarefa;
- anexos;
- historico de atividades;
- notificacoes;
- relatorios;
- integracoes;
- calendario;
- indicadores de produtividade;
- auditoria;
- configuracoes por empresa.

## Requisitos nao funcionais

- interface limpa e empresarial;
- API documentada;
- codigo organizado em camadas;
- testes automatizados;
- execucao com Docker;
- deploy em servidor/cloud;
- seguranca para dados de empresas;
- evolucao para banco relacional de producao;
- separacao futura por microsservicos quando houver justificativa tecnica.

## Fora do escopo atual

Ainda nao estao implementados:

- autenticacao;
- multiempresa;
- projetos reais;
- comentarios;
- anexos;
- deploy publico.

Observacao: Kanban e dashboard existem como endpoints de API e possuem telas no frontend. O limite atual e que esses recursos ainda fazem parte do modulo de tarefas, sem projetos, equipes, usuarios e organizacoes como modulos cadastrais completos.
