import java.net.URI

plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "1.9.0"
    id("maven-publish")
}

group = "gr.le0nidas"
version = "0.0.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    testImplementation(kotlin("test"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = group.toString()
            artifactId = "ollama-kotlin-playground"
            version = version.toString()

            from(components["java"])
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = URI.create("https://maven.pkg.github.com/le0nidas/ollama-kotlin-playground")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}