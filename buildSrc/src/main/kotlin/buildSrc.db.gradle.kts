plugins {
    kotlin("jvm")
}

dependencies {
    implementation("com.urosjarc:db-messiah:0.0.2")
    implementation("com.urosjarc:db-messiah-extra:0.0.2")
    runtimeOnly("org.xerial:sqlite-jdbc:3.44.1.0")
}
