package com.taskmanager.api.application.usecase;

import com.taskmanager.api.domain.model.Task;
import com.taskmanager.api.domain.model.TaskStatus;
import java.util.List;

public record KanbanColumn(
        TaskStatus status,
        List<Task> tasks
) {
    public int total() {
        return tasks.size();
    }
}
