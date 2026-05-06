package com.taskmanager.api.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.taskmanager.api.exception.TaskNotFoundException;
import com.taskmanager.api.task.dto.TaskCreateDTO;
import com.taskmanager.api.task.dto.TaskResponseDTO;
import com.taskmanager.api.task.dto.TaskUpdateDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository);
    }

    @Test
    void shouldCreateTaskWithDefaultsWhenStatusAndPriorityAreNull() {
        TaskCreateDTO request = new TaskCreateDTO(
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

        TaskResponseDTO response = taskService.create(request);

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(taskCaptor.capture());

        assertThat(taskCaptor.getValue().getStatus()).isEqualTo(TaskStatus.PENDENTE);
        assertThat(taskCaptor.getValue().getPriority()).isEqualTo(TaskPriority.MEDIA);
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Criar API");
    }

    @Test
    void shouldPartiallyUpdateTask() {
        Task task = new Task("Titulo antigo", "Descricao antiga", "Lisa Kim", "Compliance", 30,
                TaskStatus.PENDENTE, TaskPriority.BAIXA, null);
        task.setId(1L);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        TaskUpdateDTO request = new TaskUpdateDTO(null, null, null, null, 95,
                TaskStatus.CONCLUIDO, TaskPriority.ALTA, null);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponseDTO response = taskService.update(1L, request);

        assertThat(response.title()).isEqualTo("Titulo antigo");
        assertThat(response.description()).isEqualTo("Descricao antiga");
        assertThat(response.progress()).isEqualTo(95);
        assertThat(response.status()).isEqualTo(TaskStatus.CONCLUIDO);
        assertThat(response.priority()).isEqualTo(TaskPriority.ALTA);
    }

    @Test
    void shouldThrowWhenTaskDoesNotExist() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.findById(99L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("99");
    }
}
