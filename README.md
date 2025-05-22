# Task_Sii

Fundraising Box Manager
A Spring Boot application for managing fundraising events and collection boxes. Includes support for multi-currency donation tracking and real-time exchange rate conversion via
https://exchangerate.host.

## Build and run

### Requirements

- Java 17+
- Maven
- Internet connection required for fetching currency rates
- Free API key from exchange

### Start

###  Steps

```bash
# 1. Clone the repository
git clone https://github.com/AdrianSzponarOnline/Task_Sii

# 2. Rename config file
mv src/main/resources/application-example.properties src/main/resources/application.properties

# 3. Set your exchange API key
# (paste it into the application.properties under `exchange.api.key=`)

# 4. Build and run the app
./mvnw clean install
./mvnw spring-boot:run
```
## Access the application

| Feature      | URL                                |
|--------------|------------------------------------|
| API Base URL | `http://localhost:8080`            |
| H2 Console   | `http://localhost:8080/h2-console` |
| JDBC URL     | `jdbc:h2:mem:fundraising-db`       |
| Username     | `sa`                               |
| Password     | *(leave blank)*                    |

## CollectionBox API

| Method   | Endpoint                    | Description                                       | Example request/payload                 |
|----------|-----------------------------|---------------------------------------------------|-----------------------------------------|
| `POST`   | `/api/boxes`                | Create new empty box                              | –                                       |
| `GET`    | `/api/boxes`                | Get a list of all boxes                           | –                                       |
| `DELETE` | `/api/boxes/{id}`           | Delete box with id                                | –                                       |
| `PUT`    | `/api/boxes/{id}/assign`    | Assign box to a fundraising event                 | `{ "eventId": 1 }`                      |
| `POST`   | `/api/boxes/{id}/add-money` | Adding money into the box                         | `{ "currency": "EUR", "amount": 50.0 }` |
| `POST`   | `/api/boxes/{id}/transfer`  | Transfer money from the box to the assigned event | –                                       |

## FundraisingEvent API

| Method | Endpoint             | Description                         | Example request/payload                         |
|--| -------------------- |-------------------------------------|-------------------------------------------------|
| `POST` | `/api/events`        | Create new fundraiser               | `{ "eventName": "Charity", "currency": "PLN" }` ||
| `GET` | `/api/events/report` | Get financial raport of fundraisers | –                                               |

## REST API sample responses

### GET /api/boxes
```json
[
  {
    "id": 1,
    "empty": false,
    "assigned": true
  },
  {
    "id": 2,
    "empty": true,
    "assigned": false
  }
]
``` 

### GET /api/events/report
```json
[
  {
    "eventName": "Charity One",
    "amount": 2048.00,
    "currency": "EUR"
  },
  {
    "eventName": "Charity Two",
    "amount": 512.64,
    "currency": "GBP"
  }
]

```
