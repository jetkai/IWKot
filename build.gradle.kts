import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.6.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.jetbrains.dokka") version "1.5.31"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
}

group = "iw4"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    //Required for Spring Web
    implementation("org.springframework.boot:spring-boot-starter-security:2.6.6")
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.6")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.6")
    developmentOnly("org.springframework.boot:spring-boot-devtools:2.6.6")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.6")
    testImplementation("org.springframework.security:spring-security-test:5.6.2")

    //Required for Serializing/Deserializing
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.2")

    //Required for Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    //Required for Dokka
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
    dokkaHtmlPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.5.31")

    //Required for MariaDB driver
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:3.0.4")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
