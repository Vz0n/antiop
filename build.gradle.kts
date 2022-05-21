plugins {
    java
}

group = "com.monkeycraftservices.antiop"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
}

dependencies {
    implementation("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
}

tasks.getByName<JavaCompile>("compileJava"){
    options.encoding = "UTF-8"
}