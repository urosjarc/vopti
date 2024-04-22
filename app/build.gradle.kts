plugins {
    this.id("buildSrc.common")
    this.id("buildSrc.logging")
    this.id("buildSrc.injections")
    this.id("buildSrc.db")
}
dependencies {
    this.implementation(this.project(":core"))
}
