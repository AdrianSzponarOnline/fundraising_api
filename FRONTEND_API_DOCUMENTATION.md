# TaskSii Frontend API Documentation

## Overview
This is a Spring Boot application for managing fundraising events, collection boxes, volunteers, and donations. The API supports role-based authentication with different user types: USER, OWNER, VOLUNTEER, and ADMIN.

## Base URL
```
http://localhost:8080/api
```

## Authentication
The API uses JWT (JSON Web Token) authentication. Include the token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## User Roles
- **USER**: Can add money to collection boxes
- **OWNER**: Can create events, manage volunteers, manage collection boxes, view reports
- **VOLUNTEER**: Assigned to collection boxes by owners
- **ADMIN**: Full system access

---

## API Endpoints

### 1. Authentication Controller (`/api/auth`)

#### POST `/api/auth/login`
**Description**: Authenticate user and get JWT token
**Request Body**:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```
**Response**:
```json
{
  "token": "jwt-token-string",
  "email": "user@example.com",
  "roles": ["ROLE_USER"]
}
```

#### GET `/api/auth/me`
**Description**: Get current authenticated user info
**Headers**: `Authorization: Bearer <token>`
**Response**:
```json
{
  "email": "user@example.com"
}
```

#### POST `/api/auth/register`
**Description**: Register a new regular user
**Request Body**:
```json
{
  "email": "user@example.com",
  "password": "Password123"
}
```
**Response**:
```json
{
  "email": "user@example.com"
}
```

#### POST `/api/auth/register/owner`
**Description**: Register a new organization owner
**Request Body**:
```json
{
  "email": "owner@example.com",
  "password": "Password123",
  "organizationName": "Charity Organization",
  "nip": "1234567890",
  "regon": "123456789",
  "krs": "1234567890",
  "phoneNumber": "+48123456789",
  "addresses": [
    {
      "streetName": "Main Street 1",
      "city": "Warsaw",
      "state": "Mazovia",
      "country": "Poland",
      "postalCode": "00-001"
    }
  ]
}
```
**Response**:
```json
{
  "id": 1,
  "email": "owner@example.com",
  "roles": ["ROLE_OWNER"],
  "organizationName": "Charity Organization",
  "nip": "1234567890",
  "regon": "123456789",
  "krs": "1234567890",
  "phoneNumber": "+48123456789",
  "addresses": [...]
}
```

---

### 2. Fundraising Event Controller (`/api/events`)

#### POST `/api/events`
**Description**: Create a new fundraising event
**Authorization**: OWNER role required
**Request Body**:
```json
{
  "eventName": "Help for Children",
  "currency": "PLN"
}
```
**Response**:
```json
{
  "id": 1,
  "name": "Help for Children",
  "currency": "PLN",
  "accountBalance": 0.00
}
```

#### GET `/api/events/report`
**Description**: Get financial report for all events owned by current user
**Authorization**: OWNER role required
**Response**:
```json
[
  {
    "eventName": "Help for Children",
    "amount": 1500.50,
    "currency": "PLN"
  }
]
```

---

### 3. Collection Box Controller (`/api/boxes`)

#### POST `/api/boxes`
**Description**: Register a new collection box for an event
**Authorization**: OWNER role required
**Request Body**:
```json
{
  "eventId": 1
}
```
**Response**:
```json
{
  "id": 1,
  "empty": true,
  "assigned": false,
  "created": "2024-01-15T10:30:00",
  "collectedAt": null,
  "eventId": 1,
  "eventName": "Help for Children",
  "volunteerId": null,
  "volunteerName": null
}
```

#### GET `/api/boxes`
**Description**: Get all collection boxes for current owner
**Authorization**: OWNER role required
**Response**:
```json
[
  {
    "id": 1,
    "empty": false,
    "assigned": true,
    "created": "2024-01-15T10:30:00",
    "collectedAt": null,
    "eventId": 1,
    "eventName": "Help for Children",
    "volunteerId": 2,
    "volunteerName": "John Doe"
  }
]
```

#### DELETE `/api/boxes?id={boxId}`
**Description**: Delete a collection box
**Authorization**: OWNER role required
**Response**: 204 No Content

#### PUT `/api/boxes/assign`
**Description**: Assign a collection box to an event
**Authorization**: OWNER role required
**Request Body**:
```json
{
  "boxId": 1,
  "eventId": 2
}
```
**Response**: 200 OK

#### POST `/api/boxes/add-money`
**Description**: Add money to a collection box
**Authorization**: USER role required
**Request Body**:
```json
{
  "boxId": 1,
  "currency": "PLN",
  "amount": 50.00
}
```
**Response**: 200 OK

#### POST `/api/boxes/transfer`
**Description**: Transfer money from collection box to event account
**Authorization**: OWNER role required
**Request Body**:
```json
{
  "boxId": 1
}
```
**Response**: 200 OK

---

### 4. Volunteer Controller (`/api/volunteers`)

#### GET `/api/volunteers`
**Description**: Get all volunteers for current owner
**Authorization**: OWNER role required
**Response**:
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "phoneNumber": "+48123456789",
    "ownerProfileId": 1,
    "collectionBoxes": [...]
  }
]
```

