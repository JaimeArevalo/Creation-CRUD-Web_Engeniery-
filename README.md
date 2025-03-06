# Card Demo - Legacy System Modernization

## 🚀 Project Description
Modernization project of a legacy banking card system, transforming a COBOL application into a modern Java solution using Spring Boot. This project implements a complete CRUD system for card and transaction management with advanced features like automatic card number generation, transaction processing, and data backup functionality.

## 🛠 Technologies
- **Language**: Java 11+
- **Framework**: Spring Boot 2.7.0
- **Database**: H2 (In-memory)
- **Dependency Management**: Maven
- **Documentation**: SpringDoc OpenAPI

## 📋 Prerequisites
- JDK 21 or higher
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, NetBeans)
- Postman (for API testing)

## 🔧 Installation

### Clone Repository
```bash
git clone https://github.com/felixsuarez0727/CARD-DEMO.git
cd card-demo
```

### Build Project
```bash
mvn clean install
```

### Run Application
```bash
mvn spring-boot:run
```

## 🌐 API Endpoints

### Card Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/cards` | Create new card |
| GET | `/api/v1/cards` | List all active cards |
| GET | `/api/v1/cards/{id}` | Get card by ID |
| GET | `/api/v1/cards/number/{cardNumber}` | Get by card number |
| PUT | `/api/v1/cards/{id}` | Update card |
| PATCH | `/api/v1/cards/{id}` | Partial update |
| DELETE | `/api/v1/cards/{id}` | Soft delete card |

### Transaction Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/transactions` | Create new transaction |
| GET | `/api/v1/transactions` | List all transactions |
| GET | `/api/v1/transactions/{id}` | Get transaction by ID |
| GET | `/api/v1/transactions/card/{cardId}` | Get transactions by card |
| PUT | `/api/v1/transactions/{id}` | Update transaction |
| DELETE | `/api/v1/transactions/{id}` | Delete transaction |
| GET | `/api/v1/transactions/report` | Generate transaction report |

### Backup Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/backup/manual` | Create manual backup |
| GET | `/api/v1/backup/list` | List all backups |

### Card Operations
- **Create Card**: 
  - `POST /api/cards`
  - Creates a new card with automatic number generation or specified number
  
- **List Cards**: 
  - `GET /api/cards`
  - Returns only active cards
  - Supports pagination
  
- **Get Card by ID**: 
  - `GET /api/cards/{id}`
  - Returns card details if active
  
- **Get Card by Number**: 
  - `GET /api/cards/number/{cardNumber}`
  - Finds card by its unique number
  
- **Update Card**: 
  - `PUT /api/cards/{id}`
  - Full update of card details
  
- **Patch Card**: 
  - `PATCH /api/cards/{id}`
  - Partial update of card details
  
- **Delete Card**: 
  - `DELETE /api/cards/{id}`
  - Performs soft delete (deactivation)

### Transaction Operations
- **Create Transaction**:
  - `POST /api/transactions`
  - Process PURCHASE or PAYMENT transactions
  
- **Get Transactions**:
  - `GET /api/transactions`
  - List all transactions
  
- **Get Transaction by ID**:
  - `GET /api/transactions/{id}`
  - Get specific transaction details
  
- **Get Card Transactions**:
  - `GET /api/transactions/card/{cardId}`
  - Get all transactions for a specific card
  
- **Update Transaction**:
  - `PUT /api/transactions/{id}`
  - Update transaction details
  
- **Delete Transaction**:
  - `DELETE /api/transactions/{id}`
  - Remove transaction record
  
- **Transaction Report**:
  - `GET /api/transactions/report`
  - Generate transaction report with filters

### Backup Operations
- **Create Backup**:
  - `POST /api/backup/manual`
  - Create manual data backup
  
- **List Backups**:
  - `GET /api/backup/list`
  - View available backups

## 📊 Data Models

### Card Entity
```java
public class Card {
    private Long id;                  // Auto-generated
    private String cardNumber;        // 16 digits, unique
    private String cardHolder;        // 2-50 characters, letters only
    private String expirationDate;    // MM/YY format
    private String cvv;               // 3 digits
    private Double creditLimit;       // Positive value
    private Double balance;           // Zero or positive
    private String status;            // ACTIVE, BLOCKED, or EXPIRED
    private Boolean isActive;         // For soft delete
    private LocalDateTime createdAt;  // Auto-generated
    private LocalDateTime updatedAt;  // Auto-updated
}
```

