const statusConfig = {
  PENDENTE: { label: "Pendente", className: "status-pendente" },
  EM_PROGRESSO: { label: "Em andamento", className: "status-em-progresso" },
  EM_REVISAO: { label: "Em revisao", className: "status-em-revisao" },
  CONCLUIDO: { label: "Concluida", className: "status-concluido" },
};

const priorityConfig = {
  BAIXA: { label: "Baixa", className: "priority-baixa" },
  MEDIA: { label: "Media", className: "priority-media" },
  ALTA: { label: "Alta", className: "priority-alta" },
};

const roadmapModules = {
  calendar: {
    title: "Calendar",
    description: "Organizacao futura de agendas, prazos e eventos vinculados ao trabalho do time.",
  },
  performance: {
    title: "Performance",
    description: "Acompanhamento planejado de desempenho, metas e sinais operacionais por equipe.",
  },
  employees: {
    title: "Employees",
    description: "Cadastro e consulta centralizada de colaboradores em uma etapa futura da plataforma.",
  },
  invoices: {
    title: "Invoices",
    description: "Controle planejado de faturas, cobrancas e historico financeiro operacional.",
  },
  payrolls: {
    title: "Payrolls",
    description: "Modulo futuro para apoiar ciclos de folha, pagamentos e registros relacionados.",
  },
  recruitment: {
    title: "Recruitment & Hiring",
    description: "Fluxo planejado para acompanhar vagas, candidatos e etapas de contratacao.",
  },
  integration: {
    title: "Integration",
    description: "Area futura para conectar o Trackio a ferramentas externas e automacoes.",
  },
  help: {
    title: "Help & Center",
    description: "Central planejada de suporte, documentacao e orientacao para usuarios.",
  },
};

const state = {
  tasks: [],
  query: "",
  status: "",
  selectedTask: null,
  loading: false,
  error: "",
  view: "tasks",
  roadmapModule: "",
};

const el = {
  pageHeading: document.querySelector("#page-heading"),
  pageSubtitle: document.querySelector("#page-subtitle"),
  tasksPanel: document.querySelector("#tasks-panel"),
  roadmapPanel: document.querySelector("#roadmap-panel"),
  tasksNav: document.querySelector("[data-main-view='tasks']"),
  roadmapLinks: document.querySelectorAll("[data-roadmap-module]"),
  roadmap: {
    status: document.querySelector("#roadmap-status"),
    title: document.querySelector("#roadmap-title"),
    description: document.querySelector("#roadmap-description"),
    notice: document.querySelector("#roadmap-notice"),
  },
  taskSearch: document.querySelector("#task-search"),
  statusFilter: document.querySelector("#status-filter"),
  tableWrap: document.querySelector("#table-wrap"),
  tableBody: document.querySelector("#task-table-body"),
  loadingState: document.querySelector("#loading-state"),
  errorState: document.querySelector("#error-state"),
  errorMessage: document.querySelector("#error-message"),
  retryButton: document.querySelector("#retry-button"),
  emptyState: document.querySelector("#empty-state"),
  addButtons: document.querySelectorAll("#add-task-button, [data-empty-add]"),
  summary: {
    total: document.querySelector("#summary-total"),
    pending: document.querySelector("#summary-pending"),
    progress: document.querySelector("#summary-progress"),
    completed: document.querySelector("#summary-completed"),
  },
  dialog: document.querySelector("#task-dialog"),
  form: document.querySelector("#task-form"),
  closeDialog: document.querySelector("#close-dialog"),
  cancelDialog: document.querySelector("#cancel-dialog"),
  deleteButton: document.querySelector("#delete-task-button"),
  formError: document.querySelector("#form-error"),
  dialogKicker: document.querySelector("#dialog-kicker"),
  dialogTitle: document.querySelector("#dialog-title"),
  progressOutput: document.querySelector("#progress-output"),
  toast: document.querySelector("#toast"),
  fields: {
    id: document.querySelector("#task-id"),
    title: document.querySelector("#task-title"),
    description: document.querySelector("#task-description"),
    assignee: document.querySelector("#task-assignee"),
    project: document.querySelector("#task-project"),
    status: document.querySelector("#task-status"),
    priority: document.querySelector("#task-priority"),
    dueDate: document.querySelector("#task-due-date"),
    progress: document.querySelector("#task-progress"),
  },
};

async function loadTasks() {
  state.loading = true;
  state.error = "";
  render();

  try {
    if (!window.TaskApi && typeof TaskApi === "undefined") {
      throw new Error("Cliente da API de tarefas nao foi carregado.");
    }
    state.tasks = await TaskApi.list();
  } catch (error) {
    state.tasks = [];
    state.error = error.message || "Nao foi possivel carregar as tarefas.";
  } finally {
    state.loading = false;
    render();
  }
}

function visibleTasks() {
  const term = state.query.trim().toLowerCase();
  return state.tasks.filter((task) => {
    const normalized = normalizeTask(task);

    if (state.status && normalized.status !== state.status) {
      return false;
    }

    if (!term) {
      return true;
    }

    const values = [
      normalized.title,
      task.description,
      normalized.status,
      statusLabel(normalized.status),
      normalized.priority,
      priorityLabel(normalized.priority),
    ];
    return values.some((value) => String(value || "").toLowerCase().includes(term));
  });
}

