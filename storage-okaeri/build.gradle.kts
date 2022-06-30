group = "me.wcaleniewolny.libresectors"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://storehouse.okaeri.eu/repository/maven-public/") }
}

dependencies {
    implementation(project(":api"))
    implementation("eu.okaeri:okaeri-persistence-mongo:1.5.12")
    implementation("eu.okaeri:okaeri-configs-json-simple:4.0.5")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}