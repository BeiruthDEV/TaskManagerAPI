package com.taskmanager.api.application.factory;

import com.taskmanager.api.application.command.CreateTaskCommand;
import com.taskmanager.api.domain.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskFactory {

    public Task createFrom(CreateTaskCommand command) {
        return new Task(
                command.title(),
                command.description(),
                command.assignee(),
                command.projectName(),
                command.progress(),
                command.status(),
                command.priority(),
                command.dueDate()
        );
    }
}
