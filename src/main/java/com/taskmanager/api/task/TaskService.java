package com.taskmanager.api.task;

import com.taskmanager.api.exception.TaskNotFoundException;
import com.taskmanager.api.task.dto.TaskCreateDTO;
import com.taskmanager.api.task.dto.TaskResponseDTO;
import com.taskmanager.api.task.dto.TaskUpdateDTO;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public Page<TaskResponseDTO> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable).map(TaskResponseDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public TaskResponseDTO findById(Long id) {
        return TaskResponseDTO.fromEntity(findTaskById(id));
    }

    @Transactional
    public TaskResponseDTO create(TaskCreateDTO request) {
        Task task = new Task(
                request.title(),
                request.description(),
                request.assignee(),
                request.projectName(),
                request.progress(),
                request.status(),
                request.priority(),
                request.dueDate()
        );
        Task savedTask = taskRepository.save(task);
        log.info("Tarefa criada com id {}", savedTask.getId());
        return TaskResponseDTO.fromEntity(savedTask);
    }

    @Transactional
    public TaskResponseDTO update(Long id, TaskUpdateDTO request) {
        Task task = findTaskById(id);

        if (request.title() != null) {
            task.setTitle(request.title());
        }
        if (request.description() != null) {
            task.setDescription(request.description());
        }
        if (request.assignee() != null) {
            task.setAssignee(request.assignee());
        }
        if (request.projectName() != null) {
            task.setProjectName(request.projectName());
        }
        if (request.progress() != null) {
            task.setProgress(request.progress());
        }
        if (request.status() != null) {
            task.setStatus(request.status());
        }
        if (request.priority() != null) {
            task.setPriority(request.priority());
        }
        if (request.dueDate() != null) {
            task.setDueDate(request.dueDate());
        }
        task.setUpdatedAt(LocalDateTime.now());

        log.info("Tarefa atualizada com id {}", id);
        return TaskResponseDTO.fromEntity(task);
    }

    @Transactional
    public void delete(Long id) {
        Task task = findTaskById(id);
        taskRepository.delete(task);
        log.info("Tarefa removida com id {}", id);
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDTO> filter(TaskStatus status, TaskPriority priority) {
        List<Task> tasks;
        if (status != null && priority != null) {
            tasks = taskRepository.findByStatusAndPriority(status, priority);
        } else if (status != null) {
            tasks = taskRepository.findByStatus(status);
        } else if (priority != null) {
            tasks = taskRepository.findByPriority(priority);
        } else {
            tasks = taskRepository.findAll();
        }

        return tasks.stream()
                .map(TaskResponseDTO::fromEntity)
                .toList();
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
