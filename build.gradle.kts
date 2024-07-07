plugins {
	id("java")
	id("application")
	id("checkstyle")
	id("jacoco")

	id("org.springframework.boot") version "3.3.1"
	id("io.spring.dependency-management") version "1.1.5"
	id("io.freefair.lombok") version "8.6"
	id("io.sentry.jvm.gradle") version "4.7.0"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

application {
	mainClass = "hexlet.code.AppApplication"
}

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.projectlombok:lombok:1.18.26")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("jakarta.validation:jakarta.validation-api:3.0.2")
	implementation("net.datafaker:datafaker:2.0.1")
	implementation("org.instancio:instancio-junit:3.3.0")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("org.mapstruct:mapstruct:1.5.5.Final")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

	annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.postgresql:postgresql:42.6.0")

	testImplementation(platform("org.junit:junit-bom:5.10.0"))
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.jacocoTestReport {
	reports { xml.required.set(true) }
}

sentry {
	includeSourceContext = true

	org = "hexlet-dg"
	projectName = "java-project-99"
	authToken = System.getenv("SENTRY_AUTH_TOKEN")
}

tasks.sentryBundleSourcesJava {
	enabled = System.getenv("SENTRY_AUTH_TOKEN") != null
}

