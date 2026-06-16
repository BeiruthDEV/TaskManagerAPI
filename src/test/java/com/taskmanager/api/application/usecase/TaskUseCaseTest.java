package com.taskmanager.api.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.taskmanager.api.application.command.CreateTaskCommand;
import com.taskmanager.api.application.command.UpdateTaskCommand;
import com.taskmanager.api.application.factory.TaskFactory;
import com.taskmanager.api.application.port.TaskRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskUseCaseTest {

    @Mock
    private TaskRepository taskRepository;

    private CreateTaskUseCase createTaskUseCase;
    private UpdateTaskUseCase updateTaskUseCase;
    private DeleteTaskUseCase deleteTaskUseCase;
    private FindTaskUseCase findTaskUseCase;
    private ListTasksUseCase listTasksUseCase;
    private KanbanTasksUseCase kanbanTasksUseCase;

    @BeforeEach
    void setUp() {
        createTaskUseCase = new CreateTaskUseCase(taskRepository, new TaskFactory());
        updateTaskUseCase = new UpdateTaskUseCase(taskRepository);
        deleteTaskUseCase = new DeleteTaskUseCase(taskRepository);
        findTaskUseCase = new FindTaskUseCase(taskRepository);
        listTasksUseCase = new ListTasksUseCase(taskRepository);
        kanbanTasksUseCase = new KanbanTasksUseCase(taskRepository);
    }

    @Test
    void shouldCreateTaskWhenCommandIsValid() {
        LocalDate dueDate = LocalDate.now().plusDays(2);
        CreateTaskCommand command = new CreateTaskCommand(
                "Criar API",
                "Implementar endpoints",
                "Lisa Kim",
                "Trackio",
                40,
                TaskStatus.EM_PROGRESSO,
                TaskPriority.ALTA,
                dueDate
        );

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setId(1L);
            task.setCreatedAt(LocalDateTime.now());
            task.setUpdatedAt(LocalDateTime.now());
            return task;
        });

        Task createdTask = createTaskUseCase.create(command);

        assertThat(createdTask.getId()).isEqualTo(1L);
        assertThat(createdTask.getTitle()).isEqualTo("Criar API");
        assertThat(createdTask.getStatus()).isEqualTo(TaskStatus.EM_PROGRESSO);
        assertThat(createdTask.getPriority()).isEqualTo(TaskPriority.ALTA);
        assertThat(createdTask.getDueDate()).isEqualTo(dueDate);
    }

    @Test
    void shouldCreateTaskPreservingExpectedDefaultsWhenStatusAndPriorityAreNull() {
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

        Task createdTask = createTaskUseCase.create(command);

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(taskCaptor.capture());

        assertThat(taskCaptor.getValue().getStatus()).isEqualTo(TaskStatus.PENDENTE);
        assertThat(taskCaptor.getValue().getPriority()).isEqualTo(TaskPriority.MEDIA);
        assertThat(createdTask.getId()).isEqualTo(1L);
        assertThat(createdTask.getTitle()).isEqualTo("Criar API");
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

        Task updatedTask = updateTaskUseCase.update(1L, command);

        assertThat(updatedTask.getTitle()).isEqualTo("Titulo antigo");
        assertThat(updatedTask.getDescription()).isEqualTo("Descricao antiga");
        assertThat(updatedTask.getProgress()).isEqualTo(95);
        assertThat(updatedTask.getStatus()).isEqualTo(TaskStatus.CONCLUIDO);
        assertThat(updatedTask.getPriority()).isEqualTo(TaskPriority.ALTA);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingTaskDoesNotExist() {
        UpdateTaskCommand command = new UpdateTaskCommand("Novo titulo", null, null, null, null,
                null, null, null);
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateTaskUseCase.update(99L, command))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldFindTaskWhenTaskExists() {
        Task task = new Task("Encontrar", null, null, null, null, null, null, null);
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task foundTask = findTaskUseCase.findById(1L);

        assertThat(foundTask).isSameAs(task);
    }

    @Test
    void shouldThrowExceptionWhenTaskDoesNotExist() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> findTaskUseCase.findById(99L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldDeleteTaskWhenTaskExists() {
        Task task = new Task("Remover", null, null, null, null, null, null, null);
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        deleteTaskUseCase.delete(1L);

        verify(taskRepository).delete(task);
    }

    @Test
    void shouldThrowExceptionWhenDeletingTaskDoesNotExist() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteTaskUseCase.delete(99L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void shouldListTasksWhenPageableIsProvided() {
        Pageable pageable = PageRequest.of(0, 10);
        Task task = new Task("Listar", null, null, null, null, null, null, null);
        Page<Task> expectedPage = new PageImpl<>(List.of(task), pageable, 1);
        when(taskRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Task> result = listTasksUseCase.list(pageable);

        assertThat(result.getContent()).containsExactly(task);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldFilterTasksByStatusAndPriority() {
        Task task = new Task("Filtrar", null, null, null, null,
                TaskStatus.PENDENTE, TaskPriority.ALTA, null);
        when(taskRepository.findByStatusAndPriority(TaskStatus.PENDENTE, TaskPriority.ALTA))
                .thenReturn(List.of(task));

        List<Task> result = listTasksUseCase.filter(TaskStatus.PENDENTE, TaskPriority.ALTA);

        assertThat(result).containsExactly(task);
    }

    @Test
    void shouldListAllTasksWhenNoFilterIsProvided() {
        Task task = new Task("Sem filtro", null, null, null, null, null, null, null);
        when(taskRepository.findAll()).thenReturn(List.of(task));

        List<Task> result = listTasksUseCase.filter(null, null);

        assertThat(result).containsExactly(task);
    }

    @Test
    void shouldGroupTasksByStatusForKanbanBoard() {
        Task pending = new Task("Pendente", null, null, null, null,
                TaskStatus.PENDENTE, TaskPriority.ALTA, null);
        Task inProgress = new Task("Em progresso", null, null, null, null,
                TaskStatus.EM_PROGRESSO, TaskPriority.MEDIA, null);
        Task review = new Task("Revisao", null, null, null, null,
                TaskStatus.EM_REVISAO, TaskPriority.BAIXA, null);
        when(taskRepository.findAll()).thenReturn(List.of(pending, inProgress, review));

        List<KanbanColumn> board = kanbanTasksUseCase.getBoard();

        assertThat(board).hasSize(TaskStatus.values().length);
        assertThat(board.get(0).status()).isEqualTo(TaskStatus.PENDENTE);
        assertThat(board.get(0).tasks()).containsExactly(pending);
        assertThat(board.get(1).status()).isEqualTo(TaskStatus.EM_PROGRESSO);
        assertThat(board.get(1).tasks()).containsExactly(inProgress);
        assertThat(board.get(2).status()).isEqualTo(TaskStatus.EM_REVISAO);
        assertThat(board.get(2).tasks()).containsExactly(review);
        assertThat(board.get(3).status()).isEqualTo(TaskStatus.CONCLUIDO);
        assertThat(board.get(3).tasks()).isEmpty();
    }
}
