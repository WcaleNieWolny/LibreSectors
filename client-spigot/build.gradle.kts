plugins {
    id("java")
    id("kr.entree.spigradle") version "2.4.2"
}

group = "me.wcaleniewolny.libresectors"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    maven { url = uri("https://storehouse.okaeri.eu/repository/maven-public/") }
}

dependencies{
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    implementation("eu.okaeri:okaeri-configs-yaml-bukkit:4.0.5")
    implementation(project(path = ":storage-okaeri", configuration = "default"))
    implementation("org.msgpack:msgpack-core:0.9.3")
}

val pluginName = "LibreSectorsClient"

spigot {
    name = pluginName
    authors = listOf("WcaleNieWolny")
    version = this.version
    website = "https://github.com/WcaleNieWolny/"
    apiVersion = "1.18"
    excludeLibraries = listOf("*")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}