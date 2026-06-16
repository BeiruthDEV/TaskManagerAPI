package com.taskmanager.api.presentation.task.dto;

import com.taskmanager.api.application.usecase.KanbanColumn;
import com.taskmanager.api.domain.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Coluna de tarefas agrupadas por status no Kanban")
public record TaskKanbanColumnDTO(
        TaskStatus status,
        String title,
        Integer total,
        List<TaskResponseDTO> tasks
) {
    public static TaskKanbanColumnDTO fromApplication(KanbanColumn column) {
        return new TaskKanbanColumnDTO(
                column.status(),
                formatTitle(column.status()),
                column.total(),
                column.tasks().stream()
                        .map(TaskResponseDTO::fromDomain)
                        .toList()
        );
    }

    private static String formatTitle(TaskStatus status) {
        return switch (status) {
            case PENDENTE -> "Pendente";
            case EM_PROGRESSO -> "Em progresso";
            case EM_REVISAO -> "Em revisao";
            case CONCLUIDO -> "Concluido";
        };
    }
}
