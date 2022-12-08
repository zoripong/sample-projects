dependencies {
    implementation(project(":domain"))
    implementation(project(":database"))

    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.batch:spring-batch-test")
}

tasks {
    bootJar {
        enabled = true
        archiveFileName.set(archiveBaseName.get() + "." + archiveExtension.get())
    }
    register("prepareKotlinBuildScriptModel")
}
