const TaskApi = (() => {
  const API_URL = "/api/tasks";

  async function request(url, options = {}) {
    const response = await fetch(url, {
      headers: { "Content-Type": "application/json", ...options.headers },
      ...options,
    });

    if (!response.ok) {
      const payload = await response.json().catch(() => null);
      throw new Error(payload?.message || "Nao foi possivel completar a operacao.");
    }

    if (response.status === 204) {
      return null;
    }

    return response.json();
  }

  async function list({ status } = {}) {
    if (status) {
      const params = new URLSearchParams({ status });
      return normalizeTasksResponse(await request(`${API_URL}/filter?${params.toString()}`));
    }

    const firstPage = await request(`${API_URL}?page=0&size=100`);
    const tasks = normalizeTasksResponse(firstPage);
    const totalPages = Number(firstPage?.totalPages || 1);

    if (!firstPage?.content || totalPages <= 1) {
      return tasks;
    }

    const remainingPages = [];
    for (let page = 1; page < totalPages; page += 1) {
      remainingPages.push(request(`${API_URL}?page=${page}&size=100`));
    }

    const pages = await Promise.all(remainingPages);
    return tasks.concat(pages.flatMap(normalizeTasksResponse));
  }

  function normalizeTasksResponse(payload) {
    if (Array.isArray(payload)) {
      return payload;
    }

    if (Array.isArray(payload?.content)) {
      return payload.content;
    }

    if (Array.isArray(payload?.data)) {
      return payload.data;
    }

    return [];
  }

  function create(payload) {
    return request(API_URL, {
      method: "POST",
      body: JSON.stringify(payload),
    });
  }

  function update(id, payload) {
    return request(`${API_URL}/${id}`, {
      method: "PUT",
      body: JSON.stringify(payload),
    });
  }

  function remove(id) {
    return request(`${API_URL}/${id}`, { method: "DELETE" });
  }

  function dashboard() {
    return request(`${API_URL}/dashboard`);
  }

  function kanban() {
    return request(`${API_URL}/kanban`);
  }

  return {
    list,
    normalizeTasksResponse,
    create,
    update,
    remove,
    dashboard,
    kanban,
  };
})();
