🔐 RBAC System - Role-Based Access Control
A full-stack web-based Role-Based Access Control (RBAC) system demonstrating enterprise-grade security principles including authentication, authorization, audit logging, and permission management.

## 🎯 Project Objectives

This project implements a full-stack Role-Based Access Control (RBAC) system to demonstrate:

1. ✅ **Authentication** – Secure login using JWT.
2. ✅ **Authorization** – Permission-based access control for protected features.
3. ✅ **User Management** – Create, read, update, and delete users.
4. ✅ **Role Management** – Create roles and assign them to users.
5. ✅ **Permission Management** – Manage permissions for specific resources and actions.
6. ✅ **Audit Logging** – Track and review system activities.
7. ✅ **Security** – Implement password hashing, JWT token management, and CORS configuration.

## 🏗️ System Architecture

```text
┌─────────────────────────────────────────────────────────────┐
│                       Client Layer                          │
│             (React + Vite + Tailwind CSS)                   │
│                    Port 5173                                │
│                                                             │
│  • Login Page           • Dashboard                         │
│  • User Management      • Role Management                   │
│  • Permission Management • Audit Logs                       │
└────────────────────────────┬────────────────────────────────┘
                             │
                             │ HTTP/REST API + JWT
                             │
┌────────────────────────────▼────────────────────────────────┐
│                       API Layer                             │
│                (Spring Boot + Java)                         │
│                    Port 8080                                │
│                                                             │
│  • Authentication & JWT Security                            │
│  • User Management APIs                                     │
│  • Role Management APIs                                     │
│  • Permission Management APIs                               │
│  • Authorization & Access Control                           │
│  • Audit Logging                                            │
└────────────────────────────┬────────────────────────────────┘
                             │
                             │ Spring Data JPA / Hibernate
                             │
┌────────────────────────────▼────────────────────────────────┐
│                    Database Layer                           │
│                        (MySQL)                              │
│                                                             │
│  • Users Table          • Roles Table                       │
│  • Permissions Table    • Resources Table                   │
│  • User-Role Mapping    • Role-Permission Mapping           │
│  • Audit Logs                                               │
└─────────────────────────────────────────────────────────────┘
```

### 🔄 Request Flow

1. **User Login:** The user enters login credentials in the React frontend.
2. **Authentication:** Spring Boot validates the credentials and generates a JWT.
3. **API Requests:** The frontend sends HTTP requests to the backend with the JWT.
4. **Authorization:** The backend validates the token and checks the user's permissions.
5. **Database Operations:** Spring Data JPA/Hibernate interacts with MySQL to retrieve or update data.
6. **Response:** The backend returns the result to the frontend, which updates the UI.

7. ## 🛠️ Tech Stack

### Backend

| **Technology** | **Version** | **Purpose** |
|---|---|---|
| Java | Verify installed version | Programming language |
| Spring Boot | Verify `pom.xml` | Backend framework |
| Spring Security | Verify `pom.xml` | Authentication and authorization |
| Spring Data JPA | Verify `pom.xml` | Database operations |
| Hibernate | Managed by Spring Boot | ORM |
| MySQL | Verify installed version | Relational database |
| JWT | Verify `pom.xml` dependency | Token-based authentication |
| Maven | Verify `pom.xml` | Dependency management and build tool |

### Frontend

| **Technology** | **Version** | **Purpose** |
|---|---|---|
| React | 18.2.0 | UI framework |
| React Router DOM | 6.30.6 | Client-side routing |
| Axios | 1.6.2 | HTTP client for API requests |
| Tailwind CSS | 3.4.19 | Styling |
| Lucide React | 0.263.1 | Icons |
| Vite | 5.4.21 | Development server and build tool |

### Development Tools

| **Technology** | **Purpose** |
|---|---|
| IntelliJ IDEA | Java development and project management |
| Visual Studio Code | Frontend development |
| Git | Version control |
| GitHub | Source code hosting |
| Postman | API testing, if used |

