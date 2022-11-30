plugins {
    kotlin("multiplatform") version "1.7.22"
    id("org.jetbrains.dokka") version "1.7.20"
    id("maven-publish")
    id("signing")
}

allprojects {
    apply {
        plugin("maven-publish")
        plugin("signing")
    }

    group = "kr.jadekim"
    version = "2.0.4"

    repositories {
        mavenCentral()
    }

    publishing {
        repositories {
            val ossrhUsername: String by project
            val ossrhPassword: String by project

            if (version.toString().endsWith("-SNAPSHOT", true)) {
                maven {
                    name = "mavenCentralSnapshot"
                    setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                    credentials {
                        username = ossrhUsername
                        password = ossrhPassword
                    }
                }
            } else {
                maven {
                    name = "mavenCentral"
                    setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                    credentials {
                        username = ossrhUsername
                        password = ossrhPassword
                    }
                }
            }
        }
    }

    signing {
        sign(publishing.publications)
    }
}

kotlin {
    jvm {
        compilations.all {
            val jvmTarget: String by rootProject

            kotlinOptions.jvmTarget = jvmTarget
        }
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
//    js(LEGACY) {
//        browser()
//        nodejs()
//    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                val kotlinxDatetimeVersion: String by project

                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")

                implementation("co.touchlab:stately-concurrency:1.2.3")
                implementation("co.touchlab:stately-collections:1.2.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
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
//        val jsMain by getting
//        val jsTest by getting
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

tasks.named("publish") {
    subprojects.forEach {
        finalizedBy("${it.name}:publish")
    }
}
