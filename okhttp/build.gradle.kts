import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

dependencies {
    val kotlinxDatetimeVersion: String by project

    implementation(project(":"))

    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
}

tasks.withType<KotlinCompile> {
    val jvmTarget: String by rootProject

    kotlinOptions.jvmTarget = jvmTarget
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications.create<MavenPublication>("lib") {
        groupId = project.group.toString()
        artifactId = project.name
        version = project.version.toString()
        from(components["java"])

        pom {
            name.set(project.name)
            description.set("Logging Library for Kotlin")
            url.set("https://github.com/jdekim43/j-logger")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("jdekim43")
                    name.set("Jade Kim")
                }
            }
            scm {
                connection.set("scm:git:git://github.com/jdekim43/j-logger.git")
                developerConnection.set("scm:git:git://github.com/jdekim43/j-logger.git")
                url.set("https://github.com/jdekim43/j-logger")
            }
        }
    }
}