let currentSectionId = null;

const coursesListEl = document.getElementById("courses-list");
const sectionsListEl = document.getElementById("sections-list");
const sectionContentEl = document.getElementById("section-content");
const currentCourseEl = document.getElementById("current-course");

const chatMessagesEl = document.getElementById("chat-messages");
const chatInputEl = document.getElementById("chat-input");
const chatSendBtn = document.getElementById("chat-send");

const errorBanner = document.getElementById("error-banner");

function showError(msg) {
  errorBanner.style.display = "block";
  errorBanner.textContent = msg;
  setTimeout(() => (errorBanner.style.display = "none"), 4500);
}

function setActive(selector, element) {
  document.querySelectorAll(selector).forEach(x => x.classList.remove("active"));
  element.classList.add("active");
}

function addChatMessage(role, text) {
  const wrap = document.createElement("div");
  wrap.className = `chat-message ${role}`;

  const r = document.createElement("div");
  r.className = "chat-message-role";
  r.textContent = role === "user" ? "You" : "AI";

  const b = document.createElement("div");
  b.innerHTML = window.marked ? marked.parse(text) : text;

  wrap.appendChild(r);
  wrap.appendChild(b);
  chatMessagesEl.appendChild(wrap);
  chatMessagesEl.scrollTop = chatMessagesEl.scrollHeight;
}

async function fetchJson(url, options) {
  const res = await fetch(url, options);
  const text = await res.text();

  let data;
  try { data = text ? JSON.parse(text) : null; }
  catch { data = text; }

  if (!res.ok) {
    const msg = (data && data.message) ? data.message : `Request failed: ${res.status}`;
    throw new Error(msg);
  }

  return data;
}

// ✅ Эти endpoints должны существовать у тебя.
// Если у тебя другие — скажи, я подстрою.
async function loadCourses() {
  return fetchJson("/api/courses");
}

async function loadSections(courseId) {
  return fetchJson(`/api/courses/${courseId}/sections`);
}

async function loadSection(sectionId) {
  // optional endpoint: если нет — просто вернёт ошибку и мы покажем fallback
  return fetchJson(`/api/sections/${sectionId}`);
}

async function askChat(sectionId, message) {
  return fetchJson(`/api/sections/${sectionId}/chat`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ message })
  });
}

function renderCourses(courses) {
  coursesListEl.innerHTML = "";

  if (!courses || courses.length === 0) {
    coursesListEl.innerHTML = `<li class="hint">No courses found</li>`;
    return;
  }

  courses.forEach(c => {
    const li = document.createElement("li");
    li.className = "course-item";
    li.innerHTML = `
      <div>${c.title ?? ("Course " + c.id)}</div>
      <div class="course-desc">${c.description ?? ""}</div>
    `;

    li.onclick = async () => {
      setActive(".course-item", li);
      currentCourseEl.textContent = c.title ?? "";

      try {
        const sections = await loadSections(c.id);
        renderSections(sections);
      } catch (e) {
        showError(e.message);
      }
    };

    coursesListEl.appendChild(li);
  });
}

function renderSections(sections) {
  sectionsListEl.innerHTML = "";
  chatMessagesEl.innerHTML = "";
  sectionContentEl.innerHTML = `<p class="hint">Choose a section to see its content.</p>`;
  currentSectionId = null;

  if (!sections || sections.length === 0) {
    sectionsListEl.innerHTML = `<li class="hint">No sections found</li>`;
    return;
  }

  sections.forEach(s => {
    const li = document.createElement("li");
    li.className = "section-item";
    li.textContent = s.title ?? ("Section " + s.id);

    li.onclick = async () => {
      setActive(".section-item", li);
      currentSectionId = s.id;

      // content
      try {
        const sec = await loadSection(s.id);
        if (sec && sec.content) {
          sectionContentEl.innerHTML = window.marked ? marked.parse(sec.content) : sec.content;
        } else {
          sectionContentEl.innerHTML = `<p class="hint">Selected section: ${s.id}</p>`;
        }
      } catch {
        sectionContentEl.innerHTML = `<p class="hint">Selected section: ${s.id}</p>`;
      }

      chatMessagesEl.innerHTML = "";
      addChatMessage("ai", `Ask me anything about section **${s.id}**.`);
    };

    sectionsListEl.appendChild(li);
  });
}

chatSendBtn.onclick = async () => {
  const message = chatInputEl.value.trim();
  if (!message) return;

  if (!currentSectionId) {
    showError("Please choose a section first.");
    return;
  }

  addChatMessage("user", message);
  chatInputEl.value = "";

  try {
    const data = await askChat(currentSectionId, message);
    addChatMessage("ai", data.reply ?? JSON.stringify(data, null, 2));
  } catch (e) {
    showError(e.message);
  }
};

(async function init() {
  try {
    const courses = await loadCourses();
    renderCourses(courses);
  } catch (e) {
    showError("UI opened, but /api/courses not working. Check backend endpoints.");
  }
})();