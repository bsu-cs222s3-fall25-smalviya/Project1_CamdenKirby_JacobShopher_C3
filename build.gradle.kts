plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.jayway.jsonpath:json-path:2.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    implementation("net.minidev:json-smart:2.5.0")
}

tasks.test {
    useJUnitPlatform()
}