const statusConfig = {
  PENDENTE: { label: "Pendente", className: "status-pendente" },
  EM_PROGRESSO: { label: "Em andamento", className: "status-em-progresso" },
  EM_REVISAO: { label: "Em revisão", className: "status-em-revisao" },
  CONCLUIDO: { label: "Concluída", className: "status-concluido" },
};

const priorityConfig = {
  BAIXA: { label: "Baixa", className: "priority-baixa" },
  MEDIA: { label: "Média", className: "priority-media" },
  ALTA: { label: "Alta", className: "priority-alta" },
};

const statusOrder = ["PENDENTE", "EM_PROGRESSO", "EM_REVISAO", "CONCLUIDO"];
const priorityOrder = ["ALTA", "MEDIA", "BAIXA"];

const roadmapModules = {
  calendar: {
    title: "Calendário",
    description: "Organização futura de agendas, prazos e eventos vinculados ao trabalho do time.",
  },
  performance: {
    title: "Desempenho",
    description: "Acompanhamento planejado de desempenho, metas e sinais operacionais por equipe.",
  },
  employees: {
    title: "Colaboradores",
    description: "Cadastro e consulta centralizada de colaboradores em uma etapa futura da plataforma.",
  },
  invoices: {
    title: "Faturas",
    description: "Controle planejado de faturas, cobranças e histórico financeiro operacional.",
  },
  payrolls: {
    title: "Folhas",
    description: "Módulo futuro para apoiar ciclos de folha, pagamentos e registros relacionados.",
  },
  recruitment: {
    title: "Recrutamento",
    description: "Fluxo planejado para acompanhar vagas, candidatos e etapas de contratação.",
  },
  integration: {
    title: "Integrações",
    description: "Área futura para conectar o Trackio a ferramentas externas e automações.",
  },
  help: {
    title: "Ajuda",
    description: "Central planejada de suporte, documentação e orientação para usuários.",
  },
};

const state = {
  tasks: [],
  query: "",
  status: "",
  priority: "",
  assignee: "",
  selectedTask: null,
  loading: false,
  error: "",
  dashboard: null,
  dashboardLoading: false,
  dashboardError: "",
  kanban: null,
  kanbanLoading: false,
  kanbanError: "",
  view: "tasks",
  roadmapModule: "",
};

