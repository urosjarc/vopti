plugins {
    java
    this.id("buildSrc.common")
    this.id("buildSrc.logging")
    this.id("buildSrc.injections")
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "3.0.1"
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
    this.implementation(this.project(":app"))
    this.implementation(this.project(":core"))
    implementation("org.jfxtras:jmetro:11.6.14")
}
