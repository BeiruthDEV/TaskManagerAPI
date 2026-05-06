package com.taskmanager.api.task;

import com.taskmanager.api.task.dto.TaskCreateDTO;
import com.taskmanager.api.task.dto.TaskResponseDTO;
import com.taskmanager.api.task.dto.TaskUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "Operacoes de gerenciamento de tarefas")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Listar tarefas", description = "Lista todas as tarefas com suporte a paginacao")
    @ApiResponse(responseCode = "200", description = "Tarefas listadas com sucesso")
    public Page<TaskResponseDTO> findAll(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        return taskService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tarefa por id")
    @ApiResponse(responseCode = "200", description = "Tarefa encontrada")
    @ApiResponse(responseCode = "404", description = "Tarefa nao encontrada")
    public TaskResponseDTO findById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Criar tarefa")
    @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisicao invalida")
    public ResponseEntity<TaskResponseDTO> create(@Valid @RequestBody TaskCreateDTO request) {
        TaskResponseDTO task = taskService.create(request);
        return ResponseEntity
                .created(URI.create("/api/tasks/" + task.id()))
                .body(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tarefa", description = "Atualiza parcial ou completamente uma tarefa")
    @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisicao invalida")
    @ApiResponse(responseCode = "404", description = "Tarefa nao encontrada")
    public TaskResponseDTO update(@PathVariable Long id, @Valid @RequestBody TaskUpdateDTO request) {
        return taskService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar tarefa")
    @ApiResponse(responseCode = "204", description = "Tarefa removida com sucesso")
    @ApiResponse(responseCode = "404", description = "Tarefa nao encontrada")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    @Operation(summary = "Filtrar tarefas", description = "Filtra tarefas por status e prioridade")
    @ApiResponse(responseCode = "200", description = "Filtro executado com sucesso")
    @ApiResponse(responseCode = "400", description = "Parametros invalidos")
    public List<TaskResponseDTO> filter(
            @Parameter(example = "PENDENTE")
            @RequestParam(required = false)
            TaskStatus status,
            @Parameter(example = "ALTA")
            @RequestParam(required = false)
            TaskPriority priority
    ) {
        return taskService.filter(status, priority);
    }
}
