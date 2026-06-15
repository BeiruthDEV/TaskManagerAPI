package com.taskmanager.api.infrastructure.persistence;

import com.taskmanager.api.domain.model.Task;

final class TaskJpaMapper {

    private TaskJpaMapper() {
    }

    static Task toDomain(TaskJpaEntity entity) {
        return new Task(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getAssignee(),
                entity.getProjectName(),
                entity.getProgress(),
                entity.getStatus(),
                entity.getPriority(),
                entity.getDueDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    static TaskJpaEntity toEntity(Task task) {
        return new TaskJpaEntity(
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
