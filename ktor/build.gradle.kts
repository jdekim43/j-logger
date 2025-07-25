plugins {
    kotlin("multiplatform")
    id("org.jetbrains.dokka")
}

kotlin {
    jvmToolchain(8)

    jvm {
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                val kotlinxDatetimeVersion: String by project

                implementation(project(":"))
                implementation(project(":${rootProject.name}-coroutine"))

                implementation("io.ktor:ktor-server-core:3.1.3")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                val junitVersion: String by project

                implementation(kotlin("test-junit5"))
                runtimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
                compileOnly("org.junit.jupiter:junit-jupiter-api:$junitVersion")
                compileOnly("org.junit.jupiter:junit-jupiter-params:$junitVersion")
            }
        }
    }

    val dokkaHtml by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class)
    val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
        dependsOn(dokkaHtml)
        archiveClassifier.set("javadoc")
        from(dokkaHtml.outputDirectory)
    }

    publishing {
        publications.withType<MavenPublication> {
            artifact(javadocJar)
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
}