## 🏗 Project Structure
```
CARD-DEMO/
├── backups/                          # Backup storage
├── logs/                            # Application logs
├── postman/                         # API testing
│   └── CardDemo-Collection.json
├── src/
│   ├── main/
│   │   ├── java/com/carddemo/
│   │   │   ├── config/              # Configuration classes
│   │   │   │   └── BackupConfig.java
│   │   │   ├── controller/          # REST endpoints
│   │   │   │   ├── BackupController.java
│   │   │   │   ├── CardController.java
│   │   │   │   └── TransactionController.java
│   │   │   ├── exception/           # Error handling
│   │   │   │   ├── BackupException.java
│   │   │   │   ├── CardNotFoundException.java
│   │   │   │   ├── DuplicateCardException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── InvalidCardDataException.java
│   │   │   │   └── TransactionNotFoundException.java
│   │   │   ├── model/              # Data models
│   │   │   │   ├── dto/
│   │   │   │   │   ├── BackupResponse.java
│   │   │   │   │   ├── CardRequest.java
│   │   │   │   │   ├── CardResponse.java
│   │   │   │   │   ├── TransactionRequest.java
│   │   │   │   │   └── TransactionResponse.java
│   │   │   │   ├── Card.java
│   │   │   │   └── Transaction.java
│   │   │   ├── repository/         # Data access
│   │   │   │   ├── CardRepository.java
│   │   │   │   └── TransactionRepository.java
│   │   │   ├── service/           # Business logic
│   │   │   │   ├── BackupService.java
│   │   │   │   ├── CardService.java
│   │   │   │   ├── TransactionService.java
│   │   │   │   └── ValidationService.java
│   │   │   ├── validation/        # Data validation
│   │   │   │   ├── CardValidator.java
│   │   │   │   └── TransactionValidator.java
│   │   │   └── CardDemoApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── target/
├── pom.xml
└── README.md
```

## 📁 Directory Description
### Main Components
- backups/                            # Storage directory for system backups
- logs/                              # Application logs directory
- postman/                           # Contains Postman collection for API testing
- src/main/java/com/carddemo/
  - config/                          # Configuration classes for system components
  - controller/                      # REST API endpoints (Cards, Transactions, Backup)
  - exception/                       # Custom exception handlers and definitions
  - model/                          # Domain models and DTOs
  - repository/                     # Data access layer interfaces
  - service/                        # Business logic implementation
  - validation/                     # Data validation rules
- src/test/                         # Test cases and configurations
- pom.xml                          # Maven dependencies and build configuration

### Key Files
### Controllers:
- CardController.java              # Card management endpoints
- TransactionController.java       # Transaction processing endpoints
- BackupController.java           # Backup system endpoints

### Models:
- Card.java                       # Card entity model
- Transaction.java               # Transaction entity model
- dto/*                         # Data Transfer Objects

### Services:
- CardService.java              # Card business logic
- TransactionService.java       # Transaction processing logic
- BackupService.java           # Backup system operations
- ValidationService.java       # Data validation services

### Repositories:
- CardRepository.java          # Card data operations
- TransactionRepository.java   # Transaction data operations

### Validation:
- CardValidator.java          # Card data validation rules
- TransactionValidator.java   # Transaction validation rules

### Exception Handling:
- GlobalExceptionHandler.java  # Centralized error handling
- CardNotFoundException.java   # Card-specific exceptions
- TransactionNotFoundException.java # Transaction-specific exceptions
- BackupException.java        # Backup system exceptions

### Configuration:
- application.properties      # Application settings
- BackupConfig.java          # Backup system configuration

### Additional Components
`dto/`:
- `CardRequest.java`
- `CardResponse.java`
- `TransactionRequest.java`
- `TransactionResponse.java`
- `BackupResponse.java`

## This structure implements a clean architecture pattern with:

- Clear separation of concerns
- Independent layers (controller, service, repository)
- Dedicated validation components
- Comprehensive error handling
- Modular backup system
- Transaction processing capabilities

## 📦 Main Dependencies
```xml 
<dependencies>
    <!-- Spring Boot Starters -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <!-- Database -->
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Utilities -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.datatype</groupId>
        <artifactId>jackson-datatype-jsr310</artifactId>
    </dependency>

    <!-- Testing -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## 💾 Database Configuration
# Database Configuration

```properties
spring.datasource.url=jdbc:h2:mem:carddb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Backup Configuration
backup.directory=backups
backup.retention-days=7
backup.schedule.cron=0 0 0 * * *

# Logging
logging.file.name=logs/card-demo.log
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

## 🔐 Validation Rules

### Card Validation
- Card Number: Must be exactly 16 digits
- Card Holder: 2-50 characters, letters and spaces only
- Expiration Date: MM/YY format
- CVV: Exactly 3 digits
- Credit Limit: Must be positive
- Balance: Must be zero or positive
- Status: Must be ACTIVE, BLOCKED, or EXPIRED

### Transaction Validation
- Amount: Must be positive
- Type: Must be PURCHASE or PAYMENT
- Description: Required, 3-100 characters
- Card must be active
- Purchase amount cannot exceed available credit
- Payment amount cannot exceed current balance

## 🎯 Key Features
- Automatic card number generation
- Transaction processing
- Data backup system
- Soft delete implementation
- Field validations
- Error handling
- Transaction reporting
- Active/Inactive card filtering

## 🧪 Testing
1. Import the provided Postman collection
2. Execute requests in sequence to test all features
3. Verify validation rules and error handling

## 🔧 Troubleshooting
- **Port in Use**: Change server.port in application.properties
- **H2 Console Access**: Ensure correct JDBC URL
- **Validation Errors**: Check request payload format
- **404 Errors**: Verify card exists and is active

## 📝 Notes
- Data is reset on application restart (in-memory database)
- Backups are stored in the backups/ directory
- Logs are maintained in the logs/ directory
- Automatic timestamps for all operations

## 🤝 Contributing
1. Fork repository
2. Create feature branch
3. Commit changes
4. Push to branch
5. Create Pull Request

