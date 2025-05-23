plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":"))

    implementation("io.sentry:sentry:7.22.5")
    implementation("io.sentry:sentry-kotlin-extensions:7.22.5")
}

kotlin {
    jvmToolchain(8)
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