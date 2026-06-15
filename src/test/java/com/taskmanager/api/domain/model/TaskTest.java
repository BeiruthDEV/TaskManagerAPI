package com.taskmanager.api.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void shouldCreateTaskWithDefaultStatusPriorityAndProgress() {
        Task task = new Task("Criar API", null, null, null, null, null, null, null);

        assertThat(task.getProgress()).isZero();
        assertThat(task.getStatus()).isEqualTo(TaskStatus.PENDENTE);
        assertThat(task.getPriority()).isEqualTo(TaskPriority.MEDIA);
    }

    @Test
    void shouldCreateTaskWhenValuesAreProvided() {
        LocalDate dueDate = LocalDate.now().plusDays(1);
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();

        Task task = new Task(
                1L,
                "Criar API",
                "Implementar endpoints",
                "Lisa Kim",
                "Trackio",
                40,
                TaskStatus.EM_PROGRESSO,
                TaskPriority.ALTA,
                dueDate,
                createdAt,
                updatedAt
        );

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo("Criar API");
        assertThat(task.getDescription()).isEqualTo("Implementar endpoints");
        assertThat(task.getAssignee()).isEqualTo("Lisa Kim");
        assertThat(task.getProjectName()).isEqualTo("Trackio");
        assertThat(task.getProgress()).isEqualTo(40);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.EM_PROGRESSO);
        assertThat(task.getPriority()).isEqualTo(TaskPriority.ALTA);
        assertThat(task.getDueDate()).isEqualTo(dueDate);
        assertThat(task.getCreatedAt()).isEqualTo(createdAt);
        assertThat(task.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void shouldPreserveDefaultsWhenSettersReceiveNullValues() {
        Task task = new Task("Criar API", null, null, null, 50,
                TaskStatus.CONCLUIDO, TaskPriority.ALTA, null);

        task.setProgress(null);
        task.setStatus(null);
        task.setPriority(null);

        assertThat(task.getProgress()).isZero();
        assertThat(task.getStatus()).isEqualTo(TaskStatus.PENDENTE);
        assertThat(task.getPriority()).isEqualTo(TaskPriority.MEDIA);
    }
}
