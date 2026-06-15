package com.taskmanager.api.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Task {

    private Long id;
    private String title;
    private String description;
    private String assignee;
    private String projectName;
    private Integer progress = 0;
    private TaskStatus status = TaskStatus.PENDENTE;
    private TaskPriority priority = TaskPriority.MEDIA;
    private LocalDate dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Task(
            String title,
            String description,
            String assignee,
            String projectName,
            Integer progress,
            TaskStatus status,
            TaskPriority priority,
            LocalDate dueDate
    ) {
        this(null, title, description, assignee, projectName, progress, status, priority, dueDate, null, null);
    }

    public Task(
            Long id,
            String title,
            String description,
            String assignee,
            String projectName,
            Integer progress,
            TaskStatus status,
            TaskPriority priority,
            LocalDate dueDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.projectName = projectName;
        this.progress = progress == null ? 0 : progress;
        this.status = status == null ? TaskStatus.PENDENTE : status;
        this.priority = priority == null ? TaskPriority.MEDIA : priority;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void markUpdated(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress == null ? 0 : progress;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status == null ? TaskStatus.PENDENTE : status;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority == null ? TaskPriority.MEDIA : priority;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
