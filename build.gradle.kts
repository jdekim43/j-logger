import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Date

plugins {
    kotlin("jvm") version "1.3.50"
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.4"
}

val artifactName = "j-logger"
val artifactGroup = "kr.jadekim"
val artifactVersion = "1.0.0"
group = artifactGroup
version = artifactVersion

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    val kotlinxCoroutineVersion: String by project
    val jacksonVersion: String by project

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutineVersion")

    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            groupId = artifactGroup
            artifactId = artifactName
            version = artifactVersion
            from(components["java"])
            artifact(sourcesJar)
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")

    publish = true

    setPublications("lib")

    pkg.apply {
        repo = "maven"
        name = rootProject.name
        setLicenses("MIT")
        setLabels("kotlin", "logger")
        vcsUrl = "https://github.com/jdekim43/j-logger.git"
        version.apply {
            name = artifactVersion
            released = Date().toString()
        }
    }
}