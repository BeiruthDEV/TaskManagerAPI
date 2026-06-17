package com.taskmanager.api.application.usecase;

import com.taskmanager.api.application.pagination.PageQuery;
import com.taskmanager.api.application.pagination.PageResult;
import com.taskmanager.api.application.port.TaskRepository;
import com.taskmanager.api.domain.model.Task;
import com.taskmanager.api.domain.model.TaskPriority;
import com.taskmanager.api.domain.model.TaskStatus;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListTasksUseCase {

    private final TaskRepository taskRepository;

    public ListTasksUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public PageResult<Task> list(PageQuery query) {
        return taskRepository.findAll(query);
    }

    @Transactional(readOnly = true)
    public List<Task> filter(TaskStatus status, TaskPriority priority) {
        if (status != null && priority != null) {
            return taskRepository.findByStatusAndPriority(status, priority);
        }
        if (status != null) {
            return taskRepository.findByStatus(status);
        }
        if (priority != null) {
            return taskRepository.findByPriority(priority);
        }
        return taskRepository.findAll();
    }
}
