package com.taskmanager.api.infrastructure.persistence;

import com.taskmanager.api.domain.model.TaskPriority;
import com.taskmanager.api.domain.model.TaskStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataTaskRepository extends JpaRepository<TaskJpaEntity, Long> {

    List<TaskJpaEntity> findByStatus(TaskStatus status);

    List<TaskJpaEntity> findByPriority(TaskPriority priority);

    List<TaskJpaEntity> findByStatusAndPriority(TaskStatus status, TaskPriority priority);
}
