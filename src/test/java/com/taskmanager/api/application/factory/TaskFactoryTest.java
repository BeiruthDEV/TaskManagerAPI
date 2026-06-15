package com.taskmanager.api.application.factory;

import static org.assertj.core.api.Assertions.assertThat;

import com.taskmanager.api.application.command.CreateTaskCommand;
import com.taskmanager.api.domain.model.Task;
import com.taskmanager.api.domain.model.TaskPriority;
import com.taskmanager.api.domain.model.TaskStatus;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class TaskFactoryTest {

    private final TaskFactory taskFactory = new TaskFactory();

    @Test
    void shouldCreateTaskFromCreateCommand() {
        LocalDate dueDate = LocalDate.now().plusDays(1);
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

        Task task = taskFactory.createFrom(command);

        assertThat(task.getTitle()).isEqualTo("Criar API");
        assertThat(task.getDescription()).isEqualTo("Implementar endpoints");
        assertThat(task.getAssignee()).isEqualTo("Lisa Kim");
        assertThat(task.getProjectName()).isEqualTo("Trackio");
        assertThat(task.getProgress()).isEqualTo(40);
        assertThat(task.getStatus()).isEqualTo(TaskStatus.EM_PROGRESSO);
        assertThat(task.getPriority()).isEqualTo(TaskPriority.ALTA);
        assertThat(task.getDueDate()).isEqualTo(dueDate);
    }

    @Test
    void shouldPreserveDomainDefaultsWhenCommandOmitsOptionalValues() {
        CreateTaskCommand command = new CreateTaskCommand(
                "Criar API",
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Task task = taskFactory.createFrom(command);

        assertThat(task.getProgress()).isZero();
        assertThat(task.getStatus()).isEqualTo(TaskStatus.PENDENTE);
        assertThat(task.getPriority()).isEqualTo(TaskPriority.MEDIA);
    }
}
