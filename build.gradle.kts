import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.21"
    id("signing")
    id("maven-publish")
}

val artifactName = "j-logger"
val artifactGroup = "kr.jadekim"
val artifactVersion = "1.1.2"
group = artifactGroup
version = artifactVersion

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    val kotlinxCoroutineVersion: String by project
    val slf4jVersion: String by project
    val jacksonVersion: String by project
    val gsonVersion: String by project
    val okHttpVersion: String by project
    val fuelVersion: String by project
    val koinVersion: String by project
    val ktorVersion: String by project

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutineVersion")

    implementation("org.slf4j:slf4j-api:$slf4jVersion")

    compileOnly("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    compileOnly("com.google.code.gson:gson:$gsonVersion")

    compileOnly("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")
    compileOnly("com.github.kittinunf.fuel:fuel:$fuelVersion")
    compileOnly("org.koin:koin-core:$koinVersion")
    compileOnly("io.ktor:ktor-server-core:$ktorVersion")
}

tasks.withType<KotlinCompile> {
    val jvmTarget: String by project

    kotlinOptions.jvmTarget = jvmTarget
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("lib") {
            groupId = artifactGroup
            artifactId = artifactName
            version = artifactVersion
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
        val ossrhUsername: String by project
        val ossrhPassword: String by project

        maven {
            name = "mavenCentral"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = ossrhUsername
                password = ossrhPassword
            }

//            mavenContent {
//                releasesOnly()
//            }
        }
//        maven {
//            name = "mavenCentralSnapshot"
//            setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots/")
//            credentials {
//                username = ossrhUsername
//                password = ossrhPassword
//            }
//
//            mavenContent {
//                snapshotsOnly()
//            }
//        }
    }
}

signing {
    sign(publishing.publications["lib"])
}