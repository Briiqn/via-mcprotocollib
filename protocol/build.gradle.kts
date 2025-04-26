plugins {
    id("mcprotocollib.publish-conventions")
    jacoco
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

version = "1.21.5-SNAPSHOT"
description = "MCProtocolLib is a simple library for communicating with Minecraft clients and servers."

dependencies {
    // Minecraft related libraries
    api(libs.cloudburstnbt)

    // Gson
    api(libs.gson)

    // MinecraftAuth for authentication
    api(libs.minecraftauth)

    // Slf4j
    api(libs.slf4j.api)

    // Kyori adventure
    api(libs.bundles.adventure)

    // Math utilities
    api(libs.bundles.math)

    // Stripped down fastutil
    api(libs.bundles.fastutil)

    // Netty
    api(libs.bundles.netty)

    // Checker Framework
    api(libs.checkerframework.qual)

    // Test dependencies
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.slf4j.simple)
    implementation("com.viaversion:viaversion-common:5.3.2")
    implementation("com.viaversion:viabackwards-common:5.3.2")
    implementation("com.viaversion:viarewind-common:4.0.7")
    implementation("net.raphimc:ViaLegacy:3.0.9")
    implementation("com.viaversion:viaaprilfools-common:4.0.1")
    implementation("com.viaversion:vialoader:4.0.2")
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }
}

// ShadowJar configuration
tasks.shadowJar {
    archiveClassifier.set("")
    mergeServiceFiles()


    minimize()
}

tasks.jar {
    archiveClassifier.set("slim")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

publishing {
    publications {
        getByName<MavenPublication>("maven") {
            artifact(tasks.shadowJar)
        }
    }
}
