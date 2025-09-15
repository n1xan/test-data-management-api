# Test Data Management API

A comprehensive test data management framework built on the BELLATRIX testing framework, designed for automated testing with external APIs like Trello.

## Overview

This project provides a robust, hierarchical test data management system that enables automated creation, manipulation, and cleanup of test data across multiple entities. It's particularly useful for integration testing scenarios where you need to create complex data relationships (e.g., Boards → Lists → Cards).

## Features

- **Hierarchical Entity Management**: Support for complex entity relationships with automatic dependency resolution
- **Factory Pattern**: Clean, reusable entity creation with sensible defaults
- **Repository Pattern**: Consistent data access layer across all entities
- **Environment-Based Configuration**: Secure API key management using environment variables
- **Automatic Cleanup**: Built-in test data cleanup to prevent test pollution
- **Package-Based Organization**: Clean separation of concerns with dedicated packages per entity

## Supported Entities

- **Boards**: Trello board management (create, read, update, delete)
- **Lists**: Trello list management with board dependencies
- **Cards**: Trello card management with list dependencies

## Quick Start

1. Set up environment variables:
   ```bash
   export TRELLO_API_KEY="your_api_key"
   export TRELLO_API_TOKEN="your_api_token"
   ```

2. Run tests:
   ```bash
   mvn test -pl test-data-management-module
   ```

## Architecture

The framework follows a clean architecture pattern:
- **Entities**: Domain models with Lombok annotations for reduced boilerplate
- **Repositories**: Data access layer handling HTTP operations
- **Factories**: Entity creation with default values and dependency injection
- **Tests**: Comprehensive lifecycle testing for each entity

## Dependencies

- BELLATRIX Framework (Core & Data modules)
- JUnit 5 for testing
- Lombok for code generation
- Gson for JSON processing

## Project Structure

```
test-data-management-module/
├── src/main/java/
│   ├── board/          # Board entity and repository
│   ├── list/           # List entity and repository
│   └── card/           # Card entity and repository
└── src/test/java/
    ├── board/          # Board lifecycle tests
    ├── list/           # List lifecycle tests
    └── card/           # Card lifecycle tests
```

## Contributing

When adding new entities:
1. Create entity class with Lombok annotations
2. Implement repository extending HttpRepository
3. Create factory with default entity creation methods
4. Add comprehensive lifecycle tests
5. Update this README with new entity information