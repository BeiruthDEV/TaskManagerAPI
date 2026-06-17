package com.taskmanager.api.infrastructure.persistence;

import com.taskmanager.api.application.pagination.PageQuery;
import com.taskmanager.api.application.pagination.PageResult;
import com.taskmanager.api.application.port.TaskRepository;
import com.taskmanager.api.domain.model.Task;
import com.taskmanager.api.domain.model.TaskPriority;
import com.taskmanager.api.domain.model.TaskStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

@Repository
public class TaskRepositoryAdapter implements TaskRepository {

    private final SpringDataTaskRepository springDataTaskRepository;

    public TaskRepositoryAdapter(SpringDataTaskRepository springDataTaskRepository) {
        this.springDataTaskRepository = springDataTaskRepository;
    }

    @Override
    public PageResult<Task> findAll(PageQuery query) {
        Page<Task> page = springDataTaskRepository
                .findAll(PageRequest.of(query.page(), query.size()))
                .map(TaskJpaMapper::toDomain);
        return PageResult.of(page.getContent(), query, page.getTotalElements());
    }

    @Override
    public List<Task> findAll() {
        return springDataTaskRepository.findAll().stream()
                .map(TaskJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Task> findById(Long id) {
        return springDataTaskRepository.findById(id).map(TaskJpaMapper::toDomain);
    }

    @Override
    public Task save(Task task) {
        TaskJpaEntity savedEntity = springDataTaskRepository.save(TaskJpaMapper.toEntity(task));
        return TaskJpaMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Task task) {
        springDataTaskRepository.deleteById(task.getId());
    }

    @Override
    public long count() {
        return springDataTaskRepository.count();
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        return springDataTaskRepository.findByStatus(status).stream()
                .map(TaskJpaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Task> findByPriority(TaskPriority priority) {
        return springDataTaskRepository.findByPriority(priority).stream()
                .map(TaskJpaMapper::toDomain)
                .toList();
    }

    @Override
    public List<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority) {
        return springDataTaskRepository.findByStatusAndPriority(status, priority).stream()
                .map(TaskJpaMapper::toDomain)
                .toList();
    }
}
