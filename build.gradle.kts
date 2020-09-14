import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Date

plugins {
    kotlin("jvm") version "1.4.10"
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.4"
}

val artifactName = "j-logger"
val artifactGroup = "kr.jadekim"
val artifactVersion = "1.0.23"
group = artifactGroup
version = artifactVersion

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    val kotlinxCoroutineVersion: String by project
    val slf4jVersion: String by project
    val jacksonVersion: String by project
    val gsonVersion: String by project
    val okHttpVersion: String by project
    val koinVersion: String by project
    val ktorVersion: String by project

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutineVersion")

    implementation("org.slf4j:slf4j-api:$slf4jVersion")

    compileOnly("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    compileOnly("com.google.code.gson:gson:$gsonVersion")

    compileOnly("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")
    compileOnly("org.koin:koin-core:$koinVersion")
    compileOnly("io.ktor:ktor-server-core:$ktorVersion")
}

tasks.withType<KotlinCompile> {
    val jvmTarget: String by project

    kotlinOptions.jvmTarget = jvmTarget
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