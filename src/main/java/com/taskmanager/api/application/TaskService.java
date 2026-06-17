package com.taskmanager.api.application;

import com.taskmanager.api.application.command.CreateTaskCommand;
import com.taskmanager.api.application.command.UpdateTaskCommand;
import com.taskmanager.api.application.pagination.PageQuery;
import com.taskmanager.api.application.pagination.PageResult;
import com.taskmanager.api.application.usecase.CreateTaskUseCase;
import com.taskmanager.api.application.usecase.DeleteTaskUseCase;
import com.taskmanager.api.application.usecase.FindTaskUseCase;
import com.taskmanager.api.application.usecase.KanbanColumn;
import com.taskmanager.api.application.usecase.KanbanTasksUseCase;
import com.taskmanager.api.application.usecase.ListTasksUseCase;
import com.taskmanager.api.application.usecase.TaskDashboard;
import com.taskmanager.api.application.usecase.TaskDashboardUseCase;
import com.taskmanager.api.application.usecase.UpdateTaskUseCase;
import com.taskmanager.api.domain.model.Task;
import com.taskmanager.api.domain.model.TaskPriority;
import com.taskmanager.api.domain.model.TaskStatus;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final CreateTaskUseCase createTaskUseCase;
    private final UpdateTaskUseCase updateTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;
    private final FindTaskUseCase findTaskUseCase;
    private final ListTasksUseCase listTasksUseCase;
    private final KanbanTasksUseCase kanbanTasksUseCase;
    private final TaskDashboardUseCase taskDashboardUseCase;

    public TaskService(
            CreateTaskUseCase createTaskUseCase,
            UpdateTaskUseCase updateTaskUseCase,
            DeleteTaskUseCase deleteTaskUseCase,
            FindTaskUseCase findTaskUseCase,
            ListTasksUseCase listTasksUseCase,
            KanbanTasksUseCase kanbanTasksUseCase,
            TaskDashboardUseCase taskDashboardUseCase
    ) {
        this.createTaskUseCase = createTaskUseCase;
        this.updateTaskUseCase = updateTaskUseCase;
        this.deleteTaskUseCase = deleteTaskUseCase;
        this.findTaskUseCase = findTaskUseCase;
        this.listTasksUseCase = listTasksUseCase;
        this.kanbanTasksUseCase = kanbanTasksUseCase;
        this.taskDashboardUseCase = taskDashboardUseCase;
    }

    public PageResult<Task> findAll(PageQuery query) {
        return listTasksUseCase.list(query);
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

    public List<KanbanColumn> getKanbanBoard() {
        return kanbanTasksUseCase.getBoard();
    }

    public TaskDashboard getDashboard() {
        return taskDashboardUseCase.getDashboard();
    }
}
