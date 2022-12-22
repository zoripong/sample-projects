import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val armeriaVersion: String by project
val opentracingVersion: String by project
val grpcVersion: String by project
val clayVersion: String by project

dependencies {
    implementation(project(":domain"))
    implementation(project(":database"))

    implementation("se.ohou.clay:armeria:$clayVersion") {
        exclude(group = "com.linecorp.armeria", module = "armeria-spring-boot2-webflux-starter")
        exclude(group = "com.linecorp.armeria", module = "armeria-grpc")
    }
    implementation("com.linecorp.armeria:armeria:$armeriaVersion")
    implementation("com.linecorp.armeria:armeria-spring-boot2-starter:$armeriaVersion")
    implementation("com.linecorp.armeria:armeria-tomcat9:$armeriaVersion")
    implementation("com.linecorp.armeria:armeria-grpc:$armeriaVersion")

    // gRPC
    implementation("se.ohou.mortar:mortar-libs-kotlin:1.+")
    implementation("io.grpc:grpc-netty-shaded:$grpcVersion")
    implementation("io.github.lognet:grpc-spring-boot-starter:4.9.0")

    // for gRPC mapping
    implementation("se.ohou.clay:grpc:$clayVersion")

    // spring-boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // kafka
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")

    // mongo
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("com.ninja-squad:springmockk:3.1.1")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
}

sourceSets.main {
    java.srcDirs("src/main/java", "build/generated/java")
    withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
        kotlin.srcDirs("src/main/kotlin", "build/generated/kotlin")
    }
}

tasks {
    // gRPC descriptor file 을 Armeria DocService가 읽을 수 있는 위치로 이동
    register<Copy>("copyDescriptorSet") {
        dependsOn("mortarPrepare")
        from(layout.buildDirectory.dir("generated/image.bin"))
        into(layout.buildDirectory.dir("resources/main/META-INF/armeria/grpc"))
    }

    register<Exec>("mortarPrepare") {
        workingDir = projectDir
        commandLine = listOf("mortar", "prepare")
        environment("NO_TTY", "1")
    }

    withType<KotlinCompile> {
        dependsOn("copyDescriptorSet")
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    bootJar {
        enabled = true
        archiveFileName.set(archiveBaseName.get() + "." + archiveExtension.get())
    }
}
