# Project File Structure

This is a Spring Boot application following Maven's standard directory layout and Spring Boot best practices.

## Root Directory Structure

```
/workspace/
├── .git/                           # Git version control directory
├── .mvn/                           # Maven wrapper configuration
│   └── wrapper/
│       └── maven-wrapper.properties
├── src/                            # Source code directory
│   ├── main/                       # Main application source
│   │   ├── java/                   # Java source files
│   │   └── resources/              # Application resources
│   └── test/                       # Test source
│       └── java/                   # Test Java source files
├── target/                         # Maven build output (generated)
│   ├── classes/                    # Compiled main classes
│   └── test-classes/               # Compiled test classes
├── .cursorrules                    # Cursor IDE rules configuration
├── .gitattributes                  # Git attributes configuration
├── .gitignore                      # Git ignore patterns
├── README.md                       # Project documentation
├── pom.xml                         # Maven project configuration
├── mvnw                           # Maven wrapper script (Unix)
└── mvnw.cmd                       # Maven wrapper script (Windows)
```

## Source Code Structure

### Main Application (`src/main/java/com/example/gros/`)

```
src/main/java/com/example/gros/
├── GrosApplication.java            # Spring Boot main application class
├── config/                         # Configuration classes
│   ├── SecurityConfig.java        # Security configuration
│   └── SessionFilter.java         # Custom session filter
├── controller/                     # REST API controllers
│   ├── AuthController.java        # Authentication endpoints
│   ├── ProductController.java     # Product management endpoints
│   ├── UserController.java        # User management endpoints
│   └── WishlistController.java    # Wishlist management endpoints
├── dto/                           # Data Transfer Objects
│   ├── ApiResponse.java           # Generic API response wrapper
│   ├── LoginRequest.java          # Login request payload
│   ├── PasswordChangeRequest.java # Password change request payload
│   ├── ProductDTO.java            # Product data transfer object
│   ├── RegisterRequest.java       # User registration request payload
│   ├── UserDTO.java               # User data transfer object
│   ├── WishlistDTO.java           # Wishlist data transfer object
│   └── WishlistRequest.java       # Wishlist request payload
├── exception/                      # Custom exceptions and handlers
│   ├── GlobalExceptionHandler.java # Global exception handler
│   ├── ProductNotFoundException.java # Product not found exception
│   └── UserNotFoundException.java # User not found exception
├── model/                         # JPA Entity classes
│   ├── LoginTracking.java         # Login tracking entity
│   ├── Product.java               # Product entity
│   ├── User.java                  # User entity
│   └── Wishlist.java              # Wishlist entity
├── repository/                    # Data access layer (Spring Data JPA)
│   ├── LoginTrackingRepository.java # Login tracking repository
│   ├── ProductRepository.java     # Product repository
│   ├── UserRepository.java        # User repository
│   └── WishlistRepository.java    # Wishlist repository
└── service/                       # Business logic layer
    ├── AuthService.java           # Authentication service
    ├── ProductService.java        # Product business logic
    ├── UserService.java           # User business logic
    └── WishlistService.java       # Wishlist business logic
```

### Resources (`src/main/resources/`)

```
src/main/resources/
├── application.properties         # Spring Boot configuration properties
├── data.sql                      # Sample data initialization script
└── schema.sql                    # Database schema initialization script
```

### Test Structure (`src/test/java/com/example/gros/`)

```
src/test/java/com/example/gros/
└── GrosApplicationTests.java      # Main application test class
```

## Architecture Overview

This Spring Boot application follows a layered architecture pattern:

1. **Controller Layer** (`controller/`): Handles HTTP requests and responses
2. **Service Layer** (`service/`): Contains business logic
3. **Repository Layer** (`repository/`): Data access using Spring Data JPA
4. **Model Layer** (`model/`): JPA entities representing database tables
5. **DTO Layer** (`dto/`): Data transfer objects for API communication
6. **Configuration Layer** (`config/`): Spring configuration classes
7. **Exception Layer** (`exception/`): Custom exceptions and global error handling

## Key Features

- **Authentication System**: Login tracking and user management
- **Product Management**: CRUD operations for products
- **Wishlist Functionality**: User wishlist management
- **Security Configuration**: Custom security setup with session filtering
- **Exception Handling**: Global exception handling with custom exceptions
- **Database Integration**: JPA entities with repository pattern
- **RESTful APIs**: Well-structured REST endpoints

## Build System

- **Maven**: Used for dependency management and build lifecycle
- **Maven Wrapper**: Included for consistent builds across environments
- **Target Directory**: Contains compiled classes and build artifacts (generated)

This structure follows Spring Boot and Maven conventions, providing clear separation of concerns and maintainable code organization.