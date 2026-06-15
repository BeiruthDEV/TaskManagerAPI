package com.taskmanager.api.application;

import com.taskmanager.api.application.command.CreateTaskCommand;
import com.taskmanager.api.application.command.UpdateTaskCommand;
import com.taskmanager.api.application.usecase.CreateTaskUseCase;
import com.taskmanager.api.application.usecase.DeleteTaskUseCase;
import com.taskmanager.api.application.usecase.FindTaskUseCase;
import com.taskmanager.api.application.usecase.ListTasksUseCase;
import com.taskmanager.api.application.usecase.UpdateTaskUseCase;
import com.taskmanager.api.domain.model.Task;
import com.taskmanager.api.domain.model.TaskPriority;
import com.taskmanager.api.domain.model.TaskStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final CreateTaskUseCase createTaskUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;
    private final FindTaskUseCase findTaskUseCase;
    private final ListTasksUseCase listTasksUseCase;

    public TaskService(
            CreateTaskUseCase createTaskUseCase,
            UpdateTaskUseCase updateTaskUseCase,
            DeleteTaskUseCase deleteTaskUseCase,
            FindTaskUseCase findTaskUseCase,
            ListTasksUseCase listTasksUseCase
    ) {
        this.createTaskUseCase = createTaskUseCase;
        this.updateTaskUseCase = updateTaskUseCase;
        this.deleteTaskUseCase = deleteTaskUseCase;
        this.findTaskUseCase = findTaskUseCase;
        this.listTasksUseCase = listTasksUseCase;
    }

    public Page<Task> findAll(Pageable pageable) {
        return listTasksUseCase.list(pageable);
    }

    public Task findById(Long id) {
        return findTaskUseCase.findById(id);
    }

    public Task create(CreateTaskCommand command) {
        return createTaskUseCase.create(command);
    }

    public Task update(Long id, UpdateTaskCommand command) {
        return updateTaskUseCase.update(id, command);
    }

    public void delete(Long id) {
        deleteTaskUseCase.delete(id);
    }

    public List<Task> filter(TaskStatus status, TaskPriority priority) {
        return listTasksUseCase.filter(status, priority);
    }
}
