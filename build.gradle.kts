import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.7.21"
    id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "ru.psu.techjava"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("ru.psu.techjava.app.CApplication")
}
javafx {
    version = "19"
    modules = mutableListOf("javafx.controls", "javafx.fxml", "javafx.graphics")
}
dependencies {
    //implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("no.tornado:tornadofx:1.7.20")
    implementation("no.tornado:tornadofx-controls:1.0.6")
    implementation("com.beust:klaxon:5.5")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}