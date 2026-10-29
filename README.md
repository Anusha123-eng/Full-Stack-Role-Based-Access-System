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

## 📁 Project Structure


RABC-Practice/
├── backend/                              # Spring Boot + Java backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── rbac/
│   │   │   │       └── backend/
│   │   │   │           ├── controller/   # REST API controllers
│   │   │   │           ├── service/      # Business logic
│   │   │   │           ├── repository/   # Database operations
│   │   │   │           ├── entity/       # Database entities
│   │   │   │           ├── dto/          # Data transfer objects
│   │   │   │           ├── security/     # JWT and security configuration
│   │   │   │           ├── config/       # Application configuration
│   │   │   │           └── BackendApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/                          # Backend tests
│   └── pom.xml                            # Maven dependencies
│
├── frontend/                              # React + Vite frontend
│   ├── src/
│   │   ├── components/                    # Reusable components
│   │   │   ├── Layout.jsx
│   │   │   ├── PrivateRoute.jsx
│   │   │   └── LoadingSpinner.jsx
│   │   ├── context/
│   │   │   └── AuthContext.jsx             # Authentication state
│   │   ├── pages/                          # Application pages
│   │   │   ├── Login.jsx
│   │   │   ├── Dashboard.jsx
│   │   │   ├── Users.jsx
│   │   │   ├── Roles.jsx
│   │   │   ├── Permissions.jsx
│   │   │   └── AuditLogs.jsx
│   │   ├── utils/
│   │   │   └── api.js                      # API configuration
│   │   ├── App.jsx                         # Root component and routes
│   │   ├── main.jsx                        # React entry point
│   │   └── index.css                       # Global styles
│   ├── public/                             # Static assets
│   ├── index.html
│   ├── vite.config.js
│   ├── tailwind.config.js
│   ├── postcss.config.js
│   └── package.json
│
├── .gitignore                              # Files excluded from Git
└── README.md                               # Project documentation


**Note:** This is a representative structure based on your project details, not a verified listing of every file. Keep only the folders and filenames that actually exist in your project, and adjust the Java package structure to match your code.


🚀 Quick Start Guide

Prerequisites

Install or configure:
- Java version required by your Spring Boot project
- IntelliJ IDEA (recommended for the backend)
- Node.js and npm (for the frontend)
- MySQL Server
- Git (optional, for cloning the repository)
  
1. Get the project
Clone your GitHub repository after replacing the URL with your
repository URL:
git clone https://github.com/YOUR_USERNAME/fullstack-rbac-system.git
cd RABC-Practice
If you downloaded the ZIP, extract it and open the project folder in
IntelliJ IDEA.

3. Configure MySQL
Start MySQL and create the database if it does not already exist:
CREATE DATABASE rbac_db;
Configure the backend datasource using your local database details. Do
not commit real passwords.

Example application.properties configuration:
spring.datasource.url=jdbc:mysql://localhost:3306/rbac_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=${JWT_SECRET}
These property names must match the properties expected by your backend
code. If your project uses a different JWT property name, keep that name
instead. Use a strong random JWT secret that meets your JWT library's
requirements.

Set DB_USERNAME, DB_PASSWORD, and JWT_SECRET in IntelliJ's Run →
Edit Configurations → Environment variables for the Spring Boot run
configuration. Do not paste actual secrets into this README or commit
them to GitHub.
spring.jpa.hibernate.ddl-auto=update is convenient for local
development. Review database migration and schema-management practices
before production use.

3. Start the backend
Open the backend folder in IntelliJ IDEA and wait for Maven
dependencies to load. Run BackendApplication.java.

Alternatively, from the backend directory, if the Maven wrapper is
present:
./mvnw spring-boot:run
On Windows PowerShell:
.\mvnw.cmd spring-boot:run
If the Maven wrapper is not present, use IntelliJ's Maven tools or an
installed Maven command:
mvn spring-boot:run
The backend is expected to start at http://localhost:8080 unless
configured otherwise.

4. Start the frontend
Open a second terminal:
cd frontend
npm install
npm run dev
Vite is expected to show a local URL such as http://localhost:5173.
Open that URL in your browser.

Ensure the API base URL in frontend/src/utils/api.js points to your
running backend (for example, http://localhost:8080/api if that
matches your controller routes). If your project uses a Vite proxy,
ensure its target also points to the Spring Boot port.

✨ Main Features
The project pages and backend should be checked to confirm the exact
feature set.

Authentication
- Login with credentials.
- JWT-based authentication, if configured in the backend.
- Authentication state and protected routes in the frontend.
  
User Management
- User listing and account management, according to implemented API
  operations.
- Role assignment, if supported by the backend.

Role Management
- Create and manage roles, according to implemented API operations.
- Assign permissions to roles, if supported.

Permission Management
- View and manage permissions.
- Permission-based UI access and backend authorization, where
  implemented.

Audit Logs
- View recorded audit events if audit logging is implemented and
  connected to the backend.

🔐 Security Notes
- Never commit real database passwords, JWT secrets, access tokens, or
  .env files.
- Store local credentials in environment variables.
- Keep backend authorization checks in place; hiding a frontend button
  is not sufficient to secure an API.
- Use HTTPS in deployed environments.
- Ensure CORS allows only the frontend origins you intend to trust.
- Confirm that passwords are hashed by the backend before claiming
  password-hashing support.
- If a secret has already been pushed to a public repository, rotate
  it. Deleting it in a later commit does not remove it from Git
  history.

🧪 Testing
Test the application using the features that exist in your
implementation:
1. Sign in with valid credentials.
2. Verify that invalid credentials are rejected.
3. Verify protected routes require authentication.
4. Check that users with different permissions see only the allowed UI
   features.
5. Test the corresponding backend API authorization directly (for
   example, with Postman).
6. Test user, role, and permission operations that are implemented.
7. Verify audit events are recorded if audit logging is enabled.
Do not publish sample usernames or passwords unless you have
intentionally created safe demo accounts for this repository. Never
publish real credentials.

🐛 Troubleshooting
Backend does not start
- Check that MySQL is running and the rbac_db database exists.
- Verify the Java version and Maven dependencies.
- Confirm DB_USERNAME, DB_PASSWORD, and JWT_SECRET are
  configured in the IntelliJ run configuration.
- Check whether port 8080 is already in use.
- Review the IntelliJ Run console for the exact error.
Frontend does not start
- Check that Node.js and npm are installed.
- Run npm install inside frontend.
- Check whether port 5173 is already in use.
- Review the terminal output from npm run dev.
Login or API requests fail
- Confirm the backend is running.
- Check the API base URL in frontend/src/utils/api.js.
- Verify the database connection and user records.
- Inspect the browser developer console and Network tab, and the
  backend logs.
- Confirm that the frontend's request format matches the backend
  endpoint and authentication requirements.
CORS errors
- Check the CORS configuration in Spring Boot.
- Allow the correct frontend origin (commonly http://localhost:5173
  for local Vite development).
- Restart the backend after configuration changes.

🚀 Future Enhancements
Consider adding these only if they are not already implemented:
- Multi-factor authentication
- Account lockout and password policy
- Advanced search and filtering
- Export of reports
- Automated unit and integration tests
- Docker setup
- CI/CD pipeline
- Production deployment documentation
