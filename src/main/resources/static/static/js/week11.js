// src/main/resources/static/js/week11.js

// Если backend на том же домене и порту (http://localhost:8080), оставляем пустую строку
const API_BASE = "";

// Если когда-нибудь будешь добавлять JWT токен – он подхватится отсюда
const token = localStorage.getItem("token");

// Заголовки для запросов
const authHeaders = token
  ? { "Content-Type": "application/json", Authorization: `Bearer ${token}` }
  : { "Content-Type": "application/json" };

// --- Состояние ---

let currentCourseId = null;
let currentSectionId = null;

// отдельный чат для каждой секции
const chatBySection = {};

// текст секций по id (для передачи контента в AI)
const sectionContents = {};

// --- DOM элементы ---

const coursesListEl = document.getElementById("courses-list");
const sectionsListEl = document.getElementById("sections-list");
const sectionContentEl = document.getElementById("section-content");
const currentCourseEl = document.getElementById("current-course");
const chatMessagesEl = document.getElementById("chat-messages");
const chatInputEl = document.getElementById("chat-input");
const chatSendBtn = document.getElementById("chat-send");
const errorBannerEl = document.getElementById("error-banner");

// --- Вспомогательные функции ---

function showError(message) {
  console.error(message);
  if (!errorBannerEl) return;
  errorBannerEl.textContent = "⚠️ " + message;
  errorBannerEl.style.display = "block";
  setTimeout(() => {
    errorBannerEl.style.display = "none";
  }, 5000);
}

// Рендер чата для текущей секции
function renderChat() {
  chatMessagesEl.innerHTML = "";

  if (!currentSectionId) {
    chatMessagesEl.innerHTML =
      '<p class="hint">Select a section to start chatting.</p>';
    return;
  }

  const messages = chatBySection[currentSectionId] || [];
  if (messages.length === 0) {
    chatMessagesEl.innerHTML =
      '<p class="hint">No messages yet. Ask your first question 🙂</p>';
    return;
  }

  messages.forEach((m) => {
    const div = document.createElement("div");
    div.classList.add("chat-message");
    div.classList.add(m.role === "user" ? "user" : "ai");

    const roleEl = document.createElement("div");
    roleEl.classList.add("chat-message-role");
    roleEl.textContent = m.role === "user" ? "You" : "Tutor AI";

    const textEl = document.createElement("div");
    textEl.textContent = m.content;

    div.appendChild(roleEl);
    div.appendChild(textEl);
    chatMessagesEl.appendChild(div);
  });

  // Скроллим вниз
  chatMessagesEl.scrollTop = chatMessagesEl.scrollHeight;
}

// --- Курсы ---

async function loadCourses() {
  coursesListEl.innerHTML = '<p class="hint">Loading courses...</p>';

  try {
    const res = await fetch(`${API_BASE}/api/courses`, {
      headers: authHeaders,
    });

    if (!res.ok) {
      throw new Error(`Failed to load courses (${res.status})`);
    }

    const data = await res.json();
    coursesListEl.innerHTML = "";

    if (!data || data.length === 0) {
      coursesListEl.innerHTML =
        '<p class="empty">No courses found. Add some data to the database.</p>';
      return;
    }

    data.forEach((course, index) => {
      const li = document.createElement("li");
      li.classList.add("course-item");
      li.dataset.courseId = course.id;

      const title = document.createElement("div");
      title.textContent = course.title || `Course #${course.id}`;
      li.appendChild(title);

      if (course.description) {
        const desc = document.createElement("div");
        desc.classList.add("course-desc");
        desc.textContent = course.description;
        li.appendChild(desc);
      }

      li.addEventListener("click", () => {
        selectCourse(course);
      });

      coursesListEl.appendChild(li);

      // Выбираем первый курс по умолчанию
      if (index === 0) {
        selectCourse(course);
      }
    });
  } catch (e) {
    showError(e.message);
    coursesListEl.innerHTML = '<p class="empty">Error loading courses.</p>';
  }
}

function selectCourse(course) {
  currentCourseId = course.id;
  currentCourseEl.textContent = "Course: " + (course.title || course.id);

  // подсветка выбранного курса
  const items = coursesListEl.querySelectorAll(".course-item");
  items.forEach((el) => {
    const id = Number(el.dataset.courseId);
    if (id === course.id) {
      el.classList.add("active");
    } else {
      el.classList.remove("active");
    }
  });

  loadSections(course.id);
}

