plugins {
    id("maven-publish")
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("kapt") version "1.9.24"
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "io.github.qingshu-ui"
version = "0.0.4-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
    withJavadocJar()
    withSourcesJar()
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.meteordev.org/releases")
    }
}

dependencies {
    api(kotlin("reflect"))
    api("meteordevelopment:orbit:0.2.3")
    api("com.alibaba.fastjson2:fastjson2:2.0.52")
    api("org.springframework.boot:spring-boot-starter-websocket")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.jar {
    enabled = true
    archiveClassifier.set("")
}

tasks.bootJar {
    enabled = false
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()
            }
        }
    }
    repositories{
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/qingshu-ui/ayaka-spring-boot-starter")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}
