package com.taskmanager.api.application.usecase;

import com.taskmanager.api.domain.model.TaskPriority;
import com.taskmanager.api.domain.model.TaskStatus;
import java.util.Map;

public record TaskDashboard(
        long totalTasks,
        Map<TaskStatus, Long> tasksByStatus,
        Map<TaskPriority, Long> tasksByPriority,
        long overdueTasks,
        long completedTasks,
        double completionRate
) {
}