const el = {
  pageHeading: document.querySelector("#page-heading"),
  pageSubtitle: document.querySelector("#page-subtitle"),
  tasksPanel: document.querySelector("#tasks-panel"),
  dashboardPanel: document.querySelector("#dashboard-panel"),
  kanbanPanel: document.querySelector("#kanban-panel"),
  roadmapPanel: document.querySelector("#roadmap-panel"),
  mainLinks: document.querySelectorAll("[data-main-view]"),
  roadmapLinks: document.querySelectorAll("[data-roadmap-module]"),
  roadmap: {
    status: document.querySelector("#roadmap-status"),
    title: document.querySelector("#roadmap-title"),
    description: document.querySelector("#roadmap-description"),
    notice: document.querySelector("#roadmap-notice"),
  },
  taskSearch: document.querySelector("#task-search"),
  statusFilter: document.querySelector("#status-filter"),
  priorityFilter: document.querySelector("#priority-filter"),
  assigneeFilter: document.querySelector("#assignee-filter"),
  clearTaskFilters: document.querySelector("#clear-task-filters"),
  tableWrap: document.querySelector("#table-wrap"),
  tableBody: document.querySelector("#task-table-body"),
  loadingState: document.querySelector("#loading-state"),
  errorState: document.querySelector("#error-state"),
  errorMessage: document.querySelector("#error-message"),
  retryButton: document.querySelector("#retry-button"),
  dashboard: {
    loading: document.querySelector("#dashboard-loading-state"),
    error: document.querySelector("#dashboard-error-state"),
    errorMessage: document.querySelector("#dashboard-error-message"),
    retryButton: document.querySelector("#dashboard-retry-button"),
    content: document.querySelector("#dashboard-content"),
    total: document.querySelector("#dashboard-total"),
    completed: document.querySelector("#dashboard-completed"),
    overdue: document.querySelector("#dashboard-overdue"),
    rate: document.querySelector("#dashboard-rate"),
    statusTotal: document.querySelector("#status-distribution-total"),
    priorityTotal: document.querySelector("#priority-distribution-total"),
    statusList: document.querySelector("#dashboard-status-list"),
    priorityList: document.querySelector("#dashboard-priority-list"),
    focusTotal: document.querySelector("#dashboard-focus-total"),
    focusList: document.querySelector("#dashboard-focus-list"),
    rhythmTotal: document.querySelector("#dashboard-rhythm-total"),
    rhythmList: document.querySelector("#dashboard-rhythm-list"),
  },
  kanban: {
    loading: document.querySelector("#kanban-loading-state"),
    error: document.querySelector("#kanban-error-state"),
    errorMessage: document.querySelector("#kanban-error-message"),
    retryButton: document.querySelector("#kanban-retry-button"),
    content: document.querySelector("#kanban-content"),
    board: document.querySelector("#kanban-board"),
  },
  emptyState: document.querySelector("#empty-state"),
  addButtons: document.querySelectorAll("#add-task-button, [data-empty-add]"),
  summary: {
    total: document.querySelector("#summary-total"),
    risk: document.querySelector("#summary-risk"),
    progress: document.querySelector("#summary-progress"),
    completed: document.querySelector("#summary-completed"),
  },
  taskInsights: {
    workload: document.querySelector("#task-workload-widget"),
    deadlines: document.querySelector("#task-deadlines-widget"),
    status: document.querySelector("#task-status-widget"),
  },
  dialog: document.querySelector("#task-dialog"),
  form: document.querySelector("#task-form"),
  closeDialog: document.querySelector("#close-dialog"),
  cancelDialog: document.querySelector("#cancel-dialog"),
  deleteButton: document.querySelector("#delete-task-button"),
  formError: document.querySelector("#form-error"),
  dialogKicker: document.querySelector("#dialog-kicker"),
  dialogTitle: document.querySelector("#dialog-title"),
  dialogSubtitle: document.querySelector("#dialog-subtitle"),
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
      throw new Error("Cliente da API de tarefas não foi carregado.");
    }
    state.tasks = await TaskApi.list();
  } catch (error) {
    state.tasks = [];
    state.error = error.message || "Não foi possível carregar as tarefas.";
  } finally {
    state.loading = false;
    render();
  }
}

async function loadDashboard() {
  state.dashboardLoading = true;
  state.dashboardError = "";
  render();

  try {
    state.dashboard = await TaskApi.dashboard();
  } catch (error) {
    state.dashboard = null;
    state.dashboardError = error.message || "Não foi possível carregar o painel.";
  } finally {
    state.dashboardLoading = false;
    render();
  }
}

