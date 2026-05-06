package com.taskmanager.api.task;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(length = 120)
    private String assignee;

    @Column(length = 160)
    private String projectName;

    @Column(nullable = false)
    private Integer progress = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, columnDefinition = "varchar(20)")
    private TaskStatus status = TaskStatus.PENDENTE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, columnDefinition = "varchar(20)")
    private TaskPriority priority = TaskPriority.MEDIA;

    private LocalDate dueDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
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
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.projectName = projectName;
        this.progress = progress == null ? 0 : progress;
        this.status = status == null ? TaskStatus.PENDENTE : status;
        this.priority = priority == null ? TaskPriority.MEDIA : priority;
        this.dueDate = dueDate;
    }

    @PrePersist
    void prePersist() {
        if (status == null) {
            status = TaskStatus.PENDENTE;
        }
        if (priority == null) {
            priority = TaskPriority.MEDIA;
        }
        if (progress == null) {
            progress = 0;
        }
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void preUpdate() {
        if (status == null) {
            status = TaskStatus.PENDENTE;
        }
        if (priority == null) {
            priority = TaskPriority.MEDIA;
        }
        if (progress == null) {
            progress = 0;
        }
        updatedAt = LocalDateTime.now();
    }
}
