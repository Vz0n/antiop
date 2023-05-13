plugins {
    java
}

group = "io.github.vz0n"
version = "1.1-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
    implementation("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
}

tasks{
    compileJava {
        options.encoding = "UTF-8"
    }
}