#TODO: configure this makefile to run gradle, jdk, deployment via docker.
-include .env

devserver:
	./gradlew run