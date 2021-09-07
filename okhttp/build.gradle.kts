import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

dependencies {
    val kotlinxDatetimeVersion: String by project

    implementation(project(":"))

    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
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
    publications {
        create<MavenPublication>("lib") {
            groupId = project.group as String
            artifactId = project.name
            version = project.version as String
            from(components["java"])

            pom {
                name.set("j-logger")
                description.set("Logging Library for kotlin.")
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
                        email.set("jinyong@jadekim.kr")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/j-logger.git")
                    developerConnection.set("scm:git:ssh://github.com/j-logger.git")
                    url.set("https://github.com/jdekim43/j-logger.git")
                }
            }
        }
    }

    repositories {
        maven {
            val jfrogUsername: String by project
            val jfrogPassword: String by project

            setUrl("https://jadekim.jfrog.io/artifactory/maven/")

            credentials {
                username = jfrogUsername
                password = jfrogPassword
            }
        }
    }
}