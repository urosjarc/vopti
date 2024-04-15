plugins {
    java
    application
    kotlin("jvm") version "1.9.22"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "3.0.1"
}

group = "com.urosjarc"
version = "0.0.1"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(19)
}

application {
    mainClass = "com.urosjarc.vopti.MainKt"
}

javafx {
    version = "19.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

jlink {
    imageZip = project.file("${buildDir}/distributions/app-${javafx.platform.classifier}.zip")
    options = listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages")
    launcher {
        name = "app"
    }
}

dependencies {
    // Kotlinx
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

    //Dark
    implementation("org.jfxtras:jmetro:11.6.14")

    //Database
    implementation("com.urosjarc:db-messiah:0.0.2")
    implementation("com.urosjarc:db-messiah-extra:0.0.2")
    runtimeOnly("org.xerial:sqlite-jdbc:3.44.1.0")

    //Logging
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.20.0")
    implementation("org.apache.logging.log4j:log4j-api-kotlin:1.2.0")

    //Koin
    implementation("io.insert-koin:koin-logger-slf4j:3.3.0")
    implementation("io.insert-koin:koin-core:3.3.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
}
