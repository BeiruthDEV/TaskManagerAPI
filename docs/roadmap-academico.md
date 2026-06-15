# Roadmap academico

Este roadmap organiza a evolucao do Trackio em fases pequenas, revisaveis e seguras. A ordem proposta parte do estado atual identificado na auditoria tecnica e evita criar microsservicos, Docker ou deploy antes de existir base suficiente de testes e arquitetura.

## 1. Fases ja concluidas

### Fase 0 - Auditoria tecnica

Status: concluida.

Entregas:

- Analise da estrutura atual do projeto.
- Identificacao de tecnologias, pontos de entrada, testes, documentacao e lacunas.
- Relatorio em `docs/auditoria-fase-0.md`.
- Execucao da suite atual com `.\mvnw.cmd test`.

Criterios de aceite atendidos:

- Nenhum arquivo de codigo alterado.
- Relatorio tecnico criado.
- Plano incremental proposto.
- Testes existentes executados com sucesso.

### Fase 1 - Documentacao academica base

Status: em execucao nesta entrega.

Entregas previstas:

- Atualizacao do `README.md`.
- Criacao de `docs/proposta-academica.md`.
- Criacao de `docs/roadmap-academico.md`.
- Separacao clara entre estado atual, adaptacoes planejadas, evidencias existentes e evidencias futuras.
- Execucao de `.\mvnw.cmd test` ao final.

Criterios de aceite:

- Apenas arquivos de documentacao alterados.
- Nenhum arquivo de codigo, build ou arquitetura alterado.
- Documentacao clara, academica e objetiva.
- Nenhuma funcionalidade planejada descrita como se ja estivesse pronta.
- Testes atuais continuam passando.

## 2. Proximas fases

### Fase 2 - Baseline de qualidade e testes de API

Objetivo:

Criar uma base de testes mais forte antes de qualquer refatoracao arquitetural.

Atividades:

- Adicionar testes para `TaskController`.
- Cobrir criacao, busca, atualizacao, remocao e filtros.
- Cobrir respostas 400 e 404.
- Registrar comando padrao de verificacao.

Criterios de aceite:

- Testes novos passam junto com os existentes.
- Nenhum endpoint tem contrato alterado sem necessidade.
- Nenhuma mudanca estrutural grande.
- `.\mvnw.cmd test` passa.

Riscos:

- Testes de controller podem exigir ajuste fino de contexto Spring.
- Validacoes podem revelar inconsistencias atuais que precisarao ser tratadas em fase propria.

### Fase 3 - Limpeza incremental da aplicacao

Objetivo:

Melhorar legibilidade e responsabilidades sem mudar o comportamento externo.

Atividades:

- Extrair mapeamento de entidade para DTO.
- Reduzir responsabilidades do `TaskService`.
- Padronizar defaults e regras de atualizacao.
- Manter contratos HTTP estaveis.

Criterios de aceite:

- Testes da Fase 2 continuam passando.
- Nenhuma rota removida.
- Codigo fica mais coeso e revisavel.
- Mudancas pequenas e rastreaveis.

Riscos:

- Refatorar mapeamentos pode causar divergencias em respostas da API.
- Alterar regras de update pode mudar comportamento atual se nao houver testes suficientes.

### Fase 4 - Preparacao para Arquitetura Limpa

Objetivo:

Separar regras de negocio de detalhes de framework e infraestrutura.

Atividades:

- Definir pacotes conceituais de dominio, aplicacao, infraestrutura e interfaces de entrada.
- Introduzir casos de uso e portas.
- Adaptar persistencia Spring Data como detalhe externo.
- Preservar comportamento dos endpoints.

Criterios de aceite:

- Dominio deixa de depender diretamente de detalhes desnecessarios de framework, conforme escopo definido para a fase.
- Casos de uso ficam testaveis.
- Testes existentes e novos continuam passando.
- Documentacao arquitetural atualizada.

Riscos:

- Movimentacao de pacotes pode quebrar imports e configuracoes do Spring.
- Mudanca grande demais pode dificultar revisao.

### Fase 5 - BDD

Objetivo:

Demonstrar comportamento esperado do sistema em linguagem proxima ao negocio.

Atividades:

- Adicionar ferramenta de BDD, como Cucumber, se aprovada para o projeto.
- Criar cenarios para criar, listar, atualizar, filtrar e remover tarefas.
- Integrar cenarios ao ciclo de testes.

Criterios de aceite:

- Cenarios executaveis criados.
- Linguagem dos cenarios compreensivel para avaliador nao tecnico.
- Suite completa continua passando.

Riscos:

- BDD pode gerar manutencao extra se repetir testes de API sem valor.
- Cenarios muito detalhados podem ficar frageis.

### Fase 6 - Banco e migracoes

Objetivo:

Substituir evolucao automatica de schema por migracoes controladas.

Atividades:

- Avaliar Flyway ou Liquibase.
- Criar migracao inicial.
- Preparar perfil de banco mais proximo de producao, como PostgreSQL, se o escopo permitir.
- Manter H2 para testes quando adequado.

Criterios de aceite:

- Aplicacao sobe com migracoes.
- Testes continuam passando.
- `ddl-auto=update` deixa de ser dependencia principal para evolucao do schema.

Riscos:

- Diferencas entre H2 e PostgreSQL podem exigir ajustes.
- Migracao inicial precisa refletir corretamente o modelo atual.

### Fase 7 - Docker e Docker Compose

Objetivo:

Permitir execucao reproduzivel em containers.

Atividades:

- Criar Dockerfile para a API.
- Criar docker-compose com aplicacao e banco.
- Externalizar configuracoes.
- Documentar comandos de execucao.

Criterios de aceite:

- Aplicacao sobe via Docker.
- Banco sobe via docker-compose.
- Swagger e endpoints principais funcionam em container.
- Documentacao atualizada.

Riscos:

- Configuracao de profiles pode divergir entre local e container.
- Build de imagem pode ficar lento se nao for estruturado corretamente.

### Fase 8 - CI/CD

Objetivo:

Automatizar verificacao do projeto.

Atividades:

- Criar workflow de GitHub Actions para build e testes.
- Adicionar verificacao de qualidade/cobertura, se definido.
- Avaliar build de imagem Docker em pipeline.

Criterios de aceite:

- Pipeline executa em pull request ou push.
- Build e testes passam no ambiente remoto.
- Falhas ficam visiveis para revisao.

Riscos:

- Diferenca de versao Java entre local e CI pode quebrar build.
- Logs e configuracoes de teste podem deixar pipeline ruidoso.

### Fase 9 - Deploy

Objetivo:

Publicar uma versao demonstravel em servidor ou cloud.

Atividades:

- Escolher alvo de deploy.
- Configurar profile de producao.
- Definir variaveis de ambiente e banco.
- Documentar URL, processo e evidencias.

Criterios de aceite:

- Aplicacao acessivel publicamente ou em ambiente demonstravel.
- Swagger ou endpoint de saude disponivel.
- Processo de deploy documentado.
- Nao expor credenciais.

Riscos:

- Banco gratuito/cloud pode ter limites.
- Configuracoes sensiveis precisam ser isoladas.

### Fase 10 - Microsservicos, se necessario

Objetivo:

Demonstrar microsservicos sem superdimensionar o projeto.

Atividades:

- Avaliar fronteiras de dominio.
- Documentar decisao arquitetural.
- Extrair um servico pequeno apenas se houver justificativa.
- Demonstrar comunicacao entre servicos e execucao via compose.

Criterios de aceite:

- Decomposicao justificada.
- Servicos executam localmente.
- Comunicacao documentada.
- Testes basicos mantidos.

Riscos:

- Microsservicos podem aumentar complexidade sem ganho real.
- Observabilidade, configuracao e comunicacao podem consumir tempo desproporcional.

## 3. Ordem recomendada de execucao

1. Consolidar documentacao academica.
2. Ampliar testes automatizados.
3. Fazer limpezas pequenas protegidas por testes.
4. Preparar Arquitetura Limpa.
5. Adicionar BDD.
6. Controlar schema de banco com migracoes.
7. Containerizar com Docker/Docker Compose.
8. Automatizar CI/CD.
9. Fazer deploy.
10. Avaliar microsservicos apenas no final.

## 4. Riscos tecnicos gerais

- Refatorar arquitetura sem testes suficientes pode quebrar comportamentos ja existentes.
- Prometer funcionalidades academicas antes da implementacao pode prejudicar a rastreabilidade da entrega.
- Introduzir Docker, deploy e microsservicos cedo demais pode aumentar complexidade sem estabilizar a base.
- Diferencas entre H2, PostgreSQL, ambiente local, CI e cloud podem gerar bugs de configuracao.
- Mudancas no contrato da API podem quebrar a interface estatica atual.
- Falta de documentacao de decisoes pode dificultar defesa academica do projeto.
