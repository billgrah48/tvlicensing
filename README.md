[readme.txt](https://github.com/user-attachments/files/26168594/readme.txt)
# TV Licence

A Spring Boot web application for managing TV licence registrations, dashboard access, and licence status for customers. The application allows users to register, log in, view their dashboard, and manage licence-related actions in a secure way.

## Features

- Customer registration with validation.
- Secure login and protected dashboard access.
- Customer dashboard showing account details and licences.
- Licence purchase and cancellation flow.
- Spring Security protection for authenticated pages.
- Thymeleaf-based server-side rendering.
- Automated tests for controller, service, and validation logic.

## Tech Stack

- Java
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Thymeleaf
- H2 Database
- Maven
- JUnit 5
- Mockito
- MockMvc

## Project Structure

- `controller` — handles web requests and page navigation.
- `service` — contains business logic.
- `repository` — database access layer.
- `model` — application entities.
- `config` — security and application configuration.
- `templates` — Thymeleaf pages and fragments.
- `src/test` — automated tests.

## Requirements

- Java 25 or compatible JDK.
- Maven installed.
- A supported IDE such as IntelliJ IDEA or Eclipse.

## Setup

1. Clone the repository.

```bash
git clone <repository-url>
cd tvlicensing
