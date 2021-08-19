plugins {
    kotlin("multiplatform") version "1.5.21"
    id("maven-publish")
}

allprojects {
    apply {
        plugin("maven-publish")
    }

    group = "kr.jadekim"
    version = "2.0.0-rc1"

    repositories {
        mavenCentral()
        maven("https://jadekim.jfrog.io/artifactory/maven/")
    }

    configurations.all {
        resolutionStrategy {
            failOnVersionConflict()
            preferProjectModules()

            dependencySubstitution {
//                substitute(module("$group:${rootProject.name}")).using(project(":"))

                all {
                    (requested as? ModuleComponentSelector)?.let {
                        if (it.group != rootProject.group) {
                            return@let
                        }

                        val targetProject = if (it.module == rootProject.name) {
                            rootProject
                        } else {
                            findProject(":${it.module}")
                        } ?: return@let

                        useTarget(targetProject)
                    }
                }
            }
        }
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
    js(LEGACY) {
        browser()
        nodejs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                val kotlinxDatetimeVersion: String by project

                implementation("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxDatetimeVersion")

                implementation("co.touchlab:stately-concurrency:1.1.7")
                implementation("co.touchlab:stately-iso-collections:1.1.7-a1")
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
        val jsMain by getting
        val jsTest by getting
    }

    publishing {
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
}

tasks.named("publish") {
    subprojects.forEach {
        finalizedBy("${it.name}:publish")
    }
}
