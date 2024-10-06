plugins {
    id("maven-publish")
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("kapt") version "1.9.24"
    id("org.springframework.boot") version "3.3.2"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "io.github.qingshu-ui"
version = "1.1.0"

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
        url = uri("https://jitpack.io")
    }
}

dependencies {
    api(kotlin("reflect"))
    api("com.github.qingshu-ui:orbit:0.2.3")
    api("org.springframework.boot:spring-boot-starter-websocket")
    api("org.springframework.boot:spring-boot-starter-aop")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    api("com.squareup.okhttp3:okhttp:4.12.0")
    api("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
    kapt("org.springframework.boot:spring-boot-configuration-processor")
}

tasks.withType<Test> {
    useJUnitPlatform()
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

                withXml {
                    asNode().appendNode("repositories").apply {
                        appendNode("repository").apply {
                            appendNode("id", "jitpack.io")
                            appendNode("url", "https://jitpack.io")
                        }
                    }
                }
            }
        }
    }
}
