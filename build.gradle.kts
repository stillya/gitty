import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    id("org.springframework.boot") version "2.6.5"
    // TODO: move to spring platform
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
}

group = "dev.stillya"
version = "0.0.1"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

repositories {
    mavenLocal()
    mavenCentral {
        content {
            excludeGroup("io.github.kotlin-telegram-bot")
        }
    }
    maven {
        url = "https://jitpack.io"
        content {
            includeGroup("io.github.kotlin-telegram-bot")
        }
    }
}

dependencies {
    // spring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    // telegram
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.0.6")
    implementation("org.telegram:telegrambots-spring-boot-starter:5.7.1")
    // scrapper
    implementation("org.jsoup:jsoup:1.15.1")
    // logging
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.20")
    // test
    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.testcontainers:mongodb:1.16.3")
    testImplementation("org.testcontainers:testcontainers:1.16.3")
    testImplementation("org.testcontainers:junit-jupiter:1.16.3")
    // for M1
    if (Os.isFamily(Os.FAMILY_MAC)) {
        runtimeOnly("io.netty:netty-resolver-dns-native-macos:4.1.73.Final:osx-x86_64")
    }
}

val kotlinLanguageVersion = "1.7"
kotlin {
    jvmToolchain {
        languageVersion.set(javaLanguageVersion)
    }
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
