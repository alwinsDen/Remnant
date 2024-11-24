#TODO: configure this makefile to run gradle, jdk, deployment via docker.

ktor-docker-local:
	./gradlew :server:build
	docker compose up --build

ktor-docker-publish:
	docker login
	./gradlew :server:build
	docker compose build
	docker compose push