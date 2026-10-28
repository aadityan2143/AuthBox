# Authify

Authify is a full-stack authentication application built with a Spring Boot backend and a React + Vite frontend. It covers the core login flow for a modern web app, including user registration, login, JWT-based authentication, email verification, password reset, and profile access.

This project is designed to help freshers understand a real-world authentication flow and how a frontend and backend are connected in a local development environment.

## Tech Stack

| Layer | Technology |
| --- | --- |
| Backend | Java 21, Spring Boot 4, Spring Security, Spring Data JPA |
| Frontend | React 19, Vite, Bootstrap |
| Database | MySQL (default local config) / SQL Server (works with local setup) |
| Authentication | JWT stored in an HTTP-only cookie |
| Email | Spring Mail (SMTP, configurable for Brevo/Mailtrap/etc.) |
| Build Tool | Maven (backend), npm (frontend) |

## Features

- User registration with name, email, and password
- Login with email/password
- JWT authentication using an HTTP-only cookie
- Profile retrieval for authenticated users
- Email verification via OTP
- Password reset via OTP
- User data stored in a relational database
- React UI with route-based pages for login, home, email verification, and reset password

## Project Structure

```text
AuthBox/
├── backend/
│   ├── src/
│   ├── pom.xml
│   ├── mvnw
│   ├── mvnw.cmd
│   └── .mvn/
├── frontend/
│   ├── src/
│   ├── package.json
│   ├── package-lock.json
│   ├── vite.config.js
│   └── index.html
├── workbench creds.txt
└── README.md
```

## Backend Architecture

The backend is a Spring Boot application that exposes REST APIs under the context path:

```text
http://localhost:8080/api/v1
```

Important backend configuration in `backend/src/main/resources/application.properties` includes:

- database connection details
- JPA auto-update behavior
- JWT secret key
- server context path
- SMTP email configuration

Security is configured using Spring Security with JWT validation before requests are processed. The frontend sends cookies with credentials enabled so the HTTP-only JWT cookie can be used for session-based validation.

## Frontend Architecture

The frontend is a Vite React app with the following default routing:

- `/` → Home page
- `/login` → Login and register screen
- `/email-verify` → OTP verification page
- `/reset-password` → Password reset flow

The frontend connects to the backend using:

```js
http://localhost:8080/api/v1
```

This is configured in:

```text
frontend/src/utils/constants.js
```

## Prerequisites

Before running the project locally, install the following:

- Java 21
- Maven 3.9+
- Node.js 18+
- npm 9+
- Git
- A local database server:
  - MySQL 8 (default setup)
  - or SQL Server 2019/2022 Developer Edition (supported local alternative)
- An SMTP email provider or Mailtrap for OTP emails during development

## Local Environment Setup

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd AuthBox
```

### 2. Set Up the Database

#### Option A: Local MySQL (default configuration in this repo)

1. Install MySQL and start the MySQL service.
2. Create a database:

```sql
CREATE DATABASE authify_app;
```

3. Update the backend config file at `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/authify_app
spring.datasource.username=root
spring.datasource.password=your_mysql_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
```

4. If you are using MySQL 8 or newer, the app should auto-create the required tables when it starts because of `spring.jpa.hibernate.ddl-auto=update`.

#### Option B: Local SQL Server (used by many developers locally)

If your local environment uses SQL Server instead of MySQL, configure the backend with the SQL Server JDBC driver and connection details.

1. Install SQL Server 2019/2022 Developer Edition.
2. Open SQL Server Management Studio (SSMS) or use `sqlcmd` and create a database:

```sql
CREATE DATABASE AuthifyApp;
GO
```

3. Update `backend/pom.xml` and add the SQL Server JDBC dependency:

```xml
<dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>mssql-jdbc</artifactId>
    <scope>runtime</scope>
</dependency>
```

4. Update `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=AuthifyApp;encrypt=true;trustServerCertificate=true;
spring.datasource.username=sa
spring.datasource.password=YourStrong!Passw0rd
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
spring.jpa.hibernate.ddl-auto=update
```

5. If SQL Server is running locally, the app will automatically create the required tables when the backend starts.

> Note: The project currently ships with the MySQL configuration, but the app is compatible with a local SQL Server setup as well. This is the best choice if your machine uses SQL Server instead of MySQL.

### 3. Configure JWT and Email Settings

Update the backend config file with a real secret key and SMTP values:

```properties
jwt.secret.key=your_super_secret_key_here

