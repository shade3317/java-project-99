build:
	./gradlew clean build

report:
	./gradlew jacocoTestReport

start:
	./gradlew bootRun --args='--spring.profiles.active=development'

start-prod:
	./gradlew bootRun --args='--spring.profiles.active=production'

install-dist:
	./gradlew clean installDist

.PHONY: build