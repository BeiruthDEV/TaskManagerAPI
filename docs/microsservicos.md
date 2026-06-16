# Microsservicos

## Estado atual

O Trackio ainda nao possui microsservicos executaveis separados. O estado atual e um monolito modular em Spring Boot, organizado em camadas inspiradas em Arquitetura Limpa.

Essa decisao e intencional: antes de dividir em servicos separados, o projeto precisa estabilizar dominio, testes, contratos, Docker, deploy e banco de producao.

Portanto, este documento deve ser lido como uma proposta arquitetural de divisao futura, nao como evidencia de servicos separados rodando no Docker Compose atual.

## Proposta de divisao futura

Uma divisao futura coerente para o Trackio seria:

| Servico | Responsabilidade |
|---|---|
| Auth Service | login, autenticacao, tokens, roles e permissoes |
| Organization Service | empresas, funcionarios, membros, equipes e departamentos |
| Task Service | tarefas, status, prioridade, prazo, progresso, comentarios e anexos |
| Report Service | dashboard, produtividade, indicadores e relatorios |
| Notification Service | alertas, notificacoes internas e e-mails |

Essa divisao atende ao requisito academico como modelagem e justificativa tecnica. Caso a avaliacao exija execucao real de microsservicos, sera necessario criar servicos separados em uma fase propria.

## Justificativa da divisao

Auth Service isola seguranca e credenciais.

Organization Service concentra dados estruturais da empresa.

Task Service fica responsavel pelo modulo operacional principal.

Report Service pode consultar dados consolidados e gerar indicadores sem sobrecarregar o fluxo transacional.

Notification Service permite evoluir notificacoes de forma independente.

## Relacao com o monolito atual

A separacao atual em `domain`, `application`, `infrastructure` e `presentation` ajuda uma futura extracao porque:

- regras de aplicacao ja estao separadas em use cases;
- persistencia ja passa por uma porta;
- controllers ja ficam em camada de presentation;
- DTOs separam contrato HTTP do dominio.

## O que nao deve ser afirmado

Nao se deve afirmar que o projeto ja tem microsservicos em producao. O correto e afirmar que:

- existe uma modelagem de microsservicos;
- a implementacao atual e monolitica modular;
- a divisao em servicos e uma evolucao planejada;
- o Docker Compose atual sobe API e PostgreSQL, nao varios microsservicos de negocio;
- a extracao deve ocorrer apenas quando houver necessidade real ou exigencia academica especifica.

## Proximos passos

- documentar contratos entre servicos;
- identificar bounded contexts;
- separar primeiro um esqueleto minimo se a avaliacao exigir evidencia executavel;
- adicionar banco por servico somente em uma fase madura;
- considerar API Gateway apenas quando houver mais de um servico real.
