const API_URL = "/api/tasks";

const statusConfig = {
  PENDENTE: { label: "To Do", key: "todo", titleClass: "todo" },
  EM_PROGRESSO: { label: "In Progress", key: "progress", titleClass: "progress" },
  EM_REVISAO: { label: "In Review", key: "review", titleClass: "review" },
  CONCLUIDO: { label: "Completed", key: "completed", titleClass: "completed" },
};

const priorityConfig = {
  BAIXA: { label: "Low", className: "priority-baixa" },
  MEDIA: { label: "Medium", className: "priority-media" },
  ALTA: { label: "High", className: "priority-alta" },
};

const state = {
  tasks: [],
  view: "list",
  query: "",
  status: "",
  priority: "",
  collapsed: new Set(),
  selectedTask: null,
  openMenuId: null,
};

const el = {
  globalSearch: document.querySelector("#global-search"),
  taskSearch: document.querySelector("#task-search"),
  filterButton: document.querySelector("#filter-button"),
  filterPopover: document.querySelector("#filter-popover"),
  statusFilter: document.querySelector("#status-filter"),
  priorityFilter: document.querySelector("#priority-filter"),
  clearFilters: document.querySelector("#clear-filters"),
  listView: document.querySelector("#list-view"),
  kanbanView: document.querySelector("#kanban-view"),
  calendarView: document.querySelector("#calendar-view"),
  emptyState: document.querySelector("#empty-state"),
  addButtons: document.querySelectorAll("#add-task-button, [data-empty-add]"),
  quickStatusButtons: document.querySelectorAll("[data-quick-status]"),
  viewButtons: document.querySelectorAll("[data-view]"),
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

async function request(url, options = {}) {
  const response = await fetch(url, {
    headers: { "Content-Type": "application/json", ...options.headers },
    ...options,
  });

  if (!response.ok) {
    const payload = await response.json().catch(() => null);
    throw new Error(payload?.message || "Request failed.");
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

async function loadTasks() {
  const params = new URLSearchParams();
  if (state.status) {
    params.set("status", state.status);
  }
  if (state.priority) {
    params.set("priority", state.priority);
  }

  const endpoint = params.toString()
    ? `${API_URL}/filter?${params.toString()}`
    : `${API_URL}?page=0&size=100`;

  const data = await request(endpoint);
  state.tasks = Array.isArray(data) ? data : data.content || [];
  render();
}

function visibleTasks() {
  const term = state.query.trim().toLowerCase();
  if (!term) {
    return state.tasks;
  }

  return state.tasks.filter((task) => {
    const values = [
      taskCode(task),
      task.title,
      task.description,
      task.assignee,
      task.projectName,
      priorityConfig[task.priority]?.label,
      statusConfig[task.status]?.label,
    ];
    return values.some((value) => String(value || "").toLowerCase().includes(term));
  });
}

function render() {
  const tasks = visibleTasks();
  el.emptyState.classList.toggle("hidden", tasks.length !== 0);
  renderView(tasks);
  renderIcons();
}

function renderView(tasks) {
  el.listView.classList.toggle("hidden", state.view !== "list" || tasks.length === 0);
  el.kanbanView.classList.toggle("hidden", state.view !== "kanban" || tasks.length === 0);
  el.calendarView.classList.toggle("hidden", state.view !== "calendar" || tasks.length === 0);

  if (state.view === "list") {
    renderList(tasks);
  }
  if (state.view === "kanban") {
    renderKanban(tasks);
  }
  if (state.view === "calendar") {
    renderCalendar(tasks);
  }
}

function groupedTasks(tasks) {
  return Object.keys(statusConfig).map((status) => ({
    status,
    config: statusConfig[status],
    tasks: tasks.filter((task) => task.status === status),
  }));
}

function renderList(tasks) {
  el.listView.innerHTML = groupedTasks(tasks)
    .map(({ status, config, tasks: sectionTasks }) => {
      const collapsed = state.collapsed.has(status);
      const rows = collapsed ? "" : renderRows(sectionTasks);
      return `
        <section class="task-section" data-section="${status}">
          <header class="section-head">
            <span class="section-title ${config.titleClass}">${config.label}</span>
            <span class="section-count">${sectionTasks.length}</span>
            <button class="collapse-button" type="button" data-collapse="${status}" aria-label="Toggle ${config.label}">
              <i data-lucide="${collapsed ? "chevron-down" : "chevron-up"}"></i>
            </button>
            <button class="view-all" type="button" data-view-all="${status}">View All <i data-lucide="arrow-up-right"></i></button>
          </header>
          <table class="task-table">
            <thead>
              <tr>
                <th><input type="checkbox" aria-label="Select section"></th>
                <th><span class="sort-label">Task ID <i data-lucide="chevrons-up-down"></i></span></th>
                <th><span class="sort-label">Task Name <i data-lucide="chevrons-up-down"></i></span></th>
                <th><span class="sort-label">Assignee <i data-lucide="chevrons-up-down"></i></span></th>
                <th><span class="sort-label">Project Name <i data-lucide="chevrons-up-down"></i></span></th>
                <th><span class="sort-label">Progress <i data-lucide="chevrons-up-down"></i></span></th>
                <th><span class="sort-label">Deadline <i data-lucide="chevrons-up-down"></i></span></th>
                <th><span class="sort-label">Priority <i data-lucide="chevrons-up-down"></i></span></th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>${rows}</tbody>
          </table>
        </section>
      `;
    })
    .join("");

  bindDynamicActions();
}

function renderRows(tasks) {
  if (tasks.length === 0) {
    return `
      <tr>
        <td colspan="9" class="group-empty">
          <div class="group-empty-content">
            <i data-lucide="clipboard-list"></i>
            <span>No tasks in this group</span>
            <button class="ghost-button" type="button" data-empty-add>
              <i data-lucide="plus"></i>
              Create New Task
            </button>
          </div>
        </td>
      </tr>
    `;
  }

  return tasks.map((task) => {
    const priority = priorityConfig[task.priority] || priorityConfig.MEDIA;
    const progress = safeProgress(task.progress);
    return `
      <tr>
        <td><input type="checkbox" aria-label="Select ${escapeHtml(task.title)}"></td>
        <td class="task-id">${taskCode(task)}</td>
        <td><button class="task-name" type="button" data-edit="${task.id}">${escapeHtml(task.title)}</button></td>
        <td>
          <span class="person">
            <span class="avatar ${avatarTone(task.assignee)}">${initials(task.assignee)}</span>
            ${escapeHtml(task.assignee || "Unassigned")}
          </span>
        </td>
        <td class="project-name">${escapeHtml(task.projectName || "General Workspace")}</td>
        <td class="progress-cell">
          <span class="progress-text">${progress}%</span>
          <span class="progress-bar"><span style="width:${progress}%"></span></span>
        </td>
        <td class="deadline">${formatDate(task.dueDate)}</td>
        <td><span class="priority-pill ${priority.className}">${priority.label}</span></td>
        <td class="row-menu">
          <button class="row-action" type="button" data-menu="${task.id}" aria-label="Open actions">
            <i data-lucide="ellipsis"></i>
          </button>
          ${state.openMenuId === task.id ? actionMenu(task) : ""}
        </td>
      </tr>
    `;
  }).join("");
}

function actionMenu(task) {
  return `
    <div class="action-menu">
      <button type="button" data-edit="${task.id}"><i data-lucide="pencil"></i>Edit</button>
      <button type="button" data-complete="${task.id}"><i data-lucide="circle-check"></i>Complete</button>
      <button type="button" data-delete="${task.id}"><i data-lucide="trash-2"></i>Delete</button>
    </div>
  `;
}

function renderKanban(tasks) {
  el.kanbanView.innerHTML = groupedTasks(tasks)
    .map(({ config, tasks: columnTasks }) => `
      <section class="kanban-column">
        <h3>${config.label} <span class="section-count">${columnTasks.length}</span></h3>
        ${columnTasks.map((task) => `
          <button class="kanban-card" type="button" data-edit="${task.id}">
            <strong>${escapeHtml(task.title)}</strong>
            <span>${escapeHtml(task.projectName || "General Workspace")}</span>
            <span>${safeProgress(task.progress)}% - ${priorityConfig[task.priority]?.label || "Medium"}</span>
          </button>
        `).join("") || "<span class='project-name'>No tasks.</span>"}
      </section>
    `)
    .join("");

  bindDynamicActions();
}

function renderCalendar(tasks) {
  const byDay = new Map();
  tasks.forEach((task) => {
    const key = task.dueDate || "No deadline";
    byDay.set(key, [...(byDay.get(key) || []), task]);
  });

  el.calendarView.innerHTML = [...byDay.entries()]
    .sort(([a], [b]) => a.localeCompare(b))
    .map(([day, dayTasks]) => `
      <section class="calendar-day">
        <h3>${day === "No deadline" ? day : formatDate(day)}</h3>
        ${dayTasks.map((task) => `
          <button class="kanban-card" type="button" data-edit="${task.id}">
            <strong>${escapeHtml(task.title)}</strong>
            <span>${statusConfig[task.status]?.label || "Task"} - ${escapeHtml(task.assignee || "Unassigned")}</span>
          </button>
        `).join("")}
      </section>
    `)
    .join("");

  bindDynamicActions();
}

function bindDynamicActions() {
  document.querySelectorAll("[data-collapse]").forEach((button) => {
    button.addEventListener("click", () => {
      const status = button.dataset.collapse;
      if (state.collapsed.has(status)) {
        state.collapsed.delete(status);
      } else {
        state.collapsed.add(status);
      }
      render();
    });
  });

  document.querySelectorAll("[data-view-all]").forEach((button) => {
    button.addEventListener("click", () => {
      state.status = button.dataset.viewAll;
      el.statusFilter.value = state.status;
      loadTasks();
    });
  });

  document.querySelectorAll("[data-edit]").forEach((button) => {
    button.addEventListener("click", () => {
      const task = state.tasks.find((item) => item.id === Number(button.dataset.edit));
      openDialog(task);
    });
  });

  document.querySelectorAll("[data-menu]").forEach((button) => {
    button.addEventListener("click", () => {
      state.openMenuId = state.openMenuId === Number(button.dataset.menu) ? null : Number(button.dataset.menu);
      render();
    });
  });

  document.querySelectorAll("[data-complete]").forEach((button) => {
    button.addEventListener("click", () => completeTask(Number(button.dataset.complete)));
  });

  document.querySelectorAll("[data-delete]").forEach((button) => {
    button.addEventListener("click", () => deleteTask(Number(button.dataset.delete)));
  });

  el.listView.querySelectorAll("[data-empty-add]").forEach((button) => {
    button.addEventListener("click", () => openDialog());
  });
}

function openDialog(task = null) {
  state.selectedTask = task;
  el.formError.textContent = "";
  el.deleteButton.classList.toggle("hidden", !task);
  el.dialogKicker.textContent = task ? "Edit Task" : "New Task";
  el.dialogTitle.textContent = task ? task.title : "Add task details";

  el.fields.id.value = task?.id || "";
  el.fields.title.value = task?.title || "";
  el.fields.description.value = task?.description || "";
  el.fields.assignee.value = task?.assignee || "";
  el.fields.project.value = task?.projectName || "";
  el.fields.status.value = task?.status || "PENDENTE";
  el.fields.priority.value = task?.priority || "MEDIA";
  el.fields.dueDate.value = task?.dueDate || "";
  el.fields.progress.value = safeProgress(task?.progress);
  el.progressOutput.textContent = `${safeProgress(task?.progress)}%`;

  el.dialog.showModal();
  renderIcons();
}

function closeDialog() {
  el.dialog.close();
  state.selectedTask = null;
  state.openMenuId = null;
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
      await request(`${API_URL}/${el.fields.id.value}`, {
        method: "PUT",
        body: JSON.stringify(payload),
      });
      showToast("Task updated");
    } else {
      await request(API_URL, {
        method: "POST",
        body: JSON.stringify(payload),
      });
      showToast("Task added");
    }

    closeDialog();
    await loadTasks();
  } catch (error) {
    el.formError.textContent = error.message;
  }
}

async function completeTask(id) {
  try {
    await request(`${API_URL}/${id}`, {
      method: "PUT",
      body: JSON.stringify({ status: "CONCLUIDO", progress: 100 }),
    });
    state.openMenuId = null;
    showToast("Task completed");
    await loadTasks();
  } catch (error) {
    showToast(error.message);
  }
}

async function deleteTask(id = state.selectedTask?.id) {
  if (!id) {
    return;
  }

  const task = state.tasks.find((item) => item.id === id);
  const confirmed = window.confirm(`Delete "${task?.title || "this task"}"?`);
  if (!confirmed) {
    return;
  }

  try {
    await request(`${API_URL}/${id}`, { method: "DELETE" });
    closeDialog();
    showToast("Task deleted");
    await loadTasks();
  } catch (error) {
    el.formError.textContent = error.message;
    showToast(error.message);
  }
}

function bindEvents() {
  el.addButtons.forEach((button) => button.addEventListener("click", () => openDialog()));
  el.closeDialog.addEventListener("click", closeDialog);
  el.cancelDialog.addEventListener("click", closeDialog);
  el.form.addEventListener("submit", saveTask);
  el.deleteButton.addEventListener("click", () => deleteTask());
  el.fields.progress.addEventListener("input", () => {
    el.progressOutput.textContent = `${el.fields.progress.value}%`;
  });

  [el.globalSearch, el.taskSearch].forEach((input) => {
    input.addEventListener("input", () => {
      state.query = input.value;
      el.globalSearch.value = state.query;
      el.taskSearch.value = state.query;
      render();
    });
  });

  el.filterButton.addEventListener("click", () => {
    el.filterPopover.hidden = !el.filterPopover.hidden;
  });

  el.statusFilter.addEventListener("change", async () => {
    state.status = el.statusFilter.value;
    await loadTasks();
  });

  el.priorityFilter.addEventListener("change", async () => {
    state.priority = el.priorityFilter.value;
    await loadTasks();
  });

  el.clearFilters.addEventListener("click", async () => {
    state.status = "";
    state.priority = "";
    el.statusFilter.value = "";
    el.priorityFilter.value = "";
    await loadTasks();
  });

  el.quickStatusButtons.forEach((button) => {
    button.addEventListener("click", async () => {
      state.status = button.dataset.quickStatus;
      el.statusFilter.value = state.status;
      await loadTasks();
    });
  });

  el.viewButtons.forEach((button) => {
    button.addEventListener("click", () => {
      state.view = button.dataset.view;
      el.viewButtons.forEach((item) => item.classList.toggle("active", item === button));
      render();
    });
  });

  document.addEventListener("click", (event) => {
    if (!event.target.closest(".row-menu")) {
      if (state.openMenuId !== null) {
        state.openMenuId = null;
        render();
      }
    }
  });
}

function taskCode(task) {
  const id = String(task.id || 0).padStart(3, "0");
  return `P${991000 + Number(id)}-${id.slice(-1)}`;
}

function initials(name = "") {
  const parts = name.trim().split(/\s+/).filter(Boolean);
  if (parts.length === 0) {
    return "NA";
  }
  return parts.slice(0, 2).map((part) => part[0]).join("").toUpperCase();
}

function avatarTone(name = "") {
  const tones = ["", "coral", "blue"];
  const index = [...name].reduce((sum, char) => sum + char.charCodeAt(0), 0) % tones.length;
  return tones[index];
}

function safeProgress(progress = 0) {
  const value = Number(progress);
  if (Number.isNaN(value)) {
    return 0;
  }
  return Math.min(100, Math.max(0, value));
}

function formatDate(date) {
  if (!date) {
    return "No deadline";
  }

  return new Intl.DateTimeFormat("en-US", {
    month: "long",
    day: "numeric",
    year: "numeric",
  }).format(new Date(`${date}T00:00:00`));
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

async function init() {
  bindEvents();
  renderIcons();

  try {
    await loadTasks();
  } catch (error) {
    showToast(error.message);
  }
}

init();
