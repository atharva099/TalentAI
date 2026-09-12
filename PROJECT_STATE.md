# TalentAI Project State

## Current Implementation

- Spring Boot
- Java 21
- Maven
- MySQL
- Backend port: 8081
- API prefix: `/api/v1`
- Bruno used for API testing
- Frontend: React, TypeScript, Vite, and ESLint
- Frontend directory: `talentai-frontend`
- Frontend development server: port 5173

## Completed Backend

- JWT authentication
- BCrypt password hashing
- Registration API
- Login API
- Protected endpoints
- Global exception handling
- Secure environment variable configuration
- Role-Based Access Control
- Roles: CANDIDATE, RECRUITER, COMPANY_ADMIN, PLATFORM_ADMIN
- User management
- Candidate management module
- Job management module
- Resume metadata module
- Dashboard summary API

The core recruitment backend modules are complete and ready for frontend integration.

## Resume Scope

The current Resume module manages resume metadata only. Resume file storage, cloud storage,
resume parsing, and AI resume analysis are not implemented.

## Testing Status

- 31 automated tests passing
- 0 failures
- 0 errors

## Completed Frontend Foundation

- Clean scalable frontend structure under `talentai-frontend/src`
- Components, context, layouts, pages, routes, services/api, types, utils, and assets folders
- Default Vite starter UI replaced with a TalentAI placeholder application page
- Application layout foundation
- Routing foundation
- API client foundation with `/api/v1` prefix
- Application context foundation
- TalentAI branding component
- Frontend build validation passed with `npm.cmd run build`
- Frontend lint validation passed with `npm.cmd run lint`

No frontend authentication or backend API integration has been implemented yet.

## Next Major Task

Frontend authentication integration.

Planned frontend work includes:

- API client connection to the Spring Boot backend
- Login UI
- Registration UI
- JWT handling
- Authentication context/state
- Protected routes
- Logout
- Integration testing against the real backend

## Future Backend and Product Work

- Resume file/cloud storage
- Resume parsing and advanced resume processing
- AI resume analysis, scoring, ranking, and suggestions
- Interview scheduling
- Advanced reporting
- Dashboard UI
- Candidate Management UI
- Job Management UI
- Resume UI
- Reports
- Docker, CI/CD, and production deployment

## Running

http://localhost:8081

## GitHub

https://github.com/atharva099/TalentAI