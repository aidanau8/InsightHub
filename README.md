# InsightHub — Spring Boot Internship Project 

InsightHub is a backend-focused Spring Boot application developed as part of the Across Hungry IT Academy Internship. The project follows a structured weekly progression to build clean architecture, authentication flow, database integration, and essential backend engineering practices.

---

## 1. Project Overview

InsightHub is designed as a modular educational project that simulates real-world backend development.  
The implementation includes REST API endpoints, server-side validation, DTO mapping, user authentication logic, unit testing, and an additional minimalistic frontend layer using Thymeleaf.

---

## 2. Tech Stack

- **Java 17**
- **Spring Boot 3.x**
- **Spring Web**
- **Spring Data JPA**
- **Spring Validation (Jakarta Bean Validation)**
- **H2 Database**
- **Thymeleaf**
- **Maven**
- **JUnit 5**, **Spring Boot Test**, **MockMvc**

---

## 3. Project Structure


---

## 4. Weekly Progress

### **Week 1 — Project Setup**
- Installed and configured JDK 17
- Initialized Spring Boot project
- Configured Maven
- Implemented initial REST controller
- Uploaded to GitHub

---

### **Week 2 — Database & Repository Setup**
- Added H2 in-memory database
- Implemented `User` entity
- Created Spring Data JPA repository
- Configured H2 console
- Verified database interactions

---

### **Week 3 — Authentication (Backend)**
- User registration service
- Email & username duplication checks
- Password hashing (Spring Security Crypto)
- Login validation
- Custom error messages and exceptions

---

### **Week 4 — Unit & API Testing**
- Configured Spring Boot Test environment
- Implemented MockMvc tests
- Tested all main authentication flows
- Verified response status codes (201, 400, 404, 409)

---

### **Week 5 — Frontend Integration (Thymeleaf)**
Objective: Add a simple user interface for registration.

Completed:
- Added Thymeleaf dependency
- Created `index.html` (landing page)
- Created `register.html` (registration form)
- Added `FrontendController` for MVC views
- Added server-side validation using `@Valid` + `BindingResult`
- Added client-side validation (JavaScript)
- Displayed backend validation errors using Thymeleaf
- Added FlashAttributes for:
    - error messages
    - success message
- Implemented PRG pattern (Post → Redirect → Get)
- Added API tests for empty request bodies

Result:  
A fully working registration page that communicates with the backend.

---

## 5. Testing

Tools:
- JUnit 5
- Spring Boot Test
- MockMvc
- ObjectMapper

Coverage:
- Successful registration → 201
- Missing fields → 400
- Duplicate email/username → 409
- Invalid login → 401

---


### Commands:

```bash
mvn clean install
mvn spring-boot:run



