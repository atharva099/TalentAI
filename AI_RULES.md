# AI Rules for TalentAI Project

This document outlines the core architectural and development principles to be adhered to throughout the TalentAI project. These rules ensure consistency, maintainability, scalability, and security across the codebase.

## Architectural Principles

### Layered Architecture
The application must follow a clear layered architecture (e.g., Presentation, Service, Domain, Data Access). Each layer should have a distinct responsibility and communicate with adjacent layers through well-defined interfaces. This promotes separation of concerns and modularity.

### DTO → Service → Repository Flow
All data flow for business operations must strictly follow the DTO (Data Transfer Object) to Service to Repository pattern.
- **DTOs:** Used for data transfer between the presentation layer and the service layer, and for external API communication. They should not contain business logic.
- **Service Layer:** Contains the core business logic and orchestrates operations between repositories.
- **Repository Layer:** Handles data persistence and retrieval, abstracting the underlying database.

### No Business Logic Inside Controllers
Controllers (in the Presentation layer) are responsible solely for handling HTTP requests, input validation, and returning appropriate HTTP responses. They must delegate all business logic to the Service layer.

## Development Best Practices

### Constructor Injection Only
Dependency injection must exclusively use constructor injection. This promotes immutability, testability, and makes dependencies explicit.

### Never Expose Passwords
Under no circumstances should passwords or other sensitive credentials be exposed in logs, API responses, or any unencrypted storage. Always use secure hashing and storage mechanisms.

### Always Use DTOs
All data exchanged between layers and external systems must be encapsulated within Data Transfer Objects (DTOs). This provides a clear contract for data, prevents over-fetching/under-fetching, and decouples internal domain models from external representations.

### Validation with Jakarta Validation
Input validation must be performed using Jakarta Validation (JSR 380) annotations. This ensures consistent and declarative validation rules are applied at the appropriate layer (typically the DTOs in the service layer entry points).

### Global Exception Handling
Implement a centralized global exception handling mechanism to manage and standardize error responses across the application. This ensures a consistent API error format and prevents sensitive information from leaking.

### JWT Authentication
Authentication and authorization must be implemented using JSON Web Tokens (JWT). This provides a stateless, scalable, and secure mechanism for user authentication.

### Clean Code Principles
Adhere to Clean Code principles, including meaningful names, small functions, clear comments (where necessary), and avoiding duplication. The code should be easy to read, understand, and maintain.

### SOLID Principles
Apply SOLID principles (Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion) to design robust, flexible, and maintainable software.

### JavaDoc for Public Classes
All public classes, interfaces, methods, and significant fields must be documented using JavaDoc. This ensures comprehensive API documentation and improves code readability and maintainability.

### Never Hardcode Secrets
Configuration secrets (API keys, database credentials, etc.) must never be hardcoded. Use environment variables, configuration management tools, or secure secret management services.

### Use ResponseEntity<ApiResponse<?>>
All API endpoints should return `ResponseEntity<ApiResponse<?>>` to provide a consistent and standardized response structure, including status codes, messages, and payload.

### Keep Methods Small
Methods should be concise and focused on a single responsibility. Long methods are harder to understand, test, and maintain.

### No Duplicate Logic
Avoid code duplication by identifying and abstracting common logic into reusable components, utility classes, or service methods.

### Security-First Development
Security considerations must be integrated into every stage of the development lifecycle, from design to deployment. This includes threat modeling, secure coding practices, and regular security reviews.

### Enterprise Naming Conventions
Follow established enterprise naming conventions for packages, classes, methods, variables, and database entities to ensure consistency and readability across the project.
