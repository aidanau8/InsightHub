# 🚀 InsightHub — Spring Boot Internship Project

**InsightHub** is a backend-focused Spring Boot application developed as part of the Across GrowthHungry IT Academy Internship.  
The project follows a structured weekly progression, simulating real-world backend engineering tasks such as authentication, DTO mapping, validation, testing, security, and minimal frontend integration.

---

## 1. 📌 Project Overview

InsightHub is designed as a modular learning project that covers essential backend development practices:

- REST API design
- Authentication & validation
- DTO mapping
- Database integration
- Unit & integration testing
- Basic frontend connection (Thymeleaf)
- Secure AI chat integration (Week 6–7)

The goal is to develop clean architecture and strong engineering fundamentals using modern Spring Boot practices.

---

## 2. 🛠 Tech Stack

- **Java 17**
- **Spring Boot 3.x**
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring Security (Crypto + Endpoint Protection)
- H2 Database
- Thymeleaf
- Maven
- JUnit 5
- Mockito
- MockMvc

---

## 3. 📁 Project Structure


src/
└── main/
    ├── java/com/internship/insighthub/
    │       ├── configuration/
    │       ├── controller/
    │       ├── dto/
    │       ├── entity/
    │       ├── repository/
    │       └── service/
    └── resources/
        ├── templates/ (index.html, register.html)
        ├── static/css/style.css
        └── application.properties



## 4. 📅 Weekly Progress

### **Week 1 — Project Setup**
- Installed & configured **JDK 17**
- Created new **Spring Boot** project
- Set up **Maven**
- Added first **REST controller**
- Initialized **Git + GitHub** repository

---

### **Week 2 — Database & Repository Layer**
- Added **H2 in-memory database**
- Implemented **User** entity
- Created **UserRepository**
- Enabled **H2 Console**
- Tested baseline DB operations

---

### **Week 3 — Authentication Backend**
- Implemented full **registration flow**
- Added duplicate email/username validation
- Added **password hashing** (Spring Security Crypto)
- Implemented **login logic**
- Added custom exceptions + readable error messages

---

### **Week 4 — Unit & API Tests**
Configured testing environment:
- **JUnit 5**
- **MockMvc**
- **ObjectMapper**

Validated status codes:
- ✅ 201 Created  
- ❌ 400 Bad Request  
- ⚠️ 409 Conflict  
- 🔐 401 Unauthorized  

---

### **Week 5 — Thymeleaf Frontend Integration**
🎯 *Goal: Add a minimalistic UI for registration.*

Completed:
- Added **Thymeleaf dependency**
- Created **index.html** and **register.html**
- Implemented **MVC controller**
- Added **server-side validation**
- Added **client-side JS validation**
- Displayed error messages via Thymeleaf
- Added **FlashAttributes**
- Applied **PRG pattern**
- Added tests for empty request body

Result:  
A functional registration form connected to backend logic.

---

### **Week 6–7 — Secure AI Chat Integration**
#### 🔒 Backend
- Added `/api/chat` endpoint (ChatController)
- Implemented:
  - `ChatRequestDto`
  - `ChatResponseDto`
  - `ChatService` (AI API call via RestTemplate)
- Added global **CORS config**
- Updated **SecurityConfig** to protect `/api/chat`
- Implemented tests:
  - Unauthorized → 401  
  - Authorized → 200  
  - Invalid input → 400  

#### 💬 Frontend (React draft)
- Added **ProtectedRoute.js**
- Added **Chat.js** (basic UI + send logic)
- Updated routing: `/chat` is protected by token

---

### 🧪 Testing Summary

Tools:
- JUnit 5  
- Spring Boot Test  
- MockMvc  
- Mockito  
- Jackson ObjectMapper  

Coverage achieved:
- Registration success → **201**
- Empty/invalid fields → **400**
- Duplicate user → **409**
- Wrong login → **401**
- Chat unauthorized → **401**
- Chat authorized → **200**

All critical flows include tests..






