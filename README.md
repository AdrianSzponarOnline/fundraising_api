# Fundraising Box Manager

A comprehensive Spring Boot application for managing fundraising events, collection boxes, and volunteer coordination. Features multi-currency donation tracking with real-time exchange rate conversion, role-based authentication, and complete volunteer management system.

## Features

- **Multi-User System** with role-based access control (USER, OWNER, VOLUNTEER, ADMIN)
- **JWT Authentication** with secure token-based sessions
- **Fundraising Event Management** with multi-currency support
- **Collection Box System** with volunteer assignment and money tracking
- **Real-time Exchange Rate Conversion** via external API integration
- **Volunteer Management** with owner-specific coordination
- **Financial Reporting** with detailed analytics
- **RESTful API** with comprehensive validation
- **Database Support** for both H2 (development) and PostgreSQL (production)

## Technology Stack

- **Java 17+**
- **Spring Boot 3.4.5**
- **Spring Security** with JWT
- **Spring Data JPA**
- **MapStruct** for DTO mapping
- **Lombok** for boilerplate reduction
- **H2 Database** (development) / **PostgreSQL** (production)
- **Maven** build system
- **SpringDoc OpenAPI** for API documentation

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Internet connection (for exchange rate API)
- Free API key from [exchangerate.host](https://exchangerate.host) or [apilayer.net](https://apilayer.net)

## Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/AdrianSzponarOnline/Task_Sii
cd Task_Sii
```

### 2. Configure Application
```bash
# Copy example configuration
cp src/main/resources/application-example.properties src/main/resources/application.properties

# Edit application.properties and add your exchange API key
# exchange.api.key=YOUR_API_KEY_HERE
```

### 3. Build and Run
```bash
# Build the application
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

## Access Points

| Feature | URL | Description |
|---------|-----|-------------|
| **API Base** | `http://localhost:8080` | Main API endpoint |
| **H2 Console** | `http://localhost:8080/h2-console` | Database management (dev only) |
| **API Documentation** | `http://localhost:8080/swagger-ui.html` | Interactive API docs |
| **Database URL** | `jdbc:h2:mem:fundraising-db` | H2 connection string |
| **Username** | `sa` | Database username |
| **Password** | *(empty)* | Database password |

## Authentication

The application uses JWT-based authentication with role-based access control.

### User Roles
- **USER** - Can donate money to collection boxes
- **OWNER** - Can create events, manage boxes, and coordinate volunteers
- **VOLUNTEER** - Can be assigned to collection boxes
- **ADMIN** - Full system access

### Authentication Endpoints
| Method | Endpoint | Description | Access |
|--------|----------|-------------|---------|
| `POST` | `/api/auth/login` | User login | Public |
| `POST` | `/api/auth/register` | Register regular user | Public |
| `POST` | `/api/auth/register/owner` | Register organization owner | Public |
| `GET` | `/api/auth/me` | Get current user info | Authenticated |

## API Documentation

### Authentication API

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "user@example.com",
  "roles": ["ROLE_USER"]
}
```

#### Register User
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "Password123"
}
```

#### Register Owner
```http
POST /api/auth/register/owner
Content-Type: application/json

{
  "email": "owner@organization.com",
  "password": "Password123",
  "organizationName": "Charity Organization",
  "nip": "1234567890",
  "regon": "123456789",
  "krs": "1234567890",
  "phoneNumber": "+48123456789",
  "addresses": [
    {
      "street": "Main Street 1",
      "city": "Warsaw",
      "postalCode": "00-001",
      "country": "Poland"
    }
  ]
}
```

### Collection Box API

| Method | Endpoint | Description | Required Role | Request Body |
|--------|----------|-------------|---------------|--------------|
| `POST` | `/api/boxes` | Create new collection box | OWNER | - |
| `GET` | `/api/boxes` | Get all collection boxes | Any | - |
| `DELETE` | `/api/boxes?id={id}` | Delete collection box | OWNER | - |
| `PUT` | `/api/boxes/assign` | Assign box to event | OWNER | `{"boxId": 1, "eventId": 1}` |
| `POST` | `/api/boxes/add-money` | Add money to box | USER | `{"boxId": 1, "currency": "EUR", "amount": 50.0}` |
| `POST` | `/api/boxes/transfer` | Transfer money to event | OWNER | `{"boxId": 1}` |

