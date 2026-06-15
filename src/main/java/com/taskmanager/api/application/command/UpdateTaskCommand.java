package com.taskmanager.api.application.command;

import com.taskmanager.api.domain.model.TaskPriority;
import com.taskmanager.api.domain.model.TaskStatus;
import java.time.LocalDate;

public record UpdateTaskCommand(
        String title,
        String description,
        String assignee,
        String projectName,
        Integer progress,
        TaskStatus status,
        TaskPriority priority,
        LocalDate dueDate
) {
}
