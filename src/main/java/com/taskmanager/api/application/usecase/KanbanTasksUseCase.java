package com.taskmanager.api.application.usecase;

import com.taskmanager.api.application.port.TaskRepository;
import com.taskmanager.api.domain.model.Task;
import com.taskmanager.api.domain.model.TaskStatus;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KanbanTasksUseCase {

    private final TaskRepository taskRepository;

    public KanbanTasksUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public List<KanbanColumn> getBoard() {
        Map<TaskStatus, List<Task>> groupedTasks = new EnumMap<>(TaskStatus.class);
        Arrays.stream(TaskStatus.values())
                .forEach(status -> groupedTasks.put(status, List.of()));

        taskRepository.findAll().stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        Task::getStatus,
                        () -> new EnumMap<>(TaskStatus.class),
                        java.util.stream.Collectors.toList()
                ))
                .forEach(groupedTasks::put);

        return Arrays.stream(TaskStatus.values())
                .map(status -> new KanbanColumn(status, List.copyOf(groupedTasks.get(status))))
                .toList();
    }
}
