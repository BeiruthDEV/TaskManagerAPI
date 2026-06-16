package com.taskmanager.api.application.usecase;

import com.taskmanager.api.application.port.TaskRepository;
import com.taskmanager.api.domain.model.Task;
import com.taskmanager.api.domain.model.TaskPriority;
import com.taskmanager.api.domain.model.TaskStatus;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskDashboardUseCase {

    private final TaskRepository taskRepository;

    public TaskDashboardUseCase(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public TaskDashboard getDashboard() {
        List<Task> tasks = taskRepository.findAll();
        long totalTasks = tasks.size();
        Map<TaskStatus, Long> tasksByStatus = emptyStatusCounters();
        Map<TaskPriority, Long> tasksByPriority = emptyPriorityCounters();

        tasks.forEach(task -> {
            tasksByStatus.compute(task.getStatus(), (status, total) -> total + 1);
            tasksByPriority.compute(task.getPriority(), (priority, total) -> total + 1);
        });

        long completedTasks = tasksByStatus.get(TaskStatus.CONCLUIDO);
        long overdueTasks = countOverdueTasks(tasks);
        double completionRate = calculateCompletionRate(completedTasks, totalTasks);

        return new TaskDashboard(
                totalTasks,
                Map.copyOf(tasksByStatus),
                Map.copyOf(tasksByPriority),
                overdueTasks,
                completedTasks,
                completionRate
        );
    }

    private Map<TaskStatus, Long> emptyStatusCounters() {
        Map<TaskStatus, Long> counters = new EnumMap<>(TaskStatus.class);
        Arrays.stream(TaskStatus.values()).forEach(status -> counters.put(status, 0L));
        return counters;
    }

    private Map<TaskPriority, Long> emptyPriorityCounters() {
        Map<TaskPriority, Long> counters = new EnumMap<>(TaskPriority.class);
        Arrays.stream(TaskPriority.values()).forEach(priority -> counters.put(priority, 0L));
        return counters;
    }

    private long countOverdueTasks(List<Task> tasks) {
        LocalDate today = LocalDate.now();
        return tasks.stream()
                .filter(task -> task.getDueDate() != null)
                .filter(task -> task.getDueDate().isBefore(today))
                .filter(task -> task.getStatus() != TaskStatus.CONCLUIDO)
                .count();
    }

    private double calculateCompletionRate(long completedTasks, long totalTasks) {
        if (totalTasks == 0) {
            return 0.0;
        }
        double rate = (completedTasks * 100.0) / totalTasks;
        return Math.round(rate * 100.0) / 100.0;
    }
}
