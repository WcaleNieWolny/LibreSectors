plugins {
    id("java")
    id ("checkstyle")
    id ("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "me.wcaleniewolny.libresectors"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

allprojects {
    apply {
        plugin(    "java")
        plugin("checkstyle")
        plugin("com.github.johnrengelman.shadow")
    }

    tasks.build.get().dependsOn(tasks.shadowJar)

    tasks.register<com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation>("relocateShadowJar") {
        target = tasks.shadowJar.get()
        prefix = "me.wcaleniewolny.dependency" // Default value is "shadow"
    }

    tasks.shadowJar.get().dependsOn(tasks.getByName("relocateShadowJar"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}