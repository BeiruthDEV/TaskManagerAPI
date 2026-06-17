package com.taskmanager.api.presentation.task;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.api.application.TaskService;
import com.taskmanager.api.application.command.CreateTaskCommand;
import com.taskmanager.api.application.command.UpdateTaskCommand;
import com.taskmanager.api.application.pagination.PageQuery;
import com.taskmanager.api.application.pagination.PageResult;
import com.taskmanager.api.application.usecase.KanbanColumn;
import com.taskmanager.api.application.usecase.TaskDashboard;
import com.taskmanager.api.domain.exception.TaskNotFoundException;
import com.taskmanager.api.domain.model.Task;
import com.taskmanager.api.domain.model.TaskPriority;
import com.taskmanager.api.domain.model.TaskStatus;
import com.taskmanager.api.presentation.exception.GlobalExceptionHandler;
import com.taskmanager.api.presentation.task.dto.TaskCreateDTO;
import com.taskmanager.api.presentation.task.dto.TaskUpdateDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TaskController.class)
@Import(GlobalExceptionHandler.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    private Task buildTask(Long id, String title, TaskStatus status, TaskPriority priority) {
        Task task = new Task(
                id,
                title,
                "Descricao",
                "Equipe Trackio",
                "Projeto Trackio",
                40,
                status,
                priority,
                LocalDate.of(2026, 6, 30),
                LocalDateTime.of(2026, 6, 1, 10, 0),
                LocalDateTime.of(2026, 6, 1, 10, 0)
        );
        return task;
    }

    @Test
    void shouldCreateTaskWhenPayloadIsValid() throws Exception {
        TaskCreateDTO payload = new TaskCreateDTO(
                "Criar API",
                "Implementar endpoints REST",
                "Equipe Trackio",
                "Projeto Trackio",
                40,
                TaskStatus.PENDENTE,
                TaskPriority.ALTA,
                LocalDate.of(2026, 6, 30)
        );
        Task saved = buildTask(1L, "Criar API", TaskStatus.PENDENTE, TaskPriority.ALTA);
        given(taskService.create(any(CreateTaskCommand.class))).willReturn(saved);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/tasks/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Criar API"))
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.priority").value("ALTA"));
    }

    @Test
    void shouldReturnBadRequestWhenCreatePayloadMissesTitle() throws Exception {
        String invalidPayload = """
                {
                  "title": "",
                  "description": "sem titulo"
                }
                """;

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.validationErrors.title").exists());
    }

    @Test
    void shouldReturnBadRequestWhenProgressOutOfRange() throws Exception {
        String invalidPayload = """
                {
                  "title": "Tarefa",
                  "progress": 150
                }
                """;

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.progress").exists());
    }

    @Test
    void shouldReturnBadRequestWhenEnumValueIsInvalid() throws Exception {
        String invalidPayload = """
                {
                  "title": "Tarefa",
                  "status": "INEXISTENTE"
                }
                """;

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message", containsString("invalida")));
    }

    @Test
    void shouldListTasksWithPagination() throws Exception {
        Task task1 = buildTask(1L, "Tarefa 1", TaskStatus.PENDENTE, TaskPriority.ALTA);
        Task task2 = buildTask(2L, "Tarefa 2", TaskStatus.EM_PROGRESSO, TaskPriority.MEDIA);
        PageQuery query = PageQuery.of(0, 10);
        PageResult<Task> page = PageResult.of(List.of(task1, task2), query, 2);
        given(taskService.findAll(any(PageQuery.class))).willReturn(page);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(true));
    }

    @Test
    void shouldHonorCustomPageAndSizeParameters() throws Exception {
        PageQuery query = PageQuery.of(1, 5);
        PageResult<Task> page = PageResult.of(List.of(), query, 0);
        given(taskService.findAll(any(PageQuery.class))).willReturn(page);

        mockMvc.perform(get("/api/tasks")
                        .param("page", "1")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.size").value(5));
    }

    @Test
    void shouldReturnKanbanBoardGroupedByStatus() throws Exception {
        Task pending = buildTask(1L, "Pendente", TaskStatus.PENDENTE, TaskPriority.ALTA);
        Task inProgress = buildTask(2L, "Em progresso", TaskStatus.EM_PROGRESSO, TaskPriority.MEDIA);
        given(taskService.getKanbanBoard()).willReturn(List.of(
                new KanbanColumn(TaskStatus.PENDENTE, List.of(pending)),
                new KanbanColumn(TaskStatus.EM_PROGRESSO, List.of(inProgress)),
                new KanbanColumn(TaskStatus.EM_REVISAO, List.of()),
                new KanbanColumn(TaskStatus.CONCLUIDO, List.of())
        ));

        mockMvc.perform(get("/api/tasks/kanban"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTasks").value(2))
                .andExpect(jsonPath("$.columns.length()").value(4))
                .andExpect(jsonPath("$.columns[0].status").value("PENDENTE"))
                .andExpect(jsonPath("$.columns[0].title").value("Pendente"))
                .andExpect(jsonPath("$.columns[0].total").value(1))
                .andExpect(jsonPath("$.columns[0].tasks[0].id").value(1))
                .andExpect(jsonPath("$.columns[1].status").value("EM_PROGRESSO"))
                .andExpect(jsonPath("$.columns[1].tasks[0].id").value(2))
                .andExpect(jsonPath("$.columns[2].status").value("EM_REVISAO"))
                .andExpect(jsonPath("$.columns[2].total").value(0))
                .andExpect(jsonPath("$.columns[3].status").value("CONCLUIDO"));
    }

    @Test
    void shouldReturnTaskDashboardIndicators() throws Exception {
        given(taskService.getDashboard()).willReturn(new TaskDashboard(
                4,
                Map.of(
                        TaskStatus.PENDENTE, 1L,
                        TaskStatus.EM_PROGRESSO, 1L,
                        TaskStatus.EM_REVISAO, 0L,
                        TaskStatus.CONCLUIDO, 2L
                ),
                Map.of(
                        TaskPriority.BAIXA, 1L,
                        TaskPriority.MEDIA, 2L,
                        TaskPriority.ALTA, 1L
                ),
                1,
                2,
                50.0
        ));

        mockMvc.perform(get("/api/tasks/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalTasks").value(4))
                .andExpect(jsonPath("$.tasksByStatus.PENDENTE").value(1))
                .andExpect(jsonPath("$.tasksByStatus.EM_PROGRESSO").value(1))
                .andExpect(jsonPath("$.tasksByStatus.EM_REVISAO").value(0))
                .andExpect(jsonPath("$.tasksByStatus.CONCLUIDO").value(2))
                .andExpect(jsonPath("$.tasksByPriority.BAIXA").value(1))
                .andExpect(jsonPath("$.tasksByPriority.MEDIA").value(2))
                .andExpect(jsonPath("$.tasksByPriority.ALTA").value(1))
                .andExpect(jsonPath("$.overdueTasks").value(1))
                .andExpect(jsonPath("$.completedTasks").value(2))
                .andExpect(jsonPath("$.completionRate").value(50.0));
    }

    @Test
    void shouldFindTaskByIdWhenTaskExists() throws Exception {
        Task task = buildTask(7L, "Tarefa 7", TaskStatus.EM_REVISAO, TaskPriority.BAIXA);
        given(taskService.findById(7L)).willReturn(task);

        mockMvc.perform(get("/api/tasks/{id}", 7L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.title").value("Tarefa 7"))
                .andExpect(jsonPath("$.status").value("EM_REVISAO"))
                .andExpect(jsonPath("$.priority").value("BAIXA"));
    }

    @Test
    void shouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
        willThrow(new TaskNotFoundException(99L)).given(taskService).findById(99L);

        mockMvc.perform(get("/api/tasks/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message", containsString("99")))
                .andExpect(jsonPath("$.path").value("/api/tasks/99"));
    }

    @Test
    void shouldUpdateTaskWhenTaskExists() throws Exception {
        TaskUpdateDTO payload = new TaskUpdateDTO(
                "Tarefa atualizada",
                null,
                null,
                null,
                75,
                TaskStatus.CONCLUIDO,
                TaskPriority.MEDIA,
                null
        );
        Task updated = buildTask(3L, "Tarefa atualizada", TaskStatus.CONCLUIDO, TaskPriority.MEDIA);
        updated.setProgress(75);
        given(taskService.update(eq(3L), any(UpdateTaskCommand.class))).willReturn(updated);

        mockMvc.perform(put("/api/tasks/{id}", 3L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.title").value("Tarefa atualizada"))
                .andExpect(jsonPath("$.progress").value(75))
                .andExpect(jsonPath("$.status").value("CONCLUIDO"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingMissingTask() throws Exception {
        TaskUpdateDTO payload = new TaskUpdateDTO(
                null, null, null, null, null, TaskStatus.CONCLUIDO, null, null
        );
        given(taskService.update(eq(404L), any(UpdateTaskCommand.class)))
                .willThrow(new TaskNotFoundException(404L));

        mockMvc.perform(put("/api/tasks/{id}", 404L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message", containsString("404")));
    }

    @Test
    void shouldReturnBadRequestWhenUpdatePayloadHasBlankTitle() throws Exception {
        String invalidPayload = """
                {
                  "title": "   "
                }
                """;

        mockMvc.perform(put("/api/tasks/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.title").exists());
    }

    @Test
    void shouldDeleteTaskWhenTaskExists() throws Exception {
        doNothing().when(taskService).delete(5L);

        mockMvc.perform(delete("/api/tasks/{id}", 5L))
                .andExpect(status().isNoContent());

        verify(taskService).delete(5L);
    }

    @Test
    void shouldReturnNotFoundWhenDeletingMissingTask() throws Exception {
        doThrow(new TaskNotFoundException(404L)).when(taskService).delete(404L);

        mockMvc.perform(delete("/api/tasks/{id}", 404L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("404")));
    }

    @Test
    void shouldFilterTasksByStatus() throws Exception {
        Task task = buildTask(1L, "Pendente", TaskStatus.PENDENTE, TaskPriority.ALTA);
        given(taskService.filter(TaskStatus.PENDENTE, null)).willReturn(List.of(task));

        mockMvc.perform(get("/api/tasks/filter").param("status", "PENDENTE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDENTE"));
    }

    @Test
    void shouldFilterTasksByPriority() throws Exception {
        Task task = buildTask(1L, "Alta", TaskStatus.EM_PROGRESSO, TaskPriority.ALTA);
        given(taskService.filter(null, TaskPriority.ALTA)).willReturn(List.of(task));

        mockMvc.perform(get("/api/tasks/filter").param("priority", "ALTA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].priority").value("ALTA"));
    }

    @Test
    void shouldFilterTasksByStatusAndPriority() throws Exception {
        Task task = buildTask(1L, "Match", TaskStatus.PENDENTE, TaskPriority.ALTA);
        given(taskService.filter(TaskStatus.PENDENTE, TaskPriority.ALTA)).willReturn(List.of(task));

        mockMvc.perform(get("/api/tasks/filter")
                        .param("status", "PENDENTE")
                        .param("priority", "ALTA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("PENDENTE"))
                .andExpect(jsonPath("$[0].priority").value("ALTA"));
    }

    @Test
    void shouldReturnBadRequestWhenFilterStatusIsInvalid() throws Exception {
        mockMvc.perform(get("/api/tasks/filter").param("status", "INEXISTENTE"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
