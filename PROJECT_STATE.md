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

The core recruitment backend modules are complete. The backend includes CORS configuration
for the local frontend and supports the authenticated current-user endpoint at
`GET /api/v1/auth/me`.

## Resume Scope

The current Resume module manages resume metadata only. Resume file storage, cloud storage,
resume parsing, and AI resume analysis are not implemented.

## Testing Status

- 41 automated tests passing
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

## Completed Frontend Authentication Integration

- React authentication context and provider
- Login integration with the Spring Boot backend
- Registration integration with the Spring Boot backend
- JWT access-token handling through the centralized authentication session helper
- Session persistence and restoration through browser `sessionStorage`
- Current-user hydration through `GET /api/v1/auth/me`
- Protected and public route guards
- Authenticated home page displaying the current user and persisted roles
- Centralized API client with Bearer token handling for authenticated requests
- Authentication error handling for invalid or expired sessions
- Backend CORS configuration for the local frontend
- Browser end-to-end authentication verification completed successfully
- Backend validation: 41 tests passed with 0 failures and 0 errors
- Frontend validation: lint passed and production build passed
- Latest authentication commit: `7dcd7b3`

## Next Major Task

Dashboard UI Integration.

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