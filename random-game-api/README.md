# Memory Games API

Spring Boot REST API for the number-memory and name-memory games.

The code uses a small production-style layered structure. Each layer has one
clear job, which makes the application easier to understand, test, and change.

## Run

```bash
mvn spring-boot:run
```

The API runs at `http://localhost:8080`.

## Endpoints

### Random Number Game

- `POST /api/number-game/round` with `{ "digitCount": 1 }` starts a round.
- `POST /api/number-game/guess` with `{ "roundId": "...", "guess": "7" }` checks a guess.

### Random Name Game

- `POST /api/name-game/round` starts a name round:

  ```json
  { "nameCount": 2, "nameMode": "FULL" }
  ```

  `nameMode` can be `FIRST`, `LAST`, or `FULL`.

- `POST /api/name-game/guess` checks names in the displayed order:

  ```json
  {
    "roundId": "...",
    "guesses": ["Ava Patel", "Noah Kim"]
  }
  ```

The name catalogs are stored in `src/main/resources/names/`. They currently
contain 238 unique first-name entries and 207 unique last-name entries. A round
never repeats a name within its generated list.

Open `http://localhost:8080` for Swagger UI and interactive endpoint testing.

## Project structure

```text
src/main/java/com/manish/randomgengames/
├── RandomGenGamesApiApplication.java  Spring Boot starting point
├── config/                            Swagger, CORS, and web configuration
├── controller/                        HTTP endpoints
├── service/                           Game rules and use cases
├── dao/                               Round storage and name-file access
├── dto/                               API request and response objects
│   ├── number/                        Number-game JSON models
│   └── name/                          Name-game JSON models
├── model/                             Internal game objects
└── exception/                         API errors and HTTP error handling
```

### `controller`

A controller is the API's front door. It defines a URL, reads the incoming JSON
into a request DTO, calls a service, and returns a response DTO as JSON.

For example, `NumberGameController` maps `POST /api/number-game/round` and delegates to
`NumberGameService`. It deliberately contains no number-generation or scoring
logic. This keeps HTTP concerns separate from game rules.

### `service`

A service contains the application's business logic. The two services validate
requests, create random challenges, check guesses, and calculate the next
difficulty. These classes do not know how HTTP responses are rendered and do
not directly manage maps or files.

This is also why the focused unit tests live under `src/test/.../service/`.
They can test the game behavior without starting a web server.

### `dao`

DAO means **Data Access Object**. A DAO hides where data comes from or where it
is stored:

- `NumberRoundDao` stores active number rounds.
- `NameRoundDao` stores active name rounds.
- `NameCatalogDao` reads first and last names from the resource files.

`NumberRoundDao` and `NameRoundDao` share their thread-safe map and ID behavior
through `InMemoryRoundDao`. Active games are lost whenever the API restarts. In
a larger application, the DAOs could be replaced by database-backed
implementations while controllers and most service logic remain unchanged.

### `dto`

DTO means **Data Transfer Object**. These Java records describe the JSON that
crosses the API boundary. Request DTOs represent data sent by React; response
DTOs represent data returned to React. Number-game and name-game DTOs have
separate subpackages so the files remain easy to find as the API grows.

### `model`

Models represent data used inside the application. `NumberRound` and
`NameRound` are saved by the DAOs while a player is remembering a challenge.
`NameMode` represents the supported `FIRST`, `LAST`, and `FULL` choices.

Models and DTOs are intentionally separate. A future database or internal model
change therefore does not have to change the public JSON contract.

### `exception`

Services throw meaningful application exceptions for invalid input or an
unknown/completed round. `GameExceptionHandler` converts them into consistent
HTTP `400 Bad Request` or `404 Not Found` responses.

### `config`

- `OpenApiConfig` supplies the Swagger/OpenAPI title and description.
- `WebConfig` allows the local React development server to call `/api/**` and
  redirects `/` to Swagger UI.

Keeping configuration here leaves `RandomGenGamesApiApplication` with only one
responsibility: starting Spring Boot. The name represents the complete API,
which now supports both random-number and random-name games.

## What happens during a request?

Starting a number round follows this path:

```text
React
  -> NumberGameController
  -> NumberGameService
  -> NumberRoundDao
  -> NumberGameService creates the response DTO
  -> controller returns JSON to React
```

Spring creates these objects and supplies their constructor dependencies. This
is called **dependency injection**. For example, Spring passes a
`NumberGameService` to `NumberGameController`, and a `NumberRoundDao` to that
service. Constructor injection makes dependencies visible and makes classes
easy to test.

## Run tests

```bash
mvn test
```

The service tests check both game rules and name-catalog integrity. The Spring
Boot integration test starts the application context and verifies the Swagger
redirect and all documented endpoint paths.
