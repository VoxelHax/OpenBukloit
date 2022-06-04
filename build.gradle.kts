import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.voxelhax"
version = "1.0.4"

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
    implementation("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
    implementation("com.rikonardo.cafebabe:CafeBabe:1.0.1")
    implementation("org.javassist:javassist:3.29.0-GA")
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta6")
    implementation("dev.virefire.kson:KSON:1.3.1")
    implementation("dev.virefire.yok:Yok:1.0.4")
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
        archiveClassifier.set("")
    }

    build {
        dependsOn(shadowJar)
    }
}

application {
    mainClass.set("MainKt")
}