#### GET `/api/volunteers/{id}`
**Description**: Get volunteer by ID
**Authorization**: OWNER role required
**Response**:
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phoneNumber": "+48123456789",
  "ownerProfileId": 1,
  "collectionBoxes": [...]
}
```

#### POST `/api/volunteers`
**Description**: Create a new volunteer
**Authorization**: OWNER role required
**Request Body**:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phoneNumber": "+48123456789",
  "password": "password123",
  "ownerProfileId": 1
}
```
**Response**:
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phoneNumber": "+48123456789",
  "ownerProfileId": 1,
  "collectionBoxes": []
}
```

#### PUT `/api/volunteers/{id}`
**Description**: Update volunteer information
**Authorization**: OWNER role required
**Request Body**:
```json
{
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@example.com",
  "phoneNumber": "+48123456789",
  "password": "newpassword123"
}
```
**Response**:
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@example.com",
  "phoneNumber": "+48123456789",
  "ownerProfileId": 1,
  "collectionBoxes": [...]
}
```

#### DELETE `/api/volunteers/{id}`
**Description**: Delete a volunteer
**Authorization**: OWNER role required
**Response**: 204 No Content

#### POST `/api/volunteers/assign`
**Description**: Assign volunteer to a collection box
**Authorization**: OWNER role required
**Request Body**:
```json
{
  "volunteerId": 1,
  "boxId": 1
}
```
**Response**: 200 OK

---

## Data Models

### Enums

#### Currency
```typescript
enum Currency {
  EUR = "EUR",
  GBP = "GBP", 
  PLN = "PLN",
  USD = "USD"
}
```

#### User Roles
```typescript
enum ERole {
  ROLE_USER = "ROLE_USER",
  ROLE_ADMIN = "ROLE_ADMIN",
  ROLE_VOLUNTEER = "ROLE_VOLUNTEER",
  ROLE_OWNER = "ROLE_OWNER"
}
```

### DTOs

#### AddressDTO
```typescript
interface AddressDTO {
  id?: number;
  streetName: string;        // max 100 chars, required
  city: string;             // max 50 chars, required
  state?: string;           // max 50 chars, optional
  country: string;          // max 50 chars, required
  postalCode: string;       // max 20 chars, required
}
```

#### AuthRequestDTO
```typescript
interface AuthRequestDTO {
  email: string;            // valid email, max 255 chars, required
  password: string;         // 6-100 chars, required
}
```

#### AuthResponseDTO
```typescript
interface AuthResponseDTO {
  token: string;
  email: string;
  roles: string[];
}
```

#### UserDto
```typescript
interface UserDto {
  email: string;            // valid email, max 255 chars, required
}
```

#### RegisterRequestDTO
```typescript
interface RegisterRequestDTO {
  email: string;            // valid email, required
  password: string;         // min 6 chars, must contain uppercase, lowercase, and number
}
```

#### RegisterOwnerDTO
```typescript
interface RegisterOwnerDTO {
  email: string;                    // valid email, max 255 chars, required
  password: string;                 // 6-100 chars, required
  organizationName: string;         // max 100 chars, required
  nip?: string;                     // exactly 10 digits
  regon?: string;                   // exactly 9 digits
  krs?: string;                     // exactly 10 digits
  phoneNumber?: string;             // 9-15 digits with optional +
  addresses: AddressDTO[];          // required
}
```

