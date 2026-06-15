package com.taskmanager.api.application.usecase;

import com.taskmanager.api.application.command.UpdateTaskCommand;
import com.taskmanager.api.application.port.TaskRepository;
import com.taskmanager.api.domain.exception.TaskNotFoundException;
import com.taskmanager.api.domain.model.Task;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateTaskUseCase {

    private static final Logger log = LoggerFactory.getLogger(UpdateTaskUseCase.class);

    private final TaskRepository taskRepository;

    public UpdateTaskUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public Task update(Long id, UpdateTaskCommand command) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        if (command.title() != null) {
            task.setTitle(command.title());
        }
        if (command.description() != null) {
            task.setDescription(command.description());
        }
        if (command.assignee() != null) {
            task.setAssignee(command.assignee());
        }
        if (command.projectName() != null) {
            task.setProjectName(command.projectName());
        }
        if (command.progress() != null) {
            task.setProgress(command.progress());
        }
        if (command.status() != null) {
            task.setStatus(command.status());
        }
        if (command.priority() != null) {
            task.setPriority(command.priority());
        }
        if (command.dueDate() != null) {
            task.setDueDate(command.dueDate());
        }
        task.markUpdated(LocalDateTime.now());

        Task savedTask = taskRepository.save(task);
        log.info("Tarefa atualizada com id {}", id);
        return savedTask;
    }
}
