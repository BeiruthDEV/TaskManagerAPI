# Proposta academica do projeto Trackio

## 1. Descricao do problema

Equipes pequenas precisam acompanhar atividades do dia a dia, responsaveis, prioridades, prazos e progresso. Em muitos casos, ferramentas completas de gestao podem ser complexas demais para uma equipe pequena ou para um projeto academico de demonstracao tecnica.

O problema escolhido e a organizacao de tarefas em uma plataforma simples, com foco em clareza operacional e evolucao tecnica controlada. A aplicacao deve permitir que uma equipe registre tarefas, acompanhe seu andamento e consulte informacoes essenciais sem depender inicialmente de um ecossistema grande.

## 2. Objetivo da solucao

O objetivo da solucao e evoluir o Trackio como uma plataforma de gerenciamento de tarefas para equipes pequenas. O sistema parte de uma API REST ja funcional e sera adaptado em fases para demonstrar praticas e conceitos exigidos no trabalho academico.

A solucao nao pretende iniciar como um produto completo de produtividade. Nesta entrega academica, o foco e demonstrar evolucao tecnica incremental, preservando funcionamento, testes e rastreabilidade das decisoes.

## 3. Publico-alvo

O publico-alvo conceitual e composto por:

- Equipes pequenas que precisam organizar tarefas.
- Alunos e avaliadores interessados em observar a aplicacao pratica de boas praticas de engenharia.
- Pequenos projetos internos que precisam de uma API simples para acompanhamento de atividades.

## 4. Funcionalidades principais

Funcionalidades existentes no estado atual:

- Criacao de tarefas.
- Listagem paginada de tarefas.
- Busca de tarefa por identificador.
- Atualizacao parcial ou completa de tarefas.
- Remocao de tarefas.
- Filtro por status e prioridade.
- Registro de responsavel, projeto, progresso, prioridade, status e prazo.
- Documentacao da API com Swagger/OpenAPI.
- Interface estatica inicial servida pela propria aplicacao.

Funcionalidades que podem ser consideradas em fases futuras, sem estarem implementadas nesta fase:

- Autenticacao e autorizacao.
- Organizacao por equipes ou workspaces.
- Quadros Kanban.
- Relatorios e metricas.
- Notificacoes.
- Separacao de servicos, se houver justificativa arquitetural e academica.

## 5. Justificativa tecnica inicial

O projeto atual e adequado como base academica porque ja possui um dominio simples, compreensivel e demonstravel. A existencia de uma API REST funcional permite aplicar tecnicas de melhoria sem iniciar do zero.

A stack Java com Spring Boot favorece a demonstracao de:

- Separacao em camadas.
- Injecao de dependencias.
- Testes automatizados.
- Validacao de entrada.
- Persistencia com JPA.
- Documentacao de API.
- Evolucao para Docker, CI/CD e deploy.

A estrategia recomendada e incremental: primeiro documentar e proteger o comportamento existente com testes, depois refatorar a estrutura interna, e somente depois introduzir arquitetura limpa, BDD, containerizacao, deploy e possivel decomposicao em microsservicos.

## 6. Relacao entre o projeto e os criterios da prova

### Clean Code

Estado atual:

- O projeto possui nomes de classes e metodos compreensiveis.
- Ha DTOs para entrada e saida da API.
- Ha validacoes declarativas nos DTOs.
- Ha tratamento global de excecoes.

Adaptacoes planejadas:

- Reduzir responsabilidades acumuladas no service.
- Padronizar mapeamentos.
- Organizar regras de dominio de forma mais explicita.
- Melhorar documentacao e consistencia textual.

Evidencias existentes:

- `TaskController`, `TaskService`, DTOs e `GlobalExceptionHandler`.

Evidencias a criar:

- Testes adicionais, documentacao das decisoes e possiveis metricas de qualidade.

### SOLID

Estado atual:

- Existe separacao inicial entre controller, service e repository.
- Dependencias sao injetadas por construtor.

Adaptacoes planejadas:

- Introduzir contratos para casos de uso e persistencia.
- Reduzir dependencia direta da aplicacao em detalhes de infraestrutura.
- Manter responsabilidades menores e mais explicitas.

