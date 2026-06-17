package com.taskmanager.api.presentation.task;

import com.taskmanager.api.application.TaskService;
import com.taskmanager.api.application.pagination.PageQuery;
import com.taskmanager.api.application.pagination.PageResult;
import com.taskmanager.api.domain.model.Task;
import com.taskmanager.api.domain.model.TaskPriority;
import com.taskmanager.api.domain.model.TaskStatus;
import com.taskmanager.api.presentation.task.dto.TaskCreateDTO;
import com.taskmanager.api.presentation.task.dto.TaskDashboardResponseDTO;
import com.taskmanager.api.presentation.task.dto.TaskKanbanResponseDTO;
import com.taskmanager.api.presentation.task.dto.TaskPageResponseDTO;
import com.taskmanager.api.presentation.task.dto.TaskResponseDTO;
import com.taskmanager.api.presentation.task.dto.TaskUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
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
@Tag(name = "Tarefas", description = "Operações de gerenciamento de tarefas")
public class TaskController {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Listar tarefas", description = "Lista todas as tarefas com suporte a paginação")
    @ApiResponse(responseCode = "200", description = "Tarefas listadas com sucesso")
    public TaskPageResponseDTO findAll(
            @RequestParam(name = "page", required = false, defaultValue = "" + DEFAULT_PAGE) int page,
            @RequestParam(name = "size", required = false, defaultValue = "" + DEFAULT_SIZE) int size
    ) {
        PageQuery query = PageQuery.of(page, size);
        PageResult<Task> result = taskService.findAll(query);
        return TaskPageResponseDTO.fromDomain(result.map(TaskResponseDTO::fromDomain));
    }

    @GetMapping("/kanban")
    @Operation(summary = "Listar Kanban", description = "Retorna tarefas agrupadas por status para visualização Kanban")
    @ApiResponse(responseCode = "200", description = "Quadro Kanban retornado com sucesso")
    public TaskKanbanResponseDTO getKanbanBoard() {
        return TaskKanbanResponseDTO.fromApplication(taskService.getKanbanBoard());
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Exibir painel", description = "Retorna indicadores agregados do módulo de tarefas")
    @ApiResponse(responseCode = "200", description = "Painel retornado com sucesso")
    public TaskDashboardResponseDTO getDashboard() {
        return TaskDashboardResponseDTO.fromApplication(taskService.getDashboard());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar tarefa por id")
    @ApiResponse(responseCode = "200", description = "Tarefa encontrada")
    @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    public TaskResponseDTO findById(@PathVariable Long id) {
        return TaskResponseDTO.fromDomain(taskService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Criar tarefa")
    @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    public ResponseEntity<TaskResponseDTO> create(@Valid @RequestBody TaskCreateDTO request) {
        Task task = taskService.create(request.toCommand());
        return ResponseEntity
                .created(URI.create("/api/tasks/" + task.getId()))
                .body(TaskResponseDTO.fromDomain(task));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tarefa", description = "Atualiza parcial ou completamente uma tarefa")
    @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Requisição inválida")
    @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    public TaskResponseDTO update(@PathVariable Long id, @Valid @RequestBody TaskUpdateDTO request) {
        return TaskResponseDTO.fromDomain(taskService.update(id, request.toCommand()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir tarefa")
    @ApiResponse(responseCode = "204", description = "Tarefa removida com sucesso")
    @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    @Operation(summary = "Filtrar tarefas", description = "Filtra tarefas por status e prioridade")
    @ApiResponse(responseCode = "200", description = "Filtro executado com sucesso")
    @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    public List<TaskResponseDTO> filter(
            @Parameter(example = "PENDENTE")
            @RequestParam(required = false)
            TaskStatus status,
            @Parameter(example = "ALTA")
            @RequestParam(required = false)
            TaskPriority priority
    ) {
        return taskService.filter(status, priority).stream()
                .map(TaskResponseDTO::fromDomain)
                .toList();
    }
}
