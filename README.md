# TopScoreRanking

## Environment

1. Java 11.0.2
1. Gradle 6.8.2
1. Spring 2.4.3
1. JUnit 5 
1. MySQL 8.0.23

## Preparation

1. Install MySQL in your local machine
1. Create socre DB
    1. `CREATE DATABASE score;`
1. Describe password for mysql on application.properties
    1. `spring.datasource.password=$password`

## Build

Maybe you use IDE for your development.  
In that case, only run OyoTestApplication.

```
note: Score data in data.sql are inserted in score table initially.
These data are used by integration test.
```

## Unit And Integration Tests

Test files of unit and integration test are in test directory.
- Integration test: IntegrationTests.java in integration directory.
- Unit tests: all except for IntegrationTests.java.

## API Specifications

### CreateScoreAPI

- Path: /v1/scores
- Method: POST
- Requests
  - player: string
  - score: integer
  - time: string(ISO 8601)
- Responses
  - id: string

```
http://localhost:8080/v1/scores/

Requests
{
    "player(required)": "Gohan",
    "score(required)": 53335,
    "time(required)": "1989-04-01T01:00:00+09:00"
}

Responses
{
    "id": 10
}
```

### GetScoreAPI

- Path: /v1/scores/{id}
- Method: GET
- Requests
  - id(required): string
- Responses
  - id: string
  - player: string
  - score: integer
  - time: string(ISO 8601)

```
http://localhost:8080/v1/scores/1

Responses
{
    "id": 1,
    "player": "Goku",
    "score": 20,
    "time": "1984-11-01T00:00:00+09:00"
}
```

### DeleteScoreAPI

- Path: /v1/scores/{id}
- Method: DELETE
- Requests
  - id(required): string
- Responses
  - id: string
  - deleted_date: string(ISO 8601)

```
http://localhost:8080/v1/scores/1

Responses
{
    "id": 1,
    "deleted_date": "2021-03-15T00:29:58+09:00"
}
```

### GetListScoreAPI

- Path: /v1/scores/list
- Method: GET
- Requests
  - player: string
  - before: string(yyyy-MM-dd HH:mm:ss)
  - after: string(yyyy-MM-dd HH:mm:ss)
  - offset: integer(default 0)
    - support pagination
    - page size is 5
- Responses(Array of Object)
  - id: string
  - player: string
  - score: integer
  - time: string(ISO 8601)

```
http://localhost:8080/v1/scores/list?player=Goku&player=Gohan&before=2021-01-01 23:59:59&after=2020-01-01 00:00:00

Responses
[
    {
        "id": 4,
        "player": "Goku",
        "score": 30,
        "time": "2020-10-01T00:00:00+09:00"
    },
    {
        "id": 5,
        "player": "Goku",
        "score": 40,
        "time": "2020-01-01T00:00:00+09:00"
    },
    {
        "id": 6,
        "player": "Goku",
        "score": 50,
        "time": "2021-01-01T00:00:00+09:00"
    },
    {
        "id": 7,
        "player": "Gohan",
        "score": 20,
        "time": "2020-01-01T23:59:59+09:00"
    },
    {
        "id": 8,
        "player": "Gohan",
        "score": 20,
        "time": "2021-01-01T23:59:59+09:00"
    }
]
```
### GetPlayerHistoryAPI

- Path: /v1/scores/history
- Method: GET
- Requests
  - player(required): string
- Responses
  - name: string
  - scores: Array of Object
    - score: integer
    - time: string(ISO 8601)
  - top_score: integer
  - low_score: integer
  - average_score: integer

```
http://localhost:8080/v1/scores/history?player=Goku

Responses
{
    "name": "Goku",
    "scores": [
        {
            "score": 20,
            "time": "1984-11-01T00:00:00+09:00"
        },
        {
            "score": 30,
            "time": "2020-10-01T00:00:00+09:00"
        },
        {
            "score": 40,
            "time": "2020-01-01T00:00:00+09:00"
        },
        {
            "score": 50,
            "time": "2021-01-01T00:00:00+09:00"
        }
    ],
    "top_score": 50,
    "low_score": 20,
    "average_score": 35
}
```
