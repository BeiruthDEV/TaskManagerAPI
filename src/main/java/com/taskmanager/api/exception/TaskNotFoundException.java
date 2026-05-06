package com.taskmanager.api.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("Tarefa nao encontrada com id: " + id);
    }
}
