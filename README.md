# OpenWeather API Automation Tests

## Project Description

This project contains automated tests for the OpenWeather API. The tests are written in **Java** using the **Rest-Assured** library and follow the principles of API testing to validate functionality, schema, and response behavior.

## Features

- **Schema Validation**: Ensures the API response matches expected data types and structure.
- **Error Handling**: Tests for invalid API keys, missing parameters, and incorrect inputs.
- **Performance Testing**: Checks response times against defined thresholds.
- **Data Validation**: Confirms response data accuracy, such as temperature ranges and city names.

## Tools and Frameworks

- **Java 8+**: Programming language.
- **Rest-Assured**: Library for REST API testing.
- **JUnit 5**: Testing framework.
- **Maven**: Build and dependency management tool.

## Prerequisites

- Install [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html).
- Install [Apache Maven](https://maven.apache.org/install.html).
- Obtain an API key from [OpenWeather](https://openweathermap.org/api).

## Installation

1. Clone the repository
2. Navigate to the project directory:
   ```bash
   cd openWeather-automations
   ```
3. Build the project:
   ```bash
   mvn clean install
   ```

## Running Tests

To execute the tests:

1. Navigate to the project root directory.
2. Run the following command:
   ```bash
   mvn test
   ```

## Configuration

- Update the `API_KEY` constant in the test file (`OpenWeatherApiTests.java`) with your OpenWeather API key:
  ```java
  private static final String API_KEY = "YOUR_API_KEY";
  ```
