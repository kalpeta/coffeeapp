# CoffeeStore

Monorepo layout:
- `app/` — Spring Boot application
- `docker/` — local infra (Postgres, Kafka, etc.)

## Status
- Day 1: Boot skeleton, `/products` hardcoded (controller → service → repository)
- Day 2: Postgres via docker-compose; Flyway V1 migration; local profile wired

## Run (local)
1) `docker compose -f docker/docker-compose.local.yml up -d`
2) `cd app && ./mvnw spring-boot:run -Dspring-boot.run.profiles=local`
3) `curl http://localhost:8080/products`
