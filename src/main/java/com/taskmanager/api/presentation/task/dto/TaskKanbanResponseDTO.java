package com.taskmanager.api.presentation.task.dto;

import com.taskmanager.api.application.usecase.KanbanColumn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Resposta do quadro Kanban de tarefas")
public record TaskKanbanResponseDTO(
        Integer totalTasks,
        List<TaskKanbanColumnDTO> columns
) {
    public static TaskKanbanResponseDTO fromApplication(List<KanbanColumn> columns) {
        List<TaskKanbanColumnDTO> responseColumns = columns.stream()
                .map(TaskKanbanColumnDTO::fromApplication)
                .toList();
        int totalTasks = responseColumns.stream()
                .mapToInt(TaskKanbanColumnDTO::total)
                .sum();

        return new TaskKanbanResponseDTO(totalTasks, responseColumns);
    }
}
