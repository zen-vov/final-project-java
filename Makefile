.PHONY: up down logs build test run

up:
\tdocker compose up --build

down:
\tdocker compose down -v

logs:
\tdocker compose logs -f --tail=200

build:
\tmvn -Dmaven.repo.local=/private/tmp/m2 -DskipTests package

test:
\tmvn -Dmaven.repo.local=/private/tmp/m2 test

run:
\tmvn -Dmaven.repo.local=/private/tmp/m2 spring-boot:run

