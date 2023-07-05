plugins {
    kotlin("jvm") version "1.8.22"
    id("io.papermc.paperweight.userdev") version "1.5.5"
    id("xyz.jpenilla.run-paper") version "2.1.0"
    `maven-publish`
}

group = "net.craftoriya"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://repo.codemc.org/repository/maven-public/")
}
dependencies {
    paperDevBundle("1.19.3-R0.1-SNAPSHOT")
    compileOnly("de.tr7zw:item-nbt-api-plugin:2.11.3")
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks.test {
    useJUnitPlatform()
}