#### Add Money to Collection Box
```http
POST /api/boxes/add-money
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "boxId": 1,
  "currency": "EUR",
  "amount": 50.0
}
```

#### Assign Box to Event
```http
PUT /api/boxes/assign
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "boxId": 1,
  "eventId": 1
}
```

### Fundraising Event API

| Method | Endpoint | Description | Required Role | Request Body |
|--------|----------|-------------|---------------|--------------|
| `POST` | `/api/events` | Create fundraising event | Any | `{"eventName": "Charity Event", "currency": "PLN"}` |
| `GET` | `/api/events/report` | Get financial report | Any | - |

#### Create Fundraising Event
```http
POST /api/events
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "eventName": "Charity Event 2024",
  "currency": "PLN"
}
```

#### Get Financial Report
```http
GET /api/events/report
Authorization: Bearer YOUR_JWT_TOKEN
```

**Response:**
```json
[
  {
    "eventName": "Charity Event 2024",
    "amount": 2048.00,
    "currency": "EUR"
  },
  {
    "eventName": "Local Fundraiser",
    "amount": 512.64,
    "currency": "GBP"
  }
]
```

### Volunteer Management API

| Method | Endpoint | Description | Required Role | Request Body |
|--------|----------|-------------|---------------|--------------|
| `GET` | `/api/volunteers?ownerId={id}` | List volunteers by owner | OWNER | - |
| `GET` | `/api/volunteers/{id}` | Get volunteer details | OWNER | - |
| `POST` | `/api/volunteers` | Create new volunteer | OWNER | See below |
| `PUT` | `/api/volunteers/{id}` | Update volunteer | OWNER | See below |
| `DELETE` | `/api/volunteers/{id}?ownerId={id}` | Delete volunteer | OWNER | - |

#### Create Volunteer
```http
POST /api/volunteers
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "+48123456789",
  "password": "Password123",
  "ownerProfileId": 1
}
```

#### Update Volunteer
```http
PUT /api/volunteers/1
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Smith",
  "phoneNumber": "+48987654321"
}
```

## Supported Currencies

The application supports multiple currencies with real-time exchange rate conversion:

- **USD** (US Dollar) - Base currency
- **EUR** (Euro)
- **GBP** (British Pound)
- **PLN** (Polish Złoty)

## Configuration

### Environment Variables (Production)
```bash
# Database Configuration
DATABASE_HOST=localhost
DATABASE_PORT=5432
DATABASE_NAME=fundraising_db
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your_password

# JWT Configuration
SECURITY_JWT_SECRET_KEY=your_secret_key
SECURITY_JWT_EXPIRATION_TIME=3600000

# Exchange Rate API
EXCHANGE_API_URL=http://apilayer.net/api/live
EXCHANGE_API_KEY=your_api_key
```

### Development Configuration
For development, the application uses H2 in-memory database with the following default settings:
- Database URL: `jdbc:h2:mem:fundraising-db`
- Username: `sa`
- Password: *(empty)*
- H2 Console: `http://localhost:8080/h2-console`

## Testing

The application includes comprehensive test coverage:

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=VolunteerServiceTest

# Run tests with coverage
./mvnw test jacoco:report
```

### Test Structure
- **Unit Tests** - Service layer, mappers, and utilities
- **Integration Tests** - Controller endpoints and database operations
- **Security Tests** - Authentication and authorization
- **Repository Tests** - Data access layer

## Project Structure

```
src/
├── main/
│   ├── java/com/TaskSii/
│   │   ├── config/          # Security and application configuration
│   │   ├── controller/      # REST API controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── exception/      # Custom exception handling
│   │   ├── mapper/         # MapStruct mappers
│   │   ├── model/          # JPA entities
│   │   ├── repository/     # Data access layer
│   │   ├── service/        # Business logic layer
│   └── resources/
│       ├── application.properties
│       └── application-example.properties
└── test/
    └── java/com/TaskSii/
        ├── controller/     # Controller tests
        ├── service/        # Service tests
        ├── mapper/         # Mapper tests
        └── repository/     # Repository tests
```

## Security Features

- **JWT Token Authentication** with configurable expiration
- **Role-based Access Control** with method-level security
- **Password Validation** with complexity requirements
- **Input Validation** with comprehensive error messages
- **CORS Configuration** for frontend integration
- **SQL Injection Protection** via JPA/Hibernate
- **XSS Protection** with input sanitization

## Deployment

### Docker Deployment
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/TaskSii-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```
