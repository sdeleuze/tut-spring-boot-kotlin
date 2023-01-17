import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	// K2 supported with Spring plugins only as of Kotlin 1.8.20 not released yet, see https://youtrack.jetbrains.com/issue/KT-53590
	val kotlinVersion = "1.8.20-dev-5799"
	id("org.springframework.boot") version "3.0.1"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version kotlinVersion
	kotlin("plugin.spring") version kotlinVersion
	kotlin("plugin.allopen") version kotlinVersion
	kotlin("plugin.jpa") version kotlinVersion
	// kapt is not supported with K2, see https://github.com/spring-projects/spring-boot/issues/28046 related Spring Boot issue
	// kotlin("kapt") version kotlinVersion
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/") }
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-mustache")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.springframework.boot:spring-boot-devtools")
	// kapt is not supported with K2, see https://github.com/spring-projects/spring-boot/issues/28046 related Spring Boot issue
	// kapt("org.springframework.boot:spring-boot-configuration-processor")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "mockito-core")
	}
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("com.ninja-squad:springmockk:4.0.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		jvmTarget = "17"
		freeCompilerArgs += "-Xjsr305=strict"
		useK2 = true
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.Embeddable")
	annotation("jakarta.persistence.MappedSuperclass")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
