# AILab - Spring Boot with LangChain4j

This project demonstrates the integration of [LangChain4j](https://github.com/langchain4j/langchain4j-spring) with
Spring Boot.

## Features

- Spring Boot 3.2.0
- LangChain4j 0.25.0
- REST API to retrieve supported LangChain versions

## API Endpoints

### Get LangChain Versions

```
GET /api/langchain/versions
```

Returns a JSON object with information about the supported LangChain versions.

Example response:

```json
{
  "langchain4j-version": "0.25.0",
  "langchain4j-spring-boot-starter": "0.25.0"
}
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven

### Running the Application

1. Clone the repository
2. Build the project:
   ```
   mvn clean install
   ```
3. Run the application:
   ```
   mvn spring-boot:run
   ```
4. Access the API at http://localhost:8080/api/langchain/versions

## Configuration

You can configure the application by modifying the `application.properties` file:

```properties
# Application configuration
server.port=8080

# LangChain4j configuration
langchain4j.version=0.25.0
```