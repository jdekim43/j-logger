plugins {
    kotlin("multiplatform")
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
                implementation(project(":"))

                implementation("io.insert-koin:koin-core:3.1.2")
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
