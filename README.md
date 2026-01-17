# task-managment-system

# 🗂️ Task Management System (Jira-like)

A **full-stack task management system** inspired by Jira, built using **Spring Boot** for the backend and **React** for the frontend.  
The application supports **projects, tasks, users, priorities, statuses, and JWT-based authentication**.

---

## 🚀 Tech Stack

### Backend
- Java 17
- Spring Boot
- Spring Data JPA (Hibernate)
- Spring Security + JWT
- PostgreSQL
- Maven
- Lombok

### Frontend
- React
- JavaScript (ES6+)
- Axios
- React Router

---

## 🏗️ Project Structure (Monorepo)


---

## ✨ Features

### 🔐 Authentication & Authorization
- JWT-based authentication
- Secure login & signup
- Role-based access control (Admin / User)

### 📁 Project Management
- Create and manage projects
- Assign tasks to projects

### ✅ Task Management
- Create, update, and delete tasks
- Task status workflow (TODO, IN_PROGRESS, DONE)
- Task priority (LOW, MEDIUM, HIGH)
- Due dates and timestamps

### 👤 User Management
- Assign tasks to users
- Track task creator and assignee

---


### Authentication Flow
1. User logs in from React
2. Backend validates credentials
3. JWT token is generated
4. Token is sent with every request
5. Spring Security validates token before processing requests

---

## 🛠️ Backend Setup

### Prerequisites
- Java 17
- Maven
- PostgreSQL

### Steps
```bash
cd backend
mvn clean install
mvn spring-boot:run