function render() {
  renderNavigation();

  const isTasksView = state.view === "tasks";
  el.tasksPanel.classList.toggle("hidden", !isTasksView);
  el.roadmapPanel.classList.toggle("hidden", isTasksView);

  if (!isTasksView) {
    renderRoadmapPanel();
    renderIcons();
    return;
  }

  el.pageHeading.textContent = "Tasks";
  el.pageSubtitle.textContent = "Gerencie as tarefas do time";

  const tasks = visibleTasks();
  const hasTasks = tasks.length > 0;
  const hasError = Boolean(state.error);

  renderSummary();

  el.loadingState.classList.toggle("hidden", !state.loading);
  el.errorState.classList.toggle("hidden", !hasError || state.loading);
  el.tableWrap.classList.toggle("hidden", state.loading || hasError || !hasTasks);
  el.emptyState.classList.toggle("hidden", state.loading || hasError || hasTasks);

  if (hasError) {
    el.errorMessage.textContent = state.error;
  }

  if (hasTasks) {
    renderTable(tasks);
  } else {
    el.tableBody.innerHTML = "";
  }

  renderIcons();
}

function renderNavigation() {
  el.tasksNav.classList.toggle("active", state.view === "tasks");

  el.roadmapLinks.forEach((button) => {
    const isActive = state.view === "roadmap" && button.dataset.roadmapModule === state.roadmapModule;
    button.classList.toggle("active", isActive);
    button.setAttribute("aria-pressed", String(isActive));
  });
}

function renderRoadmapPanel() {
  const module = roadmapModules[state.roadmapModule] || roadmapModules.calendar;
  el.pageHeading.textContent = module.title;
  el.pageSubtitle.textContent = "Modulo planejado no roadmap do produto";
  el.roadmap.status.textContent = "Em breve";
  el.roadmap.title.textContent = module.title;
  el.roadmap.description.textContent = module.description;
  el.roadmap.notice.textContent = "Este modulo ainda nao esta disponivel nesta versao.";
}

function renderSummary() {
  const allTasks = state.tasks;
  const pending = countByStatus(allTasks, "PENDENTE");
  const inProgress = countByStatus(allTasks, "EM_PROGRESSO");
  const completed = countByStatus(allTasks, "CONCLUIDO");

  el.summary.total.textContent = allTasks.length;
  el.summary.pending.textContent = pending;
  el.summary.progress.textContent = inProgress;
  el.summary.completed.textContent = completed;
}

function countByStatus(tasks, status) {
  return tasks.filter((task) => normalizeTask(task).status === status).length;
}

function renderTable(tasks) {
  el.tableBody.innerHTML = tasks.map((task) => {
    const normalized = normalizeTask(task);
    const status = statusConfig[normalized.status];
    const priority = priorityConfig[normalized.priority];
    const progress = safeProgress(normalized.progress);
    const canMutate = normalized.id !== null && normalized.id !== undefined;

    return `
      <tr>
        <td>
          <button class="task-name" type="button" ${canMutate ? `data-edit="${normalized.id}"` : "disabled"}>
            ${escapeHtml(normalized.title)}
          </button>
          ${task.description ? `<span class="task-description">${escapeHtml(task.description)}</span>` : ""}
        </td>
        <td><span class="status-pill ${status?.className || "status-pendente"}">${statusLabel(normalized.status)}</span></td>
        <td><span class="priority-pill ${priority?.className || "priority-empty"}">${priorityLabel(normalized.priority)}</span></td>
        <td class="progress-cell">
          <span class="progress-text">${progress}%</span>
          <span class="progress-bar"><span style="width:${progress}%"></span></span>
        </td>
        <td class="actions-cell">
          <button class="icon-button" type="button" ${canMutate ? `data-edit="${normalized.id}"` : "disabled"} aria-label="Editar ${escapeHtml(normalized.title)}">
            <i data-lucide="pencil"></i>
          </button>
          <button class="icon-button danger-icon" type="button" ${canMutate ? `data-delete="${normalized.id}"` : "disabled"} aria-label="Excluir ${escapeHtml(normalized.title)}">
            <i data-lucide="trash-2"></i>
          </button>
        </td>
      </tr>
    `;
  }).join("");

  bindTableActions();
}

function bindTableActions() {
  document.querySelectorAll("[data-edit]").forEach((button) => {
    button.addEventListener("click", () => {
      const task = state.tasks.find((item) => normalizeTask(item).id === Number(button.dataset.edit));
      openDialog(task);
    });
  });

  document.querySelectorAll("[data-delete]").forEach((button) => {
    button.addEventListener("click", () => deleteTask(Number(button.dataset.delete)));
  });
}

