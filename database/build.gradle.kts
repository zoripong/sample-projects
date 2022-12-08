plugins {
    kotlin("plugin.jpa")
    kotlin("plugin.allopen")
    kotlin("plugin.noarg")
}

noArg {
    annotation("javax.persistence.Entity")
}

allOpen {
    annotation("javax.persistence.Entity")
}

val clayVersion: String by project

dependencies {
    implementation(project(":domain"))
    api("se.ohou.clay:datasource:$clayVersion")
}