#### OwnerDTO
```typescript
interface OwnerDTO {
  id: number;
  email: string;
  roles: Set<string>;
  organizationName: string;         // max 100 chars
  nip?: string;                     // max 10 chars
  regon?: string;                   // max 14 chars
  krs?: string;                     // max 10 chars
  phoneNumber?: string;             // max 15 chars
  addresses: AddressDTO[];
}
```

#### CreateFundraisingEventDTO
```typescript
interface CreateFundraisingEventDTO {
  eventName: string;                // max 255 chars, required
  currency: Currency;               // required
}
```

#### FundraisingEventDTO
```typescript
interface FundraisingEventDTO {
  id: number;
  name: string;                     // required
  currency: Currency;               // required
  accountBalance: number;           // required
}
```

#### FinancialReportDTO
```typescript
interface FinancialReportDTO {
  eventName: string;
  amount: number;
  currency: Currency;
}
```

#### VolunteerCreateDTO
```typescript
interface VolunteerCreateDTO {
  firstName: string;                // max 50 chars, required
  lastName: string;                 // max 50 chars, required
  email: string;                    // valid email, max 255 chars, required
  phoneNumber?: string;             // 9-15 digits with optional +
  password: string;                 // 6-100 chars, required
  ownerProfileId: number;           // positive number, required
}
```

#### VolunteerResponseDTO
```typescript
interface VolunteerResponseDTO {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  ownerProfileId: number;
  collectionBoxes: CollectionBoxDTO[];
}
```

#### VolunteerUpdateDTO
```typescript
interface VolunteerUpdateDTO {
  firstName?: string;               // max 50 chars
  lastName?: string;                // max 50 chars
  email?: string;                   // valid email, max 255 chars
  phoneNumber?: string;             // 9-15 digits with optional +
  password?: string;                // 6-100 chars
}
```

#### CollectionBoxDTO
```typescript
interface CollectionBoxDTO {
  id: number;
  empty: boolean;
  assigned: boolean;
  created: string;                  // ISO 8601 datetime
  collectedAt?: string;             // ISO 8601 datetime
  eventId: number;
  eventName: string;
  volunteerId?: number;
  volunteerName?: string;
}
```

#### CreateBoxRequestDTO
```typescript
interface CreateBoxRequestDTO {
  eventId: number;
}
```

#### AddMoneyRequestDTO
```typescript
interface AddMoneyRequestDTO {
  boxId: number;                    // positive number, required
  currency: Currency;               // required
  amount: number;                   // min 0.01, required
}
```

#### AssignBoxRequestDTO
```typescript
interface AssignBoxRequestDTO {
  boxId: number;                    // positive number, required
  eventId: number;                  // positive number, required
}
```

#### AssignVolunteerRequestDTO
```typescript
interface AssignVolunteerRequestDTO {
  volunteerId: number;              // positive number, required
  boxId: number;                    // positive number, required
}
```

#### TransferRequestDTO
```typescript
interface TransferRequestDTO {
  boxId: number;                    // positive number, required
}
```

---

## Error Handling

The API returns standard HTTP status codes:
- **200 OK**: Successful request
- **201 Created**: Resource created successfully
- **204 No Content**: Successful request with no response body
- **400 Bad Request**: Invalid request data
- **401 Unauthorized**: Authentication required
- **403 Forbidden**: Insufficient permissions
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

Error responses typically include validation messages for invalid data.

---

## Frontend Implementation Notes

### Authentication Flow
1. User logs in via `/api/auth/login` to get JWT token
2. Store token in localStorage or secure storage
3. Include token in Authorization header for all authenticated requests
4. Use `/api/auth/me` to get current user info and roles

### Role-Based UI
- Show different UI components based on user roles
- Hide/disable features that require specific roles
- Implement proper route guards for role-based access

### State Management
- Consider using Redux, Zustand, or Context API for:
  - User authentication state
  - Current user role
  - Events, volunteers, and collection boxes data
  - Loading and error states

### Form Validation
- Implement client-side validation matching server-side constraints
- Show real-time validation feedback
- Handle server validation errors gracefully

### Data Fetching
- Use React Query, SWR, or similar for:
  - Caching API responses
  - Background refetching
  - Optimistic updates
  - Error handling and retries

### UI Components to Build
- Login/Register forms
- Dashboard for different user types
- Event management (create, view, reports)
- Volunteer management (CRUD operations)
- Collection box management
- Money transfer interface
- Financial reports and charts
