import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	val kotlinVersion = "1.3.30"
	id("org.springframework.boot") version "2.2.0.M2"
	id("org.jetbrains.kotlin.jvm") version kotlinVersion
	id("io.spring.dependency-management") version "1.0.7.RELEASE"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

tasks.withType<KotlinCompile> {
	kotlinOptions {
		jvmTarget = "1.8"
		freeCompilerArgs = listOf("-Xjsr305=strict", "-Xuse-experimental=kotlinx.coroutines.FlowPreview")
	}
}

repositories {
	mavenCentral()
	maven("https://repo.spring.io/snapshot")
	maven("https://repo.spring.io/milestone")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

dependencies {
	implementation("org.springframework.fu:spring-fu-kofu:0.0.6.BUILD-SNAPSHOT")
	implementation("org.springframework.data:spring-data-r2dbc:1.0.0.BUILD-SNAPSHOT")
	implementation("io.r2dbc:r2dbc-postgresql:1.0.0.BUILD-SNAPSHOT")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-mustache")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.0")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.2.0")
	runtimeOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "junit")
		exclude(module = "mockito-core")
	}
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("com.ninja-squad:springmockk:1.1.1")
	testImplementation("io.mockk:mockk:1.9.1")
}
