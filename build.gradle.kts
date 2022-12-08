import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("se.ohou.mortar.plugin") version "1.+"
    id("io.spring.dependency-management")

    id("org.jlleitschuh.gradle.ktlint")
    id("org.jetbrains.dokka")
    id("org.springframework.boot")

    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("plugin.allopen")
    kotlin("plugin.noarg")
}

group = "se.ohou.commerce"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://nexus.co-workerhou.se/repository/maven-public")
        maven(url = "https://nexus.co-workerhou.se/repository/maven-releases")
        maven(url = "https://nexus.co-workerhou.se/repository/maven-snapshots")
        mavenLocal()
    }

    tasks {
        withType<Assemble> {
            dependsOn("ktlintFormat")
        }

        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "11"
            }
        }

        withType<Test> {
            useJUnitPlatform()
            systemProperty("file.encoding", "UTF-8")
        }

        withType<BootJar> {
            enabled = false
        }
    }
}

subprojects {
    apply {
        plugin("idea")
        plugin("kotlin")
        plugin("kotlin-kapt")
        plugin("kotlin-spring")
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("org.jetbrains.dokka")
    }

    // 그룹이 동일하면 같은 이름의 모듈(ex. :common:api-model, :hashtag:api-model)이 dependency에 함께 추가되는 경우에
    // conflict(Circular dependency)가 발생할 수 있어서 그룹을 다르게 만든다.
    group = "se.ohou.commerce.${path.split(":")[1]}"
    version = "0.0.1-SNAPSHOT"

    tasks.withType<Jar> {
        // 기본 설정에서는 jar 파일 이름도 동일하게 생성될 수 있다. 동일한 jar 이름을 갖는 여러 dependency를 추가하면 오류가 발생하므로
        // 각각의 이름을 다르게 만들어준다.
        archiveFileName.set(
            project.path.split(":").drop(1).joinToString(separator = "-", postfix = "-") +
                project.version + ".jar"
        )
    }

    tasks.withType<BootJar> {
        enabled = false
    }

    val kotlinCoroutinesVersion: String by project
    val kotestVersion: String by project
    val mockkVersion: String by project

    dependencies {
        // Kotlin
        implementation(kotlin("reflect"))
        implementation(kotlin("stdlib-jdk8"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:$kotlinCoroutinesVersion")

        // kotest
        testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
        testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
        testImplementation("io.mockk:mockk:$mockkVersion")
    }

    configure<KtlintExtension> {
        filter {
            exclude { element -> element.file.path.contains("generated/") }
        }
    }
}

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(buildDir.resolve("$rootDir/devdocs"))
}