async function loadKanban() {
  state.kanbanLoading = true;
  state.kanbanError = "";
  render();

  try {
    state.kanban = await TaskApi.kanban();
  } catch (error) {
    state.kanban = null;
    state.kanbanError = error.message || "Não foi possível carregar o Kanban.";
  } finally {
    state.kanbanLoading = false;
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

    if (state.priority && normalized.priority !== state.priority) {
      return false;
    }

    if (state.assignee && normalizeAssignee(task.assignee) !== state.assignee) {
      return false;
    }

    if (!term) {
      return true;
    }

    const values = [
      normalized.title,
      task.description,
      task.projectName,
      task.assignee,
      task.dueDate,
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
  const isDashboardView = state.view === "dashboard";
  const isKanbanView = state.view === "kanban";
  const isRoadmapView = state.view === "roadmap";

  el.tasksPanel.classList.toggle("hidden", !isTasksView);
  el.dashboardPanel.classList.toggle("hidden", !isDashboardView);
  el.kanbanPanel.classList.toggle("hidden", !isKanbanView);
  el.roadmapPanel.classList.toggle("hidden", !isRoadmapView);

  if (isDashboardView) {
    renderDashboardPanel();
    renderIcons();
    return;
  }

  if (isKanbanView) {
    renderKanbanPanel();
    renderIcons();
    return;
  }

  if (isRoadmapView) {
    renderRoadmapPanel();
    renderIcons();
    return;
  }

  el.pageHeading.textContent = "Tarefas";
  el.pageSubtitle.textContent = "Gerencie as tarefas do time";

  const tasks = visibleTasks();
  const hasTasks = tasks.length > 0;
  const hasError = Boolean(state.error);

  renderSummary();
  renderAssigneeFilter();
  renderTaskInsights(tasks);

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
  el.mainLinks.forEach((link) => {
    const isActive = state.view === link.dataset.mainView;
    link.classList.toggle("active", isActive);
    link.setAttribute("aria-current", isActive ? "page" : "false");
  });

  el.roadmapLinks.forEach((button) => {
    const isActive = state.view === "roadmap" && button.dataset.roadmapModule === state.roadmapModule;
    button.classList.toggle("active", isActive);
    button.setAttribute("aria-pressed", String(isActive));
  });
}

function renderDashboardPanel() {
  el.pageHeading.textContent = "Painel";
  el.pageSubtitle.textContent = "Indicadores operacionais do módulo de tarefas";

  const hasError = Boolean(state.dashboardError);
  const hasDashboard = Boolean(state.dashboard);

  el.dashboard.loading.classList.toggle("hidden", !state.dashboardLoading);
  el.dashboard.error.classList.toggle("hidden", !hasError || state.dashboardLoading);
  el.dashboard.content.classList.toggle("hidden", state.dashboardLoading || hasError || !hasDashboard);

  if (hasError) {
    el.dashboard.errorMessage.textContent = state.dashboardError;
  }

  if (!hasDashboard) {
    return;
  }

  const dashboard = normalizeDashboard(state.dashboard);
  el.dashboard.total.textContent = dashboard.totalTasks;
  el.dashboard.completed.textContent = dashboard.completedTasks;
  el.dashboard.overdue.textContent = dashboard.overdueTasks;
  el.dashboard.rate.textContent = `${formatPercent(dashboard.completionRate)}%`;
  el.dashboard.statusTotal.textContent = `${dashboard.totalTasks} tarefas`;
  el.dashboard.priorityTotal.textContent = `${dashboard.totalTasks} tarefas`;

  renderDistributionList(
    el.dashboard.statusList,
    statusOrder,
    dashboard.tasksByStatus,
    statusLabel,
    dashboard.totalTasks,
    "status"
  );
  renderDistributionList(
    el.dashboard.priorityList,
    priorityOrder,
    dashboard.tasksByPriority,
    priorityLabel,
    dashboard.totalTasks,
    "priority"
  );
  renderDashboardFocus(dashboard);
  renderDashboardRhythm(dashboard);
}

function renderDashboardFocus(dashboard) {
  const focusTasks = state.tasks
    .map(normalizeTaskWithMeta)
    .filter((task) => task.status !== "CONCLUIDO")
    .sort((a, b) => {
      const priorityDelta = priorityWeight(b.priority) - priorityWeight(a.priority);
      if (priorityDelta !== 0) {
        return priorityDelta;
      }
      return safeProgress(b.progress) - safeProgress(a.progress);
    })
    .slice(0, 5);

  el.dashboard.focusTotal.textContent = `${focusTasks.length} tarefas`;
  el.dashboard.focusList.innerHTML = focusTasks.length
    ? focusTasks.map(renderFocusItem).join("")
    : `<div class="mini-empty">Nenhuma tarefa aberta para priorizar.</div>`;
  bindTableActions();

  el.dashboard.rhythmTotal.textContent = `${formatPercent(dashboard.completionRate)}%`;
}

function renderFocusItem(task) {
  const status = statusConfig[task.status];
  const priority = priorityConfig[task.priority];
  const progress = safeProgress(task.progress);
  const project = task.projectName || "Sem projeto";
  const assignee = task.assignee || "Sem responsável";
  const dueDate = formatDueDate(task.dueDate) || "Sem prazo";

  return `
    <button class="focus-item" type="button" ${task.id !== null && task.id !== undefined ? `data-edit="${task.id}"` : "disabled"}>
      <span class="focus-rail ${priority?.className || "priority-empty"}"></span>
      <span class="focus-item-main">
        <strong>${escapeHtml(task.title)}</strong>
        <span class="focus-meta">
          <span>${escapeHtml(project)}</span>
          <span>${escapeHtml(assignee)}</span>
          <span>${escapeHtml(dueDate)}</span>
        </span>
      </span>
      <span class="focus-badges">
        <span class="status-pill ${status?.className || "status-pendente"}">${statusLabel(task.status)}</span>
        <span class="priority-pill ${priority?.className || "priority-empty"}">${priorityLabel(task.priority)}</span>
      </span>
      <span class="focus-progress">
        <strong>${progress}%</strong>
        <span class="focus-progress-track"><span style="width:${progress}%"></span></span>
      </span>
    </button>
  `;
}

function renderDashboardRhythm(dashboard) {
  const reviewTasks = Number(dashboard.tasksByStatus?.EM_REVISAO || 0);
  const progressTasks = Number(dashboard.tasksByStatus?.EM_PROGRESSO || 0);
  const pendingTasks = Number(dashboard.tasksByStatus?.PENDENTE || 0);
  const completedTasks = Number(dashboard.tasksByStatus?.CONCLUIDO || dashboard.completedTasks || 0);
  const rhythmItems = [
    { label: "Parado", value: pendingTasks, accent: "status-pendente" },
    { label: "Em produção", value: progressTasks, accent: "status-em-progresso" },
    { label: "Aguardando revisão", value: reviewTasks, accent: "status-em-revisao" },
    { label: "Concluído", value: completedTasks, accent: "status-concluido" },
  ];

  el.dashboard.rhythmList.innerHTML = rhythmItems.map((item) => {
    const percent = dashboard.totalTasks > 0 ? Math.round((item.value * 100) / dashboard.totalTasks) : 0;
    return `
      <div class="rhythm-item ${item.accent}">
        <span class="status-dot ${item.accent}"></span>
        <div>
          <strong>${escapeHtml(item.label)}</strong>
          <small>${item.value} tarefas · ${percent}%</small>
        </div>
        <span class="rhythm-count">${item.value}</span>
        <span class="rhythm-bar" aria-label="${escapeHtml(item.label)} ${percent}%">
          <span style="width:${Math.min(100, percent)}%"></span>
        </span>
      </div>
    `;
  }).join("");
}

function renderKanbanPanel() {
  el.pageHeading.textContent = "Kanban";
  el.pageSubtitle.textContent = "Tarefas agrupadas por status";

  const hasError = Boolean(state.kanbanError);
  const hasKanban = Boolean(state.kanban);

  el.kanban.loading.classList.toggle("hidden", !state.kanbanLoading);
  el.kanban.error.classList.toggle("hidden", !hasError || state.kanbanLoading);
  el.kanban.content.classList.toggle("hidden", state.kanbanLoading || hasError || !hasKanban);

  if (hasError) {
    el.kanban.errorMessage.textContent = state.kanbanError;
  }

  if (!hasKanban) {
    return;
  }

  const columns = normalizeKanbanColumns(state.kanban);
  el.kanban.board.innerHTML = columns.map((column) => renderKanbanColumn(column)).join("");
  bindKanbanActions();
}

function renderDistributionList(container, order, values, labelFn, totalTasks, type) {
  container.dataset.kind = type;
  container.innerHTML = order.map((key) => {
    const count = Number(values?.[key] || 0);
    const percent = totalTasks > 0 ? Math.round((count * 10000) / totalTasks) / 100 : 0;
    const className = type === "status"
      ? statusConfig[key]?.className || "status-pendente"
      : priorityConfig[key]?.className || "priority-empty";

    return `
      <div class="distribution-row" data-key="${key}">
        <div class="distribution-meta">
          <span class="distribution-name">
            <span class="status-dot ${className}"></span>
            <span>${labelFn(key)}</span>
          </span>
          <span class="distribution-values">
            <strong>${count}</strong>
            <small>${formatPercent(percent)}%</small>
          </span>
        </div>
        <div class="distribution-track" aria-label="${labelFn(key)} ${formatPercent(percent)}%">
          <span style="width:${Math.min(100, percent)}%"></span>
        </div>
      </div>
    `;
  }).join("");
}

function renderKanbanColumn(column) {
  const tasks = column.tasks || [];
  return `
    <section class="kanban-column" data-status="${column.status}" aria-labelledby="kanban-${column.status}">
      <header>
        <h2 id="kanban-${column.status}">
          <span class="status-dot ${statusConfig[column.status]?.className || "status-pendente"}"></span>
          ${escapeHtml(column.title || statusLabel(column.status))}
        </h2>
        <span>${tasks.length}</span>
      </header>
      <button class="kanban-add-card" type="button" data-kanban-new="${column.status}">
        <i data-lucide="plus"></i>
        Adicionar
      </button>
      <div class="kanban-list">
        ${tasks.length ? tasks.map(renderKanbanCard).join("") : renderKanbanEmpty()}
      </div>
    </section>
  `;
}

function renderKanbanCard(task) {
  const normalized = normalizeTaskWithMeta(task);
  const priority = priorityConfig[normalized.priority];
  const progress = safeProgress(normalized.progress);
  const canMutate = normalized.id !== null && normalized.id !== undefined;
  const meta = [normalized.projectName, normalized.assignee, formatDueDate(normalized.dueDate)].filter(Boolean);

  return `
    <button class="kanban-card" type="button" ${canMutate ? `data-kanban-edit="${normalized.id}"` : "disabled"}>
      <strong>${escapeHtml(normalized.title)}</strong>
      ${task.description ? `<span class="kanban-card-description">${escapeHtml(task.description)}</span>` : ""}
      ${meta.length ? `<span class="task-meta-line">${meta.map(escapeHtml).join(" / ")}</span>` : ""}
      <div class="kanban-card-meta">
        <span class="priority-pill ${priority?.className || "priority-empty"}">${priorityLabel(normalized.priority)}</span>
        <span>${progress}%</span>
      </div>
      <span class="progress-bar"><span style="width:${progress}%"></span></span>
    </button>
  `;
}

function renderKanbanEmpty() {
  return `
    <div class="kanban-empty">
      <i data-lucide="inbox"></i>
      <span>Nenhuma tarefa</span>
    </div>
  `;
}

function bindKanbanActions() {
  document.querySelectorAll("[data-kanban-edit]").forEach((button) => {
    button.addEventListener("click", () => {
      const task = state.tasks.find((item) => normalizeTask(item).id === Number(button.dataset.kanbanEdit));
      openDialog(task);
    });
  });

  document.querySelectorAll("[data-kanban-new]").forEach((button) => {
    button.addEventListener("click", () => openDialog(null, { status: button.dataset.kanbanNew }));
  });
}

function renderRoadmapPanel() {
  const module = roadmapModules[state.roadmapModule] || roadmapModules.calendar;
  el.pageHeading.textContent = module.title;
  el.pageSubtitle.textContent = "Módulo planejado no roadmap do produto";
  el.roadmap.status.textContent = "Em breve";
  el.roadmap.title.textContent = module.title;
  el.roadmap.description.textContent = module.description;
  el.roadmap.notice.textContent = "Este módulo ainda não está disponível nesta versão.";
}

function renderSummary() {
  const allTasks = state.tasks;
  const inProgress = countByStatus(allTasks, "EM_PROGRESSO");
  const completed = countByStatus(allTasks, "CONCLUIDO");
  const risk = allTasks.filter((task) => isTaskAtRisk(normalizeTaskWithMeta(task))).length;

  el.summary.total.textContent = allTasks.length;
  el.summary.risk.textContent = risk;
  el.summary.progress.textContent = inProgress;
  el.summary.completed.textContent = completed;
}

function countByStatus(tasks, status) {
  return tasks.filter((task) => normalizeTask(task).status === status).length;
}

function renderTable(tasks) {
  el.tableBody.innerHTML = tasks.map((task) => {
    const normalized = normalizeTaskWithMeta(task);
    const status = statusConfig[normalized.status];
    const priority = priorityConfig[normalized.priority];
    const progress = safeProgress(normalized.progress);
    const canMutate = normalized.id !== null && normalized.id !== undefined;
    const project = normalized.projectName || "Sem projeto";
  const assignee = normalized.assignee || "Sem responsável";
    const dueDate = formatDueDateLong(normalized.dueDate) || "-";
    const riskClass = isTaskAtRisk(normalized) ? " due-risk" : "";

    return `
      <tr>
        <td class="check-column" data-label="Selecionar">
          <input type="checkbox" aria-label="Selecionar ${escapeHtml(normalized.title)}">
        </td>
        <td class="task-primary-cell" data-label="Tarefa">
          <button class="task-name" type="button" ${canMutate ? `data-edit="${normalized.id}"` : "disabled"}>
            ${escapeHtml(normalized.title)}
          </button>
          ${task.description ? `<span class="task-description">${escapeHtml(task.description)}</span>` : ""}
        </td>
        <td data-label="Projeto">
          <span class="project-chip">
            <span class="project-dot" style="--project-color:${projectColor(project)}"></span>
            <span class="project-name">${escapeHtml(project)}</span>
          </span>
        </td>
        <td data-label="Responsável">
          <span class="assignee-chip">
            <span class="avatar-initials" style="--avatar-bg:${assigneeColor(assignee)}">${escapeHtml(initials(assignee))}</span>
            ${escapeHtml(assignee)}
          </span>
        </td>
        <td data-label="Status"><span class="status-pill ${status?.className || "status-pendente"}">${statusLabel(normalized.status)}</span></td>
        <td data-label="Prioridade"><span class="priority-pill ${priority?.className || "priority-empty"}">${priorityLabel(normalized.priority)}</span></td>
        <td class="due-date-cell${riskClass}" data-label="Prazo">${escapeHtml(dueDate)}</td>
        <td class="progress-cell" data-label="Progresso">
          <span class="progress-text">${progress}%</span>
          <span class="progress-bar"><span style="width:${progress}%"></span></span>
        </td>
        <td class="actions-cell" data-label="Ações">
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

function renderAssigneeFilter() {
  const assignees = uniqueAssignees(state.tasks);
  const current = state.assignee;
  el.assigneeFilter.innerHTML = [
    `<option value="">Todos</option>`,
    ...assignees.map((assignee) => `<option value="${escapeHtml(assignee)}">${escapeHtml(assignee)}</option>`),
  ].join("");
  el.assigneeFilter.value = assignees.includes(current) ? current : "";
  if (current && !assignees.includes(current)) {
    state.assignee = "";
  }
}

function renderTaskInsights(tasks) {
  renderWorkloadWidget(tasks);
  renderDeadlinesWidget(tasks);
  renderTaskStatusWidget(tasks);
}

function renderWorkloadWidget(tasks) {
  const grouped = groupCounts(tasks.map((task) => normalizeTaskWithMeta(task).assignee || "Sem responsável"));
  const sortedEntries = Object.entries(grouped).sort((a, b) => b[1] - a[1]);
  const topEntries = sortedEntries.slice(0, 4);
  const others = sortedEntries.slice(4).reduce((sum, [, count]) => sum + count, 0);
  const entries = others > 0 ? [...topEntries, ["Outros", others]] : topEntries;
  const total = entries.reduce((sum, [, count]) => sum + count, 0);

  if (!entries.length) {
    el.taskInsights.workload.innerHTML = `<div class="mini-empty">Sem responsáveis no filtro atual.</div>`;
    return;
  }

  el.taskInsights.workload.innerHTML = `
    <div class="workload-donut" style="${workloadDonutStyle(entries)}">
      <strong>${total}</strong>
      <span>tarefas</span>
    </div>
    <div class="workload-list">
      ${entries.map(([assignee, count]) => {
        const percent = total > 0 ? Math.round((count * 100) / total) : 0;
        return `
          <div class="workload-row">
            <span class="avatar-initials" style="--avatar-bg:${assigneeColor(assignee)}">${escapeHtml(initials(assignee))}</span>
            <span>${escapeHtml(assignee)}</span>
            <strong>${count}</strong>
            <span class="mini-bar"><span style="width:${percent}%"></span></span>
          </div>
        `;
      }).join("")}
    </div>
  `;
}

function renderDeadlinesWidget(tasks) {
  const deadlines = tasks
    .map(normalizeTaskWithMeta)
    .filter((task) => task.dueDate && task.status !== "CONCLUIDO")
    .sort((a, b) => dateValue(a.dueDate) - dateValue(b.dueDate))
    .slice(0, 5);

  el.taskInsights.deadlines.innerHTML = deadlines.length
    ? deadlines.map((task) => `
      <button class="deadline-item" type="button" ${task.id !== null && task.id !== undefined ? `data-edit="${task.id}"` : "disabled"}>
        <span>
          <strong>${escapeHtml(task.title)}</strong>
          <small>${escapeHtml(formatDueDateLong(task.dueDate))}</small>
        </span>
        <span class="priority-pill ${isTaskAtRisk(task) ? "priority-alta" : priorityConfig[task.priority]?.className || "priority-empty"}">
          ${isTaskAtRisk(task) ? "Em risco" : priorityLabel(task.priority)}
        </span>
      </button>
    `).join("")
    : `<div class="mini-empty">Nenhum prazo no filtro atual.</div>`;
  bindTableActions();
}

function renderTaskStatusWidget(tasks) {
  const total = tasks.length;
  el.taskInsights.status.innerHTML = statusOrder.map((status) => {
    const count = tasks.filter((task) => normalizeTask(task).status === status).length;
    const percent = total > 0 ? Math.round((count * 100) / total) : 0;
    return `
      <div class="status-widget-row">
        <span class="status-dot ${statusConfig[status]?.className || "status-pendente"}"></span>
        <span>${statusLabel(status)}</span>
        <strong>${count}</strong>
        <small>${percent}%</small>
      </div>
    `;
  }).join("");
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

function updateProgressControl(value = el.fields.progress.value) {
  const progress = safeProgress(value);
  el.fields.progress.value = progress;
  el.progressOutput.textContent = `${progress}%`;
  el.fields.progress.style.setProperty("--progress-value", `${progress}%`);
}

function openDialog(task = null, defaults = {}) {
  const normalized = task ? normalizeTask(task) : null;
  state.selectedTask = task;
  el.formError.textContent = "";
  el.deleteButton.classList.toggle("hidden", !task);
  el.dialogKicker.textContent = task ? "Editar tarefa" : "Nova tarefa";
  el.dialogTitle.textContent = normalized ? normalized.title : "Abrir tarefa";
  el.dialogSubtitle.textContent = task
    ? "Revise status, prazo, responsável e progresso antes de salvar."
    : "Registre uma nova demanda com responsável, projeto e prazo claros.";

  el.fields.id.value = normalized?.id || "";
  el.fields.title.value = normalized?.title || "";
  el.fields.description.value = task?.description || "";
  el.fields.assignee.value = task?.assignee || "";
  el.fields.project.value = task?.projectName || "";
  el.fields.status.value = normalized?.status || defaults.status || "PENDENTE";
  el.fields.priority.value = normalized?.priority || "MEDIA";
  el.fields.dueDate.value = task?.dueDate || "";
  updateProgressControl(normalized?.progress);

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

    invalidateDerivedViews();
    closeDialog();
    await loadTasks();
    await refreshActiveDerivedView();
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
    invalidateDerivedViews();
    closeDialog();
    showToast("Tarefa excluida");
    await loadTasks();
    await refreshActiveDerivedView();
  } catch (error) {
    el.formError.textContent = error.message;
    showToast(error.message);
  }
}

function bindEvents() {
  el.mainLinks.forEach((link) => {
    link.addEventListener("click", (event) => {
      event.preventDefault();
      showMainView(link.dataset.mainView);
    });
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
  el.dashboard.retryButton.addEventListener("click", loadDashboard);
  el.kanban.retryButton.addEventListener("click", loadKanban);
  el.fields.progress.addEventListener("input", () => updateProgressControl());

  el.taskSearch.addEventListener("input", () => {
    state.query = el.taskSearch.value;
    render();
  });

  el.statusFilter.addEventListener("change", async () => {
    state.status = el.statusFilter.value;
    render();
  });

  el.priorityFilter.addEventListener("change", () => {
    state.priority = el.priorityFilter.value;
    render();
  });

  el.assigneeFilter.addEventListener("change", () => {
    state.assignee = el.assigneeFilter.value;
    render();
  });

  el.clearTaskFilters.addEventListener("click", () => {
    state.query = "";
    state.status = "";
    state.priority = "";
    state.assignee = "";
    el.taskSearch.value = "";
    el.statusFilter.value = "";
    el.priorityFilter.value = "";
    el.assigneeFilter.value = "";
    render();
  });
}

function showMainView(view) {
  state.view = view;
  state.roadmapModule = "";
  window.history.replaceState(null, "", view === "tasks" ? "/" : `#${view}`);
  render();

  if (view === "dashboard" && !state.dashboard && !state.dashboardLoading) {
    loadDashboard();
  }

  if (view === "kanban" && !state.kanban && !state.kanbanLoading) {
    loadKanban();
  }
}

function showRoadmapModule(moduleKey) {
  state.view = "roadmap";
  state.roadmapModule = moduleKey;
  window.history.replaceState(null, "", `#${moduleKey}`);
  render();
}

function restoreInitialView() {
  const moduleKey = window.location.hash.replace("#", "");
  if (moduleKey === "dashboard" || moduleKey === "kanban") {
    state.view = moduleKey;
    return;
  }

  if (roadmapModules[moduleKey]) {
    state.view = "roadmap";
    state.roadmapModule = moduleKey;
  }
}

function invalidateDerivedViews() {
  state.dashboard = null;
  state.kanban = null;
}

async function refreshActiveDerivedView() {
  if (state.view === "dashboard") {
    await loadDashboard();
  }

  if (state.view === "kanban") {
    await loadKanban();
  }
}

function normalizeDashboard(payload = {}) {
  return {
    totalTasks: Number(payload.totalTasks || 0),
    tasksByStatus: payload.tasksByStatus || {},
    tasksByPriority: payload.tasksByPriority || {},
    overdueTasks: Number(payload.overdueTasks || 0),
    completedTasks: Number(payload.completedTasks || 0),
    completionRate: Number(payload.completionRate || 0),
  };
}

function normalizeKanbanColumns(payload = {}) {
  const columns = Array.isArray(payload.columns) ? payload.columns : [];
  const columnsByStatus = new Map(columns.map((column) => [column.status, column]));

  return statusOrder.map((status) => {
    const column = columnsByStatus.get(status) || {};
    return {
      status,
      title: column.title || statusLabel(status),
      total: Number(column.total || 0),
      tasks: Array.isArray(column.tasks) ? column.tasks : [],
    };
  });
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

function normalizeTaskWithMeta(task = {}) {
  return {
    ...normalizeTask(task),
    assignee: task.assignee || "",
    projectName: task.projectName || "",
    dueDate: task.dueDate || "",
  };
}

function normalizeAssignee(value) {
  return value || "Sem responsável";
}

function uniqueAssignees(tasks) {
  return Array.from(new Set(tasks.map((task) => normalizeAssignee(task.assignee)))).sort((a, b) => a.localeCompare(b));
}

function isTaskAtRisk(task) {
  return task.status !== "CONCLUIDO" && (task.priority === "ALTA" || isPastDue(task.dueDate));
}

function isPastDue(value) {
  if (!value) {
    return false;
  }

  const today = new Date();
  today.setHours(0, 0, 0, 0);
  return dateValue(value) < today.getTime();
}

function dateValue(value) {
  const date = new Date(`${value}T00:00:00`);
  return Number.isNaN(date.getTime()) ? Number.MAX_SAFE_INTEGER : date.getTime();
}

function groupCounts(values) {
  return values.reduce((acc, value) => {
    acc[value] = (acc[value] || 0) + 1;
    return acc;
  }, {});
}

function projectColor(project) {
  return pickColor(project, ["#5b3df5", "#08aeca", "#16b981", "#f59e0b", "#f43f5e", "#7c3aed"]);
}

function assigneeColor(assignee) {
  return pickColor(assignee, ["#5b3df5", "#0ea5e9", "#10b981", "#f97316", "#e11d48", "#64748b"]);
}

function pickColor(seed, colors) {
  const text = String(seed || "");
  const hash = Array.from(text).reduce((sum, char) => sum + char.charCodeAt(0), 0);
  return colors[hash % colors.length];
}

function initials(name) {
  return String(name || "Sem responsável")
    .split(/\s+/)
    .filter(Boolean)
    .slice(0, 2)
    .map((part) => part[0])
    .join("")
    .toUpperCase() || "UA";
}

function workloadDonutStyle(entries) {
  const colors = ["#5b3df5", "#08aeca", "#16b981", "#f59e0b", "#f43f5e"];
  const total = entries.reduce((sum, [, count]) => sum + count, 0) || 1;
  let current = 0;
  const stops = entries.map(([, count], index) => {
    const start = current;
    current += (count / total) * 100;
    return `${colors[index % colors.length]} ${start}% ${current}%`;
  });
  return `--workload-chart: conic-gradient(${stops.join(", ")});`;
}

function priorityWeight(priority) {
  return { ALTA: 3, MEDIA: 2, BAIXA: 1 }[priority] || 0;
}

function statusLabel(status) {
  return statusConfig[status]?.label || status || "Pendente";
}

function priorityLabel(priority) {
  return priorityConfig[priority]?.label || priority || "-";
}

function formatPercent(value) {
  const number = Number(value);
  if (Number.isNaN(number)) {
    return "0";
  }
  return Number.isInteger(number) ? String(number) : number.toFixed(2);
}

function formatDueDate(value) {
  if (!value) {
    return "";
  }

  const [year, month, day] = String(value).split("-");
  return year && month && day ? `${day}/${month}` : String(value);
}

function formatDueDateLong(value) {
  if (!value) {
    return "";
  }

  const [year, month, day] = String(value).split("-");
  return year && month && day ? `${day}/${month}/${year}` : String(value);
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

  if (state.view === "dashboard") {
    loadDashboard();
  }

  if (state.view === "kanban") {
    loadKanban();
  }
}

init();
