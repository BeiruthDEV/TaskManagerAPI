package com.taskmanager.api.task.dto;

import com.taskmanager.api.task.Task;
import com.taskmanager.api.task.TaskPriority;
import com.taskmanager.api.task.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Representacao de uma tarefa")
public record TaskResponseDTO(
        Long id,
        String title,
        String description,
        String assignee,
        String projectName,
        Integer progress,
        TaskStatus status,
        TaskPriority priority,
        LocalDate dueDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static TaskResponseDTO fromEntity(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getAssignee(),
                task.getProjectName(),
                task.getProgress(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
