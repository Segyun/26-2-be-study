.DEFAULT_GOAL := help

COMPOSE := docker compose

.PHONY: help db-up run dev up down status logs app-logs db-logs build

help: ## Show available commands
	@awk 'BEGIN { FS = ":.*##"; print "Usage: make <target>\n" } /^[a-zA-Z0-9_-]+:.*##/ { printf "  %-12s %s\n", $$1, $$2 }' $(MAKEFILE_LIST)

db-up: ## Start PostgreSQL in the background
	$(COMPOSE) up -d db

run: ## Run the Spring Boot app locally (expects PostgreSQL to be running)
	@set -a; . ./.env; set +a; ./gradlew bootRun

dev: db-up run ## Start PostgreSQL, then run the app locally

up: ## Build and start the app and PostgreSQL in Docker
	$(COMPOSE) up --build -d

down: ## Stop and remove Compose containers (keeps database data)
	$(COMPOSE) down

status: ## Show Compose service status
	$(COMPOSE) ps

logs: ## Follow logs from all Compose services
	$(COMPOSE) logs -f

app-logs: ## Follow application container logs
	$(COMPOSE) logs -f app

db-logs: ## Follow PostgreSQL container logs
	$(COMPOSE) logs -f db

build: ## Build the Spring Boot executable JAR
	./gradlew bootJar