spring.mail.host=smtp-relay.brevo.com
spring.mail.port=587
spring.mail.username=your_email_username
spring.mail.password=your_email_password
spring.mail.properties.email.smtp.auth=true
spring.mail.properties.email.smtp.starttls.enable=true
spring.mail.protocol=smtp
spring.mail.properties.email.smtp.from=your_sender_email@example.com
```

For local testing, Mailtrap is a better option if you do not want to use a production SMTP service.

Example Mailtrap configuration:

```properties
spring.mail.host=sandbox.smtp.mailtrap.io
spring.mail.port=2525
spring.mail.username=your_mailtrap_username
spring.mail.password=your_mailtrap_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

### 4. Run the Backend

From the project root:

```bash
cd backend
./mvnw spring-boot:run
```

On Windows PowerShell:

```powershell
cd backend
./mvnw.cmd spring-boot:run
```

The backend should start on:

```text
http://localhost:8080
```

The API base path is:

```text
http://localhost:8080/api/v1
```

### 5. Run the Frontend

Open a second terminal:

```bash
cd frontend
npm install
npm run dev
```

The frontend usually starts on:

```text
http://localhost:5173
```

## Default Login Flow

### Register a User

1. Open `http://localhost:5173/login`
2. Switch to the create account mode
3. Enter your name, email, and password
4. Submit the form

The backend will create the user and send a welcome email.

### Log In

1. Enter your email and password
2. The backend validates credentials
3. A JWT is generated and stored in an HTTP-only cookie
4. The frontend updates the login state and redirects to the home page

### Email Verification

After login, the user can verify their account using a 6-digit OTP sent by email.

### Reset Password

A user can request a password reset OTP, receive it in email, and set a new password.

## API Endpoints

The backend exposes these main endpoints:

| Method | Endpoint | Purpose |
| --- | --- | --- |
| POST | `/api/v1/register` | Register a new user |
| POST | `/api/v1/login` | Login and create JWT cookie |
| GET | `/api/v1/is-authenticated` | Check login status |
| GET | `/api/v1/profile` | Fetch logged-in user profile |
| POST | `/api/v1/send-otp` | Send account verification OTP |
| POST | `/api/v1/verify-email` | Verify the email OTP |
| POST | `/api/v1/send-reset-otp` | Send password reset OTP |
| POST | `/api/v1/reset-password` | Reset password |
| POST | `/api/v1/logout` | Clear JWT cookie |

## Important Notes for Freshers

- The frontend and backend run separately.
- The backend is the source of authentication logic.
- The frontend does not store the user password or JWT in local storage.
- The JWT is stored as a cookie and protected with `httpOnly`.
- If emails are not sending, check your SMTP configuration and provider credentials.
- If database connections fail, confirm:
  - the database server is running
  - the database name is correct
  - the username/password are correct
  - the port is open (3306 for MySQL, 1433 for SQL Server)
- CORS is configured for the frontend origin:

```text
http://localhost:5173
```

## Troubleshooting

### Backend startup fails

Check:

- Java 21 is installed
- Maven wrapper is executable
- database credentials are correct
- the correct DB driver is included in `pom.xml`

### Frontend fails to load or API calls fail

Check:

- `npm install` was executed
- backend is running on port 8080
- frontend is using the right backend URL in `frontend/src/utils/constants.js`
- the browser allows cookies for the backend domain

### Email OTP not received

Check:

- SMTP username/password are correct
- provider allows SMTP access
- your `spring.mail` configuration matches the provider
- a local test service like Mailtrap is being used for development

## Good Development Practices

- Keep `jwt.secret.key` unique and never expose it in public repositories.
- Use a separate local database for development instead of production data.
- Do not commit real credentials to source control.
- Use `.env` or environment-based secrets when moving toward a more professional setup.
- Test login, register, reset-password, and OTP verification flows before merging changes.

## Summary

This project is a clean example of a full-stack authentication application. It gives freshers a practical understanding of:

- server-side Java backend setup
- database-driven user authentication
- JWT cookie-based session handling
- frontend-backend API integration
- secure password storage and OTP flows
- local development setup with MySQL or SQL Server

If you follow the setup steps above, you should be able to run the app locally in a few minutes and understand how each part works together.
