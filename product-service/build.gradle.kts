plugins {
	java
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
	id("io.freefair.lombok") version "8.0.1"
}

group = "ca.gbc"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Spring Boot Starters
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-web")

	// Lombok for annotation processing
	compileOnly("org.projectlombok:lombok:1.18.28")
	annotationProcessor("org.projectlombok:lombok:1.18.28")
	testCompileOnly("org.projectlombok:lombok:1.18.28")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.28")

	// Spring Boot development tools
	developmentOnly("org.springframework.boot:spring-boot-devtools")

	// Testing dependencies
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	// TestContainers for MongoDB
	testImplementation("org.testcontainers:junit-jupiter:1.19.0") // Ensure you have the correct version here
	testImplementation("org.testcontainers:mongodb:1.19.0")

	// Unit testing dependencies
	testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")

	// Mocking framework
	testImplementation("org.mockito:mockito-core:5.5.0")

	// RestAssured for REST API testing
	testImplementation("io.rest-assured:rest-assured:5.3.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.test {
	useJUnitPlatform()
	testLogging {
		events("passed", "skipped", "failed")
	}
}