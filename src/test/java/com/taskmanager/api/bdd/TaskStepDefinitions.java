package com.taskmanager.api.bdd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import com.taskmanager.api.application.TaskService;
import com.taskmanager.api.application.command.CreateTaskCommand;
import com.taskmanager.api.application.command.UpdateTaskCommand;
import com.taskmanager.api.application.factory.TaskFactory;
import com.taskmanager.api.application.port.TaskRepository;
import com.taskmanager.api.application.usecase.CreateTaskUseCase;
import com.taskmanager.api.application.usecase.DeleteTaskUseCase;
import com.taskmanager.api.application.usecase.FindTaskUseCase;
import com.taskmanager.api.application.usecase.KanbanTasksUseCase;
import com.taskmanager.api.application.usecase.ListTasksUseCase;
import com.taskmanager.api.application.usecase.TaskDashboardUseCase;
import com.taskmanager.api.application.usecase.UpdateTaskUseCase;
import com.taskmanager.api.domain.exception.TaskNotFoundException;
import com.taskmanager.api.domain.model.Task;
import com.taskmanager.api.domain.model.TaskPriority;
import com.taskmanager.api.domain.model.TaskStatus;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class TaskStepDefinitions {

    private InMemoryTaskRepository taskRepository;
    private TaskService taskService;
    private Long existingTaskId;
    private Task createdTask;
    private Task foundTask;
    private Task updatedTask;
    private List<Task> listedTasks;
    private Throwable capturedException;

    @Before
    public void setUp() {
        taskRepository = new InMemoryTaskRepository();
        taskService = new TaskService(
                new CreateTaskUseCase(taskRepository, new TaskFactory()),
                new UpdateTaskUseCase(taskRepository),
                new DeleteTaskUseCase(taskRepository),
                new FindTaskUseCase(taskRepository),
                new ListTasksUseCase(taskRepository),
                new KanbanTasksUseCase(taskRepository),
                new TaskDashboardUseCase(taskRepository)
        );
        existingTaskId = null;
        createdTask = null;
        foundTask = null;
        updatedTask = null;
        listedTasks = List.of();
        capturedException = null;
    }

    @Given("nao existem tarefas cadastradas")
    public void naoExistemTarefasCadastradas() {
        taskRepository.clear();
    }

    @Given("existe uma tarefa cadastrada com titulo {string}")
    public void existeUmaTarefaCadastradaComTitulo(String title) {
        Task task = taskService.create(new CreateTaskCommand(
                title,
                "Descricao da tarefa",
                "Equipe Trackio",
                "Projeto academico",
                null,
                null,
                null,
                null
        ));
        existingTaskId = task.getId();
    }

    @When("eu crio uma tarefa com titulo {string}")
    public void euCrioUmaTarefaComTitulo(String title) {
        createdTask = taskService.create(new CreateTaskCommand(
                title,
                "Descricao da tarefa",
                "Equipe Trackio",
                "Projeto academico",
                null,
                null,
                null,
                null
        ));
    }

    @When("eu listo as tarefas")
    public void euListoAsTarefas() {
        listedTasks = taskService.filter(null, null);
    }

    @When("eu busco a tarefa existente")
    public void euBuscoATarefaExistente() {
        foundTask = taskService.findById(existingTaskId);
    }

    @When("eu tento buscar a tarefa com id {long}")
    public void euTentoBuscarATarefaComId(Long id) {
        capturedException = catchThrowable(() -> taskService.findById(id));
    }

    @When("eu atualizo a tarefa existente para status {string}")
    public void euAtualizoATarefaExistenteParaStatus(String status) {
        updatedTask = taskService.update(existingTaskId, new UpdateTaskCommand(
                null,
                null,
                null,
                null,
                null,
                TaskStatus.valueOf(status),
                null,
                null
        ));
    }

    @When("eu excluo a tarefa existente")
    public void euExcluoATarefaExistente() {
        taskService.delete(existingTaskId);
        listedTasks = taskService.filter(null, null);
    }

    @Then("a tarefa deve ser criada com titulo {string}")
    public void aTarefaDeveSerCriadaComTitulo(String title) {
        assertThat(createdTask).isNotNull();
        assertThat(createdTask.getTitle()).isEqualTo(title);
    }

    @Then("o status da tarefa criada deve ser {string}")
    public void oStatusDaTarefaCriadaDeveSer(String status) {
        assertThat(createdTask.getStatus()).isEqualTo(TaskStatus.valueOf(status));
    }

    @Then("a lista deve conter {int} item")
    public void aListaDeveConterItem(int expectedSize) {
        assertThat(listedTasks).hasSize(expectedSize);
    }

    @Then("a lista deve conter a tarefa {string}")
    public void aListaDeveConterATarefa(String title) {
        assertThat(listedTasks)
                .extracting(Task::getTitle)
                .contains(title);
    }

    @Then("a tarefa encontrada deve ter titulo {string}")
    public void aTarefaEncontradaDeveTerTitulo(String title) {
        assertThat(foundTask).isNotNull();
        assertThat(foundTask.getTitle()).isEqualTo(title);
    }

    @Then("devo receber erro de tarefa nao encontrada contendo {string}")
    public void devoReceberErroDeTarefaNaoEncontradaContendo(String messagePart) {
        assertThat(capturedException)
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(messagePart);
    }

    @Then("a tarefa atualizada deve ter status {string}")
    public void aTarefaAtualizadaDeveTerStatus(String status) {
        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getStatus()).isEqualTo(TaskStatus.valueOf(status));
    }

    private static class InMemoryTaskRepository implements TaskRepository {

        private final Map<Long, Task> tasks = new LinkedHashMap<>();
        private long sequence = 1L;

        @Override
        public Page<Task> findAll(Pageable pageable) {
            List<Task> allTasks = findAll();
            int start = (int) Math.min(pageable.getOffset(), allTasks.size());
            int end = Math.min(start + pageable.getPageSize(), allTasks.size());
            return new PageImpl<>(allTasks.subList(start, end), pageable, allTasks.size());
        }

        @Override
        public List<Task> findAll() {
            return tasks.values().stream()
                    .sorted(Comparator.comparing(Task::getId))
                    .toList();
        }

        @Override
        public Optional<Task> findById(Long id) {
            return Optional.ofNullable(tasks.get(id));
        }

        @Override
        public Task save(Task task) {
            LocalDateTime now = LocalDateTime.now();
            if (task.getId() == null) {
                task.setId(sequence++);
                task.setCreatedAt(now);
            }
            task.setUpdatedAt(now);
            tasks.put(task.getId(), task);
            return task;
        }

        @Override
        public void delete(Task task) {
            tasks.remove(task.getId());
        }

        @Override
        public long count() {
            return tasks.size();
        }

        @Override
        public List<Task> findByStatus(TaskStatus status) {
            return findAll().stream()
                    .filter(task -> task.getStatus() == status)
                    .toList();
        }

        @Override
        public List<Task> findByPriority(TaskPriority priority) {
            return findAll().stream()
                    .filter(task -> task.getPriority() == priority)
                    .toList();
        }

        @Override
        public List<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority) {
            return findAll().stream()
                    .filter(task -> task.getStatus() == status)
                    .filter(task -> task.getPriority() == priority)
                    .toList();
        }

        void clear() {
            tasks.clear();
            sequence = 1L;
        }
    }
}
