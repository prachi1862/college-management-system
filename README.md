# College Management System

A Spring Boot backend application for managing students, departments, professors, and subjects in a college.

The project focuses on building REST APIs with proper layering, database relationships, validation, exception handling, and Swagger documentation.

---

## Tech Stack

- Java 24
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- ModelMapper
- Swagger / OpenAPI
- Maven

---

## Features

- Student management (CRUD operations)
- Department management (CRUD operations)
- Professor management
- Subject management
- Student enrollment in subjects (Many-to-Many)
- Input validation using annotations
- Global exception handling
- RESTful API design
- Swagger API documentation

---

## Entity Relationships

- Department → Students (One-to-Many)
- Department → Professors (One-to-Many)
- Professor → Subjects (One-to-Many)
- Student ↔ Subjects (Many-to-Many)

---

## API Documentation

Swagger UI:
http://localhost:8080/swagger-ui/index.html

---

## How to Run the Project

1. Clone the repository:
   git clone https://github.com/your-username/college-management-system.git

2. Create MySQL database:
   CREATE DATABASE college_management_system;

3. Configure application.properties:
   spring.datasource.url=jdbc:mysql://localhost:3306/college_management_system
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true

4. Run the application:
   mvn spring-boot:run

5. Open Swagger UI:
   http://localhost:8080/swagger-ui/index.html

---

## Project Structure

controller
service
repository
entity
dto
exception
config

---

## Future Improvements

- JWT Authentication and Role-based Access Control
- Pagination and Sorting
- Frontend integration using React
- Deployment on cloud (AWS / Render / Railway)
- Docker containerization

---

## Author

Prachi Mehra  
B.Tech Computer Science & Engineering  
Backend Developer (Spring Boot)
