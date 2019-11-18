//import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
//import com.jfrog.bintray.gradle.BintrayExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
//    id("com.github.johnrengelman.shadow") version "2.0.2"
//    `maven-publish`
//    id("com.jfrog.bintray") version "1.8.4"
}

group = "kr.jadekim"
version = "1.0.0"
//val artifactID = "j-logger"

repositories {
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

//val shadowJar: ShadowJar by tasks
//shadowJar.apply {
//    baseName = artifactID
//    classifier = null
//}
//
//val publicationName = "JLogger"
//publishing {
//    publications.invoke {
//        publicationName(MavenPublication::class) {
//            artifactId = artifactID
//            artifact(shadowJar)
//            with(pom) {
//                withXml {
//                    asNode().appendNode("dependencies").let { node ->
//                        configurations.compile.allDependencies.forEach {
//                            node.appendNode("dependency").apply {
//                                appendNode("groupId", it.group)
//                                appendNode("artifactId", it.name)
//                                appendNode("version", it.version)
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//bintray {
//    user = System.getenv("BINTRAY_USER")
//    key = System.getenv("BINTRAY_KEY")
//    dryRun = false
//    publish = true
//    override = true
//    setPublications(publicationName)
//    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
//        repo = "maven"
//        name = "j-logger"
//        userOrg = "jdekim43"
//        vcsUrl = "https://github.com/jdekim43/j-logger.git"
//        setLicenses("MIT")
//    })
//}