package com.taskmanager.api.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.taskmanager.api.application.command.CreateTaskCommand;
import com.taskmanager.api.application.command.UpdateTaskCommand;
import com.taskmanager.api.application.factory.TaskFactory;
import com.taskmanager.api.application.pagination.PageQuery;
import com.taskmanager.api.application.pagination.PageResult;
import com.taskmanager.api.application.port.TaskRepository;
import com.taskmanager.api.application.usecase.CreateTaskUseCase;
import com.taskmanager.api.application.usecase.DeleteTaskUseCase;
import com.taskmanager.api.application.usecase.FindTaskUseCase;
import com.taskmanager.api.application.usecase.KanbanColumn;
import com.taskmanager.api.application.usecase.KanbanTasksUseCase;
import com.taskmanager.api.application.usecase.ListTasksUseCase;
import com.taskmanager.api.application.usecase.TaskDashboard;
import com.taskmanager.api.application.usecase.TaskDashboardUseCase;
import com.taskmanager.api.application.usecase.UpdateTaskUseCase;
import com.taskmanager.api.domain.exception.TaskNotFoundException;
import com.taskmanager.api.domain.model.Task;
import com.taskmanager.api.domain.model.TaskPriority;
import com.taskmanager.api.domain.model.TaskStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    private TaskService taskService;
    private TaskFactory taskFactory;

    @BeforeEach
    void setUp() {
        taskFactory = new TaskFactory();
        taskService = new TaskService(
                new CreateTaskUseCase(taskRepository, taskFactory),
                new UpdateTaskUseCase(taskRepository),
                new DeleteTaskUseCase(taskRepository),
                new FindTaskUseCase(taskRepository),
                new ListTasksUseCase(taskRepository),
                new KanbanTasksUseCase(taskRepository),
                new TaskDashboardUseCase(taskRepository)
        );
    }

    @Test
    void shouldCreateTaskWhenCommandIsValid() {
        CreateTaskCommand command = new CreateTaskCommand(
                "Criar API",
                "Implementar endpoints",
                null,
                null,
                null,
                null,
                null,
                LocalDate.now().plusDays(1)
        );

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setId(1L);
            task.setCreatedAt(LocalDateTime.now());
            task.setUpdatedAt(LocalDateTime.now());
            return task;
        });

        Task response = taskService.create(command);

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(taskCaptor.capture());

        assertThat(taskCaptor.getValue().getStatus()).isEqualTo(TaskStatus.PENDENTE);
        assertThat(taskCaptor.getValue().getPriority()).isEqualTo(TaskPriority.MEDIA);
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Criar API");
    }

    @Test
    void shouldUpdateTaskWhenTaskExists() {
        Task task = new Task("Titulo antigo", "Descricao antiga", "Lisa Kim", "Compliance", 30,
                TaskStatus.PENDENTE, TaskPriority.BAIXA, null);
        task.setId(1L);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        UpdateTaskCommand command = new UpdateTaskCommand(null, null, null, null, 95,
                TaskStatus.CONCLUIDO, TaskPriority.ALTA, null);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task response = taskService.update(1L, command);

        assertThat(response.getTitle()).isEqualTo("Titulo antigo");
        assertThat(response.getDescription()).isEqualTo("Descricao antiga");
        assertThat(response.getProgress()).isEqualTo(95);
        assertThat(response.getStatus()).isEqualTo(TaskStatus.CONCLUIDO);
        assertThat(response.getPriority()).isEqualTo(TaskPriority.ALTA);
    }

    @Test
    void shouldFindTaskWhenTaskExists() {
        Task task = new Task("Encontrar", null, null, null, null, null, null, null);
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task response = taskService.findById(1L);

        assertThat(response).isSameAs(task);
    }

    @Test
    void shouldThrowExceptionWhenTaskDoesNotExist() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.findById(99L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldDeleteTaskWhenTaskExists() {
        Task task = new Task("Remover", null, null, null, null, null, null, null);
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.delete(1L);

        verify(taskRepository).delete(task);
    }

    @Test
    void shouldListTasksWhenPageQueryIsProvided() {
        PageQuery query = PageQuery.of(0, 10);
        Task task = new Task("Listar", null, null, null, null, null, null, null);
        PageResult<Task> expected = PageResult.of(List.of(task), query, 1);
        when(taskRepository.findAll(query)).thenReturn(expected);

        PageResult<Task> result = taskService.findAll(query);

        assertThat(result.content()).containsExactly(task);
        assertThat(result.totalElements()).isEqualTo(1);
    }

    @Test
    void shouldFilterTasksWhenStatusAndPriorityAreProvided() {
        Task task = new Task("Filtrar", null, null, null, null,
                TaskStatus.PENDENTE, TaskPriority.ALTA, null);
        when(taskRepository.findByStatusAndPriority(TaskStatus.PENDENTE, TaskPriority.ALTA))
                .thenReturn(List.of(task));

        List<Task> result = taskService.filter(TaskStatus.PENDENTE, TaskPriority.ALTA);

        assertThat(result).containsExactly(task);
    }

    @Test
    void shouldReturnKanbanBoardGroupedByStatus() {
        Task pending = new Task("Pendente", null, null, null, null,
                TaskStatus.PENDENTE, TaskPriority.ALTA, null);
        Task done = new Task("Concluida", null, null, null, null,
                TaskStatus.CONCLUIDO, TaskPriority.MEDIA, null);
        when(taskRepository.findAll()).thenReturn(List.of(pending, done));

        List<KanbanColumn> board = taskService.getKanbanBoard();

        assertThat(board).hasSize(TaskStatus.values().length);
        assertThat(board.get(0).tasks()).containsExactly(pending);
        assertThat(board.get(3).tasks()).containsExactly(done);
    }

    @Test
    void shouldReturnTaskDashboardIndicators() {
        Task pending = new Task("Pendente", null, null, null, null,
                TaskStatus.PENDENTE, TaskPriority.ALTA, LocalDate.now().minusDays(1));
        Task done = new Task("Concluida", null, null, null, null,
                TaskStatus.CONCLUIDO, TaskPriority.MEDIA, LocalDate.now());
        when(taskRepository.findAll()).thenReturn(List.of(pending, done));

        TaskDashboard dashboard = taskService.getDashboard();

        assertThat(dashboard.totalTasks()).isEqualTo(2);
        assertThat(dashboard.overdueTasks()).isEqualTo(1);
        assertThat(dashboard.completedTasks()).isEqualTo(1);
        assertThat(dashboard.completionRate()).isEqualTo(50.0);
    }
}
