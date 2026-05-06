package com.taskmanager.api.task;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void shouldFilterTasksByStatusAndPriority() {
        Task highPending = new Task(
                "Tarefa urgente",
                "Resolver hoje",
                "Michael Ardi",
                "DODO System Upgrade",
                0,
                TaskStatus.PENDENTE,
                TaskPriority.ALTA,
                LocalDate.now()
        );
        Task lowCompleted = new Task(
                "Tarefa simples",
                "Ja finalizada",
                "Lisa Kim",
                "Email Marketing Launch",
                100,
                TaskStatus.CONCLUIDO,
                TaskPriority.BAIXA,
                null
        );

        taskRepository.save(highPending);
        taskRepository.save(lowCompleted);

        List<Task> result = taskRepository.findByStatusAndPriority(TaskStatus.PENDENTE, TaskPriority.ALTA);

        assertThat(result)
                .hasSize(1)
                .first()
                .extracting(Task::getTitle)
                .isEqualTo("Tarefa urgente");
    }

    @Test
    void shouldFillAuditFieldsWhenSaving() {
        Task task = new Task("Auditoria", null, null, null, null, null, null, null);

        Task savedTask = taskRepository.saveAndFlush(task);

        assertThat(savedTask.getCreatedAt()).isNotNull();
        assertThat(savedTask.getUpdatedAt()).isNotNull();
        assertThat(savedTask.getStatus()).isEqualTo(TaskStatus.PENDENTE);
        assertThat(savedTask.getPriority()).isEqualTo(TaskPriority.MEDIA);
    }
}