Evidencias existentes:

- Estrutura atual em controller, service e repository.

Evidencias a criar:

- Interfaces de portas/use cases e testes que validem comportamento sem depender diretamente do framework.

### Design Patterns

Estado atual:

- O projeto ja utiliza padroes comuns do ecossistema Spring: Repository, DTO, Dependency Injection e Exception Handler.

Adaptacoes planejadas:

- Tornar os padroes mais intencionais e documentados.
- Avaliar Mapper para conversao entre dominio e DTO.
- Avaliar Ports and Adapters na preparacao para Arquitetura Limpa.
- Avaliar Specification ou estrategia equivalente para filtros, se houver complexidade real.

Evidencias existentes:

- `TaskRepository`, DTOs e injecao de dependencias.

Evidencias a criar:

- Documentacao de padroes escolhidos e implementacoes pequenas, justificadas por necessidade real.

### TDD

Estado atual:

- Existem testes automatizados para service e repository.
- A suite atual passa com Maven.

Adaptacoes planejadas:

- Criar novos testes antes de refatoracoes.
- Cobrir controller, validacoes e erros.
- Usar os testes como rede de seguranca para mudancas incrementais.

Evidencias existentes:

- `TaskServiceTest`, `TaskRepositoryTest` e `TaskManagerApiApplicationTests`.

Evidencias a criar:

- Testes de API/controller, cenarios de erro e possivel relatorio de cobertura.

### BDD

Estado atual:

- Ainda nao ha BDD implementado.

Adaptacoes planejadas:

- Criar cenarios de comportamento para os fluxos principais de tarefas.
- Usar linguagem de negocio para expressar os comportamentos esperados.
- Integrar os cenarios a testes executaveis.

Evidencias existentes:

- Nenhuma evidencia executavel de BDD nesta fase.

Evidencias a criar:

- Arquivos `.feature`, steps e execucao automatizada dos cenarios.

### Arquitetura Limpa

Estado atual:

- O projeto possui camadas basicas, mas ainda nao segue Arquitetura Limpa formalmente.
- Dominio, DTOs HTTP, JPA e servico de aplicacao ainda estao fortemente proximos.

Adaptacoes planejadas:

- Separar dominio, aplicacao, infraestrutura e interfaces de entrada.
- Criar portas de entrada e saida.
- Manter Spring e JPA como detalhes externos.

Evidencias existentes:

- Separacao inicial entre controller, service e repository.

Evidencias a criar:

- Pacotes/camadas reorganizados, contratos e testes por caso de uso.

### Microsservicos

Estado atual:

- O projeto e um monolito.
- Nao ha microsservicos implementados.

Adaptacoes planejadas:

- Avaliar se a decomposicao e necessaria e qual fronteira faz sentido.
- Evitar criar microsservicos prematuramente.
- Demonstrar decomposicao pequena apenas apos estabilizar testes, arquitetura e Docker.

Evidencias existentes:

- Nenhuma evidencia de microsservicos nesta fase.

Evidencias a criar:

- Documento de decisao arquitetural e, se aplicavel, um servico pequeno com comunicacao demonstravel.

### Docker/Docker Compose

Estado atual:

- Nao ha Dockerfile nem docker-compose.

Adaptacoes planejadas:

- Criar Dockerfile para a API.
- Criar docker-compose com aplicacao e banco.
- Externalizar configuracoes via variaveis de ambiente.

Evidencias existentes:

- Nenhuma evidencia de containerizacao nesta fase.

Evidencias a criar:

- `Dockerfile`, `docker-compose.yml` e instrucao de execucao documentada.

### Deploy em servidor/cloud

Estado atual:

- Nao ha configuracao de deploy identificada.

Adaptacoes planejadas:

- Definir ambiente de deploy simples.
- Preparar profile de producao.
- Configurar variaveis, banco e health check.
- Documentar a URL publicada e o processo de deploy.

Evidencias existentes:

- Nenhuma evidencia de deploy nesta fase.

Evidencias a criar:

- Configuracao de deploy, pipeline ou instrucoes reproduziveis, alem de evidencia da aplicacao publicada.
