# Workout Tracker

A full-stack workout tracking app, built primarily as a hands-on learning project for **Spring Security** — working through basic authentication, role-based access control, JWT, CORS/CSRF, OAuth2 social login, and a custom Redis-backed rate-limiting security filter, in that order.

## Tech stack

**Backend:** Java 17, Spring Boot 4.1.1, Spring Security, Spring Data JPA, PostgreSQL, Redis, JWT (jjwt)
**Frontend:** React 19, TypeScript, Vite, Tailwind CSS, React Router

## Features

### Authentication & security

- Username/password registration and login, with BCrypt password hashing
- Stateless JWT authentication via a custom `JwtAuthFilter`
- Role-based access control (`USER` / `ADMIN`)
- Google OAuth2 social login — a hybrid flow where Google authenticates the user and the backend issues its own JWT, so the rest of the app treats every login the same way regardless of provider
- CORS configured for the separate frontend origin; CSRF disabled (a stateless JWT API doesn't need it)
- A custom Spring Security filter (`LoginLimitFilter`) rate-limits login attempts per username via Redis — 5 failed attempts locks that username out for 5 minutes

### Application

- Personal exercise catalog (name + difficulty level)
- Build workouts from that catalog, with inline exercise creation and search while building a workout
- View and manage existing workouts, adding/removing exercises after creation
- Admin-only endpoints for user and exercise management

## Project structure

```
workout/
├── src/main/java/com/workout_tracker/workout/
│   ├── controller/    REST controllers
│   ├── service/       Business logic
│   ├── model/          JPA entities (User, Workout, Exercise)
│   ├── security/       Security config, JWT filter, OAuth2 success handler, rate-limit filter
│   ├── jwt/             JWT utility
│   ├── repository/     Spring Data JPA repositories
│   ├── dto/             Request/response DTOs
│   └── exception/       Custom exceptions + global exception handler
├── docker-compose.yml  Postgres + Redis for local development
└── frontend/
    └── src/
        ├── pages/       Route-level pages
        ├── components/ Shared UI components
        ├── api/          Typed fetch wrappers per resource
        └── context/     Auth state (React Context)
```

## Running locally

### Prerequisites

- Java 17+
- Node.js
- Docker (for Postgres + Redis)

### 1. Start the data stores

```bash
docker-compose up -d
```

### 2. Set required environment variables

```bash
export DB_PASSWORD=your_postgres_password
export JWT_SECRET=some_long_random_string
export GOOGLE_CLIENT_ID=your_google_oauth_client_id
export GOOGLE_CLIENT_SECRET=your_google_oauth_client_secret
```

(`GOOGLE_CLIENT_ID` / `GOOGLE_CLIENT_SECRET` come from a Google Cloud Console OAuth client — see below.)

Optional, with sane defaults if omitted:

```bash
export JWT_EXPIRATION_TIME=3600000   # ms, default 1 hour
export OAUTH2_REDIRECT_URI=http://localhost:5173/oauth2/redirect
```

### 3. Start the backend

```bash
./mvnw spring-boot:run
```

Runs on `http://localhost:8080`.

### 4. Start the frontend

```bash
cd frontend
npm install
npm run dev
```

Runs on `http://localhost:5173`.

## Setting up Google OAuth2 login

1. Create a project in [Google Cloud Console](https://console.cloud.google.com).
2. Configure the OAuth consent screen (External; add yourself as a test user while the app is unverified).
3. Create an OAuth 2.0 Client ID (Web application) with authorized redirect URI:
   `http://localhost:8080/login/oauth2/code/google`
4. Use the generated client ID/secret as `GOOGLE_CLIENT_ID` / `GOOGLE_CLIENT_SECRET` above.

## API overview

| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/auth/register` | Public |
| POST | `/api/auth/login` | Public (rate-limited) |
| GET | `/api/auth/me` | Authenticated |
| GET | `/oauth2/authorization/google` | Public — starts Google login |
| GET, POST | `/api/exercises` | Authenticated |
| DELETE, PATCH | `/api/exercises/{id}` | Admin |
| POST | `/api/workouts` | Authenticated |
| GET | `/api/workouts/{id}` | Authenticated |
| PATCH | `/api/workouts/{id}/exercises/{exerciseId}/add`, `/remove` | Authenticated |
| GET | `/api/users/{id}` | Authenticated |
| GET | `/api/users/all` | Admin |
| GET | `/api/users/{id}/workouts` | Authenticated |

## Learning roadmap

This project was built by working through a Spring Security roadmap end to end:

- 🟢 **Beginner** — basic authentication, role-based access control, BCrypt password storage
- 🟡 **Intermediate** — JWT stateless auth, CORS/CSRF configuration, OAuth2 social login (Google)
- 🔴 **Advanced** — custom security filters (Redis-backed login rate limiting)

Backend built from scratch while working through a Spring Security learning roadmap. Frontend was scaffolded with Claude Code.