// --- Секции ---

async function loadSections(courseId) {
  sectionsListEl.innerHTML = '<p class="hint">Loading sections...</p>';
  sectionContentEl.innerHTML =
    '<p class="hint">Choose a section to see its content.</p>';
  currentSectionId = null;
  renderChat();

  try {
    const res = await fetch(`${API_BASE}/api/courses/${courseId}/sections`, {
      headers: authHeaders,
    });

    if (!res.ok) {
      throw new Error(`Failed to load sections (${res.status})`);
    }

    const data = await res.json();
    sectionsListEl.innerHTML = "";

    if (!data || data.length === 0) {
      sectionsListEl.innerHTML =
        '<p class="empty">No sections for this course.</p>';
      return;
    }

    data.forEach((section, index) => {
      const li = document.createElement("li");
      li.classList.add("section-item");
      li.dataset.sectionId = section.id;
      li.textContent = `Section #${section.id}`;

      // сохраняем текст секции (Markdown)
      sectionContents[section.id] = section.content || "";

      li.addEventListener("click", () => {
        selectSection(section, li);
      });

      sectionsListEl.appendChild(li);

      if (index === 0) {
        selectSection(section, li);
      }
    });
  } catch (e) {
    showError(e.message);
    sectionsListEl.innerHTML =
      '<p class="empty">Error loading sections.</p>';
  }
}

function selectSection(section, element) {
  currentSectionId = section.id;

  // подсветка выбранной секции
  const items = sectionsListEl.querySelectorAll(".section-item");
  items.forEach((el) => el.classList.remove("active"));
  if (element) {
    element.classList.add("active");
  }

  // показываем контент (Markdown → HTML)
  const raw =
    section.content ||
    sectionContents[section.id] ||
    "";
  const html = window.marked ? window.marked.parse(raw) : raw;
  sectionContentEl.innerHTML = html;

  renderChat();
}

// --- Чат ---

async function handleSend() {
  if (!currentSectionId) {
    showError("Select a section first.");
    return;
  }

  const text = chatInputEl.value.trim();
  if (!text) return;

  const sectionId = currentSectionId;

  // Добавляем сообщение пользователя
  const userMsg = {
    role: "user",
    content: text,
    ts: Date.now(),
  };
  chatBySection[sectionId] = chatBySection[sectionId] || [];
  chatBySection[sectionId].push(userMsg);
  chatInputEl.value = "";
  renderChat();

  try {
    // Берём конспект секции (на будущее, если захочешь отправлять в AI)
    const sectionNotes = sectionContents[sectionId] || "";

    // Формируем промпт (сейчас backend просто получает message, но можно расширить)
    const prompt =
      "You are a helpful tutor for this course section.\n\n" +
      "Here are the lecture notes for this section:\n\n" +
      sectionNotes +
      "\n\nStudent question:\n" +
      text +
      "\n\nAnswer in a clear and concise way.";

    // ⚠️ ГЛАВНОЕ ИЗМЕНЕНИЕ:
    // ТеперЬ отправляем запрос на /api/sections/{sectionId}/chat
    const res = await fetch(`${API_BASE}/api/sections/${sectionId}/chat`, {
      method: "POST",
      headers: authHeaders,
      body: JSON.stringify({
        message: prompt, // backend читает поле "message"
      }),
    });

    if (!res.ok) {
      throw new Error("Chat request failed: " + res.status);
    }

    const data = await res.json();

    const answer =
      data.reply ||
      data.answer ||
      data.message ||
      "No answer field in AI response 😅";

    const aiMsg = {
      role: "assistant",
      content: answer,
      ts: Date.now(),
    };
    chatBySection[sectionId].push(aiMsg);
    renderChat();
  } catch (e) {
    showError(e.message);
    const aiMsg = {
      role: "assistant",
      content:
        "❌ Error while talking to AI. Check console and backend config.",
      ts: Date.now(),
    };
    chatBySection[sectionId].push(aiMsg);
    renderChat();
  }
}

// --- Инициализация ---

loadCourses();

chatSendBtn.addEventListener("click", handleSend);

chatInputEl.addEventListener("keydown", (e) => {
  // Ctrl+Enter / Cmd+Enter – отправить
  if (e.key === "Enter" && (e.ctrlKey || e.metaKey)) {
    e.preventDefault();
    handleSend();
  }
});
