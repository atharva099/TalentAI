# TalentAI Project State

## Current Implementation

- Spring Boot
- Java 21
- Maven
- MySQL
- Backend port: 8081
- API prefix: `/api/v1`
- Bruno used for API testing

## Completed Backend

- JWT authentication
- BCrypt password hashing
- Registration API
- Login API
- Protected endpoints
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

## Next Major Task

Frontend foundation and integration.

Planned frontend work includes:

- React project setup
- Tailwind CSS configuration
- Authentication UI
- Dashboard integration
- Candidate and Job management interfaces
- Resume metadata integration

## Future Backend and Product Work

- Resume file/cloud storage
- Resume parsing and advanced resume processing
- AI resume analysis, scoring, ranking, and suggestions
- Interview scheduling
- Advanced reporting
- Docker, CI/CD, and production deployment

## Running

http://localhost:8081

## GitHub

https://github.com/atharva099/TalentAI