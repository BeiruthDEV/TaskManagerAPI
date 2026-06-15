package com.taskmanager.api.application.usecase;

import com.taskmanager.api.application.command.CreateTaskCommand;
import com.taskmanager.api.application.factory.TaskFactory;
import com.taskmanager.api.application.port.TaskRepository;
import com.taskmanager.api.domain.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateTaskUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateTaskUseCase.class);

    private final TaskRepository taskRepository;
    private final TaskFactory taskFactory;

    public CreateTaskUseCase(TaskRepository taskRepository, TaskFactory taskFactory) {
        this.taskRepository = taskRepository;
        this.taskFactory = taskFactory;
    }

    @Transactional
    public Task create(CreateTaskCommand command) {
        Task task = taskFactory.createFrom(command);
        Task savedTask = taskRepository.save(task);
        log.info("Tarefa criada com id {}", savedTask.getId());
        return savedTask;
    }
}
