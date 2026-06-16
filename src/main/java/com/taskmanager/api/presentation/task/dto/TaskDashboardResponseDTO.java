package com.taskmanager.api.presentation.task.dto;

import com.taskmanager.api.application.usecase.TaskDashboard;
import com.taskmanager.api.domain.model.TaskPriority;
import com.taskmanager.api.domain.model.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;

@Schema(description = "Indicadores agregados do dashboard de tarefas")
public record TaskDashboardResponseDTO(
        Long totalTasks,
        Map<TaskStatus, Long> tasksByStatus,
        Map<TaskPriority, Long> tasksByPriority,
        Long overdueTasks,
        Long completedTasks,
        Double completionRate
) {
    public static TaskDashboardResponseDTO fromApplication(TaskDashboard dashboard) {
        return new TaskDashboardResponseDTO(
                dashboard.totalTasks(),
                dashboard.tasksByStatus(),
                dashboard.tasksByPriority(),
                dashboard.overdueTasks(),
                dashboard.completedTasks(),
                dashboard.completionRate()
        );
    }
}
