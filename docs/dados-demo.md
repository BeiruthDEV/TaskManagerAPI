# Dados de Demonstracao

## Objetivo

O Trackio usa dados ficticios de demonstracao para que a interface seja apresentada com contexto realista. A ideia e evitar uma tela vazia ou tarefas genericas sem sentido, permitindo que o professor visualize o sistema como se ele estivesse em uso por uma empresa real.

Esses dados ajudam a demonstrar:

- listagem de tarefas;
- filtros por status, prioridade e responsavel;
- indicadores do dashboard;
- organizacao visual do Kanban;
- estados de prazo, progresso e prioridade;
- comportamento do frontend consumindo dados reais da API.

## Empresa ficticia

A empresa usada na demonstracao e:

- **Atlas Solucoes Empresariais**

Ela e totalmente ficticia. Nenhum dado representa cliente real, contrato real, colaborador real ou projeto real.

## Origem dos dados

Os dados sao semeados pelo arquivo:

- `src/main/java/com/taskmanager/api/infrastructure/config/DevDataLoader.java`

O loader roda apenas no profile `dev`, por causa da anotacao `@Profile("dev")`. Ele usa a porta `TaskRepository`, nao acessa diretamente o repository JPA, e cadastra tarefas iniciais quando a base esta vazia.

O proprio loader evita duplicacao: antes de inserir os dados, ele verifica `taskRepository.count()`. Se ja existir pelo menos uma tarefa cadastrada, ele nao cria novamente a massa de demonstracao.

## Setores simulados

Os dados representam areas comuns em uma empresa:

- Comercial;
- Financeiro;
- Recursos Humanos;
- Atendimento;
- Tecnologia/operacoes internas.

## Responsaveis simulados

Os responsaveis usados nas tarefas demo sao ficticios:

- Ana Souza;
- Bruno Lima;
- Carla Mendes;
- Diego Rocha;
- Fernanda Alves.

## Projetos simulados

Os projetos simulados sao:

- Implantacao do CRM;
- Campanha Comercial Q2;
- Fechamento Financeiro Mensal;
- Onboarding de Colaboradores;
- Reestruturacao do Atendimento.

## Tipo de informacao mockada

Os dados mockados/semeados incluem:

- titulos de tarefas;
- descricoes;
- responsaveis;
- projetos;
- prazos;
- progresso;
- status;
- prioridades.

Com isso, os indicadores do dashboard e as colunas do Kanban conseguem apresentar uma situacao mais proxima de uso empresarial.

## Uso em producao

Em producao, esses dados nao devem representar a base real da empresa. Os dados reais seriam cadastrados pelos usuarios da empresa contratante por meio da interface ou por futuras rotinas de importacao.

O `DevDataLoader` existe para demonstracao e desenvolvimento local. Em ambiente de producao, o profile esperado e `prod`, usando variaveis de ambiente e banco PostgreSQL externo. Nesse caso, a massa ficticia nao deve ser usada como dado oficial.

## Como resetar a base demo

Em desenvolvimento local com H2 em memoria, basta reiniciar a aplicacao no profile `dev`. Como o banco em memoria e recriado, o loader pode popular novamente a base.

Em Docker Compose, os dados ficam no volume PostgreSQL. Para resetar somente um ambiente local de demonstracao, use:

```powershell
docker compose down -v
docker compose up -d --build
```

Atencao: esse comando remove o volume local do PostgreSQL usado pelo Docker Compose. Ele nao deve ser usado em producao nem em qualquer ambiente que contenha dados reais.

Para ambientes com dados reais, a limpeza deve ser feita com backup, migracao controlada ou rotina administrativa propria, nunca removendo volume diretamente.

## Defesa academica

Os dados demo sao uma escolha consciente de apresentacao. Eles nao simulam funcionalidades inexistentes; apenas preenchem o modulo de tarefas ja implementado com uma massa coerente. Isso permite avaliar melhor a API, o frontend, os filtros, os cards, o dashboard e o Kanban sem depender de cadastro manual durante a apresentacao.
