group = "me.wcaleniewolny.libresectors"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://storehouse.okaeri.eu/repository/maven-public/")
    }
}

dependencies {
    implementation(project(":api"))
    implementation("eu.okaeri:okaeri-persistence-mongo:1.5.14")
    implementation("eu.okaeri:okaeri-persistence-flat:1.5.13")
    implementation("eu.okaeri:okaeri-configs-json-simple:4.0.5")
    implementation("com.google.code.gson:gson:2.9.0")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}