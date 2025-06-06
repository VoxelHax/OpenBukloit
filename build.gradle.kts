import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.voxelhax"
version = "1.1.0"

repositories {
    mavenCentral()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        url = uri("https://maven.rikonardo.com/releases")
    }
}

dependencies {
    testImplementation(kotlin("test"))
    // Spigot API dependency for Bukkit plugin development
    implementation("org.spigotmc:spigot-api:1.19-R0.1-SNAPSHOT")
    // CafeBabe library for working with Java class files
    implementation("com.rikonardo.cafebabe:CafeBabe:1.0.1")
    // Javassist library for bytecode manipulation
    implementation("org.javassist:javassist:3.29.0-GA")
    // Mordant library for colored console output
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta6")
    // KSON and Yok for configuration parsing
    implementation("dev.virefire.kson:KSON:1.3.1")
    implementation("dev.virefire.yok:Yok:1.0.4")
    // Jarchivelib for archive manipulation
    implementation("org.rauschig:jarchivelib:1.2.0")
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks {
    named<ShadowJar>("shadowJar") {
        // Configures the ShadowJar task to create a single executable JAR
        archiveClassifier.set("") // Removes the classifier from the JAR name
    }

    build {
        // Ensures the shadowJar task is run as part of the build process
        dependsOn(shadowJar)
    }
}

application {
    // Specifies the main class for the application
    mainClass.set("MainKt")
}
