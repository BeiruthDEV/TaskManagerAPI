package com.taskmanager.api.infrastructure.config;

import com.taskmanager.api.application.port.TaskRepository;
import com.taskmanager.api.domain.model.Task;
import com.taskmanager.api.domain.model.TaskPriority;
import com.taskmanager.api.domain.model.TaskStatus;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevDataLoader {

    @Bean
    CommandLineRunner loadDemoTasks(TaskRepository taskRepository) {
        return args -> {
            if (taskRepository.count() > 0) {
                return;
            }

            taskRepository.save(new Task("Mapear funil comercial atual", "Documentar etapas, responsáveis e principais perdas do funil da Atlas.",
                    "Ana Souza", "Implantação do CRM", 90, TaskStatus.EM_REVISAO, TaskPriority.ALTA, LocalDate.of(2026, 6, 12)));
            taskRepository.save(new Task("Configurar campos obrigatórios do CRM", "Criar campos de cliente, segmento, ticket estimado e origem da oportunidade.",
                    "Bruno Lima", "Implantação do CRM", 60, TaskStatus.EM_PROGRESSO, TaskPriority.ALTA, LocalDate.of(2026, 6, 20)));
            taskRepository.save(new Task("Importar base inicial de clientes", "Preparar planilha final de clientes ativos para carga assistida no CRM.",
                    "Carla Mendes", "Implantação do CRM", 10, TaskStatus.PENDENTE, TaskPriority.MEDIA, LocalDate.of(2026, 6, 24)));
            taskRepository.save(new Task("Validar fluxo de atendimento no CRM", "Conferir abertura, encaminhamento e encerramento de chamados ligados a clientes.",
                    "Diego Rocha", "Implantação do CRM", 100, TaskStatus.CONCLUIDO, TaskPriority.MEDIA, LocalDate.of(2026, 6, 7)));

            taskRepository.save(new Task("Definir lista de contas estratégicas", "Selecionar empresas prioritárias para prospecção comercial no trimestre.",
                    "Ana Souza", "Campanha Comercial Q2", 100, TaskStatus.CONCLUIDO, TaskPriority.ALTA, LocalDate.of(2026, 6, 3)));
            taskRepository.save(new Task("Preparar roteiro de abordagem comercial", "Criar roteiro de ligação, e-mail inicial e objeções frequentes.",
                    "Ana Souza", "Campanha Comercial Q2", 82, TaskStatus.EM_REVISAO, TaskPriority.MEDIA, LocalDate.of(2026, 6, 17)));
            taskRepository.save(new Task("Criar painel de acompanhamento da campanha", "Montar visão de oportunidades, reuniões agendadas e taxa de conversão.",
                    "Bruno Lima", "Campanha Comercial Q2", 45, TaskStatus.EM_PROGRESSO, TaskPriority.MEDIA, LocalDate.of(2026, 6, 26)));
            taskRepository.save(new Task("Revisar proposta padrão de serviços", "Atualizar escopo, condições comerciais e prazos de implantação.",
                    "Carla Mendes", "Campanha Comercial Q2", 0, TaskStatus.PENDENTE, TaskPriority.BAIXA, LocalDate.of(2026, 7, 1)));

            taskRepository.save(new Task("Conferir notas fiscais recebidas", "Validar documentos de fornecedores antes do fechamento mensal.",
                    "Carla Mendes", "Fechamento Financeiro Mensal", 100, TaskStatus.CONCLUIDO, TaskPriority.ALTA, LocalDate.of(2026, 6, 5)));
            taskRepository.save(new Task("Conciliar extratos bancários", "Comparar entradas, saídas e tarifas com o controle financeiro interno.",
                    "Carla Mendes", "Fechamento Financeiro Mensal", 70, TaskStatus.EM_PROGRESSO, TaskPriority.ALTA, LocalDate.of(2026, 6, 14)));
            taskRepository.save(new Task("Revisar centro de custos por setor", "Garantir alocação correta de despesas entre Tecnologia, Comercial, RH e Atendimento.",
                    "Carla Mendes", "Fechamento Financeiro Mensal", 88, TaskStatus.EM_REVISAO, TaskPriority.MEDIA, LocalDate.of(2026, 6, 18)));
            taskRepository.save(new Task("Publicar relatório financeiro mensal", "Disponibilizar resumo executivo de receitas, despesas e margem operacional.",
                    "Carla Mendes", "Fechamento Financeiro Mensal", 5, TaskStatus.PENDENTE, TaskPriority.ALTA, LocalDate.of(2026, 6, 28)));

            taskRepository.save(new Task("Preparar kit de boas-vindas", "Organizar materiais, acessos iniciais e comunicado para novos colaboradores.",
                    "Fernanda Alves", "Onboarding de Colaboradores", 100, TaskStatus.CONCLUIDO, TaskPriority.BAIXA, LocalDate.of(2026, 6, 6)));
            taskRepository.save(new Task("Agendar trilha de integração", "Marcar encontros com Comercial, Financeiro, Tecnologia e Atendimento.",
                    "Fernanda Alves", "Onboarding de Colaboradores", 55, TaskStatus.EM_PROGRESSO, TaskPriority.MEDIA, LocalDate.of(2026, 6, 19)));
            taskRepository.save(new Task("Revisar checklist do primeiro mês", "Validar atividades obrigatórias e critérios de conclusão do onboarding.",
                    "Fernanda Alves", "Onboarding de Colaboradores", 94, TaskStatus.EM_REVISAO, TaskPriority.MEDIA, LocalDate.of(2026, 6, 21)));
            taskRepository.save(new Task("Criar pesquisa de experiência inicial", "Definir perguntas para medir clareza, suporte e adaptação dos novos colaboradores.",
                    "Fernanda Alves", "Onboarding de Colaboradores", 0, TaskStatus.PENDENTE, TaskPriority.BAIXA, LocalDate.of(2026, 7, 4)));

            taskRepository.save(new Task("Classificar motivos de chamados recorrentes", "Agrupar demandas mais comuns para identificar gargalos do atendimento.",
                    "Diego Rocha", "Reestruturação do Atendimento", 100, TaskStatus.CONCLUIDO, TaskPriority.MEDIA, LocalDate.of(2026, 6, 4)));
            taskRepository.save(new Task("Redesenhar fluxo de triagem", "Definir critérios para prioridade, encaminhamento e tempo de primeira resposta.",
                    "Diego Rocha", "Reestruturação do Atendimento", 85, TaskStatus.EM_REVISAO, TaskPriority.ALTA, LocalDate.of(2026, 6, 16)));
            taskRepository.save(new Task("Configurar respostas rápidas da equipe", "Padronizar mensagens para dúvidas frequentes e solicitações simples.",
                    "Diego Rocha", "Reestruturação do Atendimento", 30, TaskStatus.EM_PROGRESSO, TaskPriority.BAIXA, LocalDate.of(2026, 6, 23)));
            taskRepository.save(new Task("Treinar equipe no novo processo de atendimento", "Conduzir sessão prática sobre triagem, registro e escalonamento.",
                    "Fernanda Alves", "Reestruturação do Atendimento", 8, TaskStatus.PENDENTE, TaskPriority.ALTA, LocalDate.of(2026, 6, 27)));
        };
    }
}
