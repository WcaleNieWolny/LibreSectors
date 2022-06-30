group = "me.wcaleniewolny.libresectors"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url =  uri("https://storehouse.okaeri.eu/repository/maven-public/") }
}

dependencies {
    implementation("eu.okaeri:okaeri-configs-core:4.0.5")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()

}