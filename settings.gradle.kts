rootProject.name = "spring-multi-module-template" // TODO(template): 수정 필요

include(":application:api")
include(":application:batch")
include(":application:consumer")
include(":database")
include(":domain")

pluginManagement {
    val ktlintVersion: String by settings
    val springDependencyManagement: String by settings
    val kotlinVersion: String by settings
    val springBootVersion: String by settings

    plugins {
        id("io.spring.dependency-management") version springDependencyManagement

        id("org.jlleitschuh.gradle.ktlint") version ktlintVersion
        id("org.springframework.boot") version springBootVersion
        id("org.jetbrains.dokka") version kotlinVersion

        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.jpa") version kotlinVersion apply false
        kotlin("plugin.allopen") version kotlinVersion apply false
        kotlin("plugin.noarg") version kotlinVersion apply false
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "se.ohou.mortar.plugin") {
                useModule("se.ohou.mortar:mortar-plugin:${requested.version}")
            }
        }
    }

    repositories {
        gradlePluginPortal()
        maven(url = "https://nexus.co-workerhou.se/repository/maven-public")
        maven(url = "https://nexus.co-workerhou.se/repository/maven-releases")
        maven(url = "https://nexus.co-workerhou.se/repository/maven-snapshots")
    }
}