function openDialog(task = null) {
  const normalized = task ? normalizeTask(task) : null;
  state.selectedTask = task;
  el.formError.textContent = "";
  el.deleteButton.classList.toggle("hidden", !task);
  el.dialogKicker.textContent = task ? "Editar tarefa" : "Nova tarefa";
  el.dialogTitle.textContent = normalized ? normalized.title : "Detalhes da tarefa";

  el.fields.id.value = normalized?.id || "";
  el.fields.title.value = normalized?.title || "";
  el.fields.description.value = task?.description || "";
  el.fields.assignee.value = task?.assignee || "";
  el.fields.project.value = task?.projectName || "";
  el.fields.status.value = normalized?.status || "PENDENTE";
  el.fields.priority.value = normalized?.priority || "MEDIA";
  el.fields.dueDate.value = task?.dueDate || "";
  el.fields.progress.value = safeProgress(normalized?.progress);
  el.progressOutput.textContent = `${safeProgress(normalized?.progress)}%`;

  el.dialog.showModal();
  renderIcons();
}

function closeDialog() {
  el.dialog.close();
  state.selectedTask = null;
}

async function saveTask(event) {
  event.preventDefault();
  el.formError.textContent = "";

  const payload = {
    title: el.fields.title.value.trim(),
    description: emptyToNull(el.fields.description.value),
    assignee: emptyToNull(el.fields.assignee.value),
    projectName: emptyToNull(el.fields.project.value),
    progress: Number(el.fields.progress.value),
    status: el.fields.status.value,
    priority: el.fields.priority.value,
    dueDate: el.fields.dueDate.value || null,
  };

  try {
    if (el.fields.id.value) {
      await TaskApi.update(el.fields.id.value, payload);
      showToast("Tarefa atualizada");
    } else {
      await TaskApi.create(payload);
      showToast("Tarefa criada");
    }

    closeDialog();
    await loadTasks();
  } catch (error) {
    el.formError.textContent = error.message;
  }
}

async function deleteTask(id = state.selectedTask?.id) {
  if (!id) {
    return;
  }

  const task = state.tasks.find((item) => normalizeTask(item).id === id);
  const confirmed = window.confirm(`Excluir "${normalizeTask(task || {}).title || "esta tarefa"}"?`);
  if (!confirmed) {
    return;
  }

  try {
    await TaskApi.remove(id);
    closeDialog();
    showToast("Tarefa excluida");
    await loadTasks();
  } catch (error) {
    el.formError.textContent = error.message;
    showToast(error.message);
  }
}

function bindEvents() {
  el.tasksNav.addEventListener("click", (event) => {
    event.preventDefault();
    showTasksView();
  });

  el.roadmapLinks.forEach((button) => {
    button.addEventListener("click", () => showRoadmapModule(button.dataset.roadmapModule));
  });

  el.addButtons.forEach((button) => button.addEventListener("click", () => openDialog()));
  el.closeDialog.addEventListener("click", closeDialog);
  el.cancelDialog.addEventListener("click", closeDialog);
  el.form.addEventListener("submit", saveTask);
  el.deleteButton.addEventListener("click", () => deleteTask());
  el.retryButton.addEventListener("click", loadTasks);
  el.fields.progress.addEventListener("input", () => {
    el.progressOutput.textContent = `${el.fields.progress.value}%`;
  });

  el.taskSearch.addEventListener("input", () => {
    state.query = el.taskSearch.value;
    render();
  });

  el.statusFilter.addEventListener("change", async () => {
    state.status = el.statusFilter.value;
    render();
  });
}

function showTasksView() {
  state.view = "tasks";
  state.roadmapModule = "";
  window.history.replaceState(null, "", "/");
  render();
}

function showRoadmapModule(moduleKey) {
  state.view = "roadmap";
  state.roadmapModule = moduleKey;
  window.history.replaceState(null, "", `#${moduleKey}`);
  render();
}

function restoreInitialView() {
  const moduleKey = window.location.hash.replace("#", "");
  if (roadmapModules[moduleKey]) {
    state.view = "roadmap";
    state.roadmapModule = moduleKey;
  }
}

function safeProgress(progress = 0) {
  const value = Number(progress);
  if (Number.isNaN(value)) {
    return 0;
  }
  return Math.min(100, Math.max(0, value));
}

function normalizeTask(task = {}) {
  return {
    id: task.id ?? null,
    title: task.title || task.name || "Sem titulo",
    status: task.status || "PENDENTE",
    priority: task.priority || "",
    progress: task.progress ?? 0,
  };
}

function statusLabel(status) {
  return statusConfig[status]?.label || status || "Pendente";
}

function priorityLabel(priority) {
  return priorityConfig[priority]?.label || priority || "-";
}

function emptyToNull(value) {
  const trimmed = value.trim();
  return trimmed.length ? trimmed : null;
}

function escapeHtml(value) {
  return String(value ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

function showToast(message) {
  el.toast.textContent = message;
  el.toast.classList.add("visible");
  window.clearTimeout(showToast.timeout);
  showToast.timeout = window.setTimeout(() => {
    el.toast.classList.remove("visible");
  }, 2200);
}

function renderIcons() {
  if (window.lucide) {
    window.lucide.createIcons();
  }
}

function init() {
  bindEvents();
  restoreInitialView();
  renderIcons();
  loadTasks();
}

init();
