.PHONY: up down logs build test run

up:
	docker compose up --build

down:
	docker compose down -v

logs:
	docker compose logs -f --tail=200

build:
	mvn -Dmaven.repo.local=/private/tmp/m2 -DskipTests package

test:
	mvn -Dmaven.repo.local=/private/tmp/m2 test

run:
	mvn -Dmaven.repo.local=/private/tmp/m2 spring-boot:run

