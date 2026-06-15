# Proposta do Trackio

## Visao geral

O Trackio e um sistema empresarial de gestao de demandas, tarefas, responsaveis, prazos e produtividade. A proposta e oferecer para empresas uma ferramenta propria para organizar a operacao diaria sem depender de planilhas, grupos de WhatsApp, e-mails soltos ou quadros Trello mal utilizados.

O projeto atual nasceu como `TaskManagerAPI`, uma API REST de tarefas, e esta evoluindo para o Trackio como produto com identidade propria e escopo empresarial.

## Problema escolhido

Empresas pequenas e medias frequentemente perdem controle sobre demandas internas porque o trabalho fica espalhado em ferramentas informais:

- planilhas sem padronizacao;
- WhatsApp;
- e-mails;
- anotacoes manuais;
- Trello sem processo claro;
- comunicacao verbal sem historico.

Isso gera tarefas esquecidas, prazos perdidos, falta de clareza sobre responsaveis, retrabalho e baixa visibilidade para gestores.

## Solucao proposta

O Trackio centraliza demandas, tarefas, responsaveis, prioridades, prazos, progresso e status em uma interface limpa e empresarial. A primeira parte implementada e o modulo de tarefas. A evolucao planejada inclui empresas, usuarios, funcionarios, equipes, projetos, Kanban, dashboard, comentarios, anexos e historico.

## Proposta comercial

O Trackio foi pensado inicialmente como software vendido por valor fechado/licenca de uso, nao como assinatura mensal obrigatoria.

Modelo proposto:

- a empresa paga um valor unico pelo escopo contratado;
- a empresa recebe direito de uso do sistema por tempo indeterminado;
- o sistema pode ser configurado para a realidade da empresa;
- suporte continuo, hospedagem, manutencao e novas funcionalidades podem ser cobrados separadamente.

Essa abordagem permite vender o produto como uma solucao empresarial sob medida, mantendo clareza contratual sobre o que esta incluso.

## Escopo atual

Hoje o sistema entrega:

- API REST para tarefas;
- frontend estatico para tarefas;
- CRUD de tarefas;
- filtros por status e prioridade;
- testes unitarios e BDD;
- Dockerfile e Docker Compose;
- documentacao academica.

## Escopo futuro

Para se tornar o Trackio empresarial completo, o projeto ainda precisa evoluir para:

- autenticacao;
- organizacao/empresa;
- funcionarios;
- equipes;
- projetos;
- Kanban;
- dashboard;
- comentarios;
- anexos;
- historico;
- deploy publico;
- banco de producao.
