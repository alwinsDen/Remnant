import io.ktor.plugin.features.*

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    kotlin("plugin.serialization") version libs.versions.kotlin.get()
    application
}

group = "org.alwinsden.remnant"
version = "1.0.0"
application {
    mainClass.set("org.alwinsden.remnant.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

ktor {
    /*this is useful for simple docker deployment. Too much hassle
    * trying to get simple stuff done.*/
    //TODO: introduce docker compose to also include ignored files.
    docker {
        jreVersion.set(JavaVersion.VERSION_17)
        portMappings.set(
            listOf(
                io.ktor.plugin.features.DockerPortMapping(
                    80,
                    8080,
                    io.ktor.plugin.features.DockerPortMappingProtocol.TCP
                )
            )
        )
        val envVars = loadEnvFile().map { (key, value) ->
            DockerEnvironmentVariable(key, value)
        }
        environmentVariables.set(envVars)
    }
}

//this function loads the env variables from .env file.
fun loadEnvFile(envFile: String = "../.env"): Map<String, String> {
    val envFileBuffer = file(envFile)
    if (!envFileBuffer.exists()) {
        return emptyMap()
    }
    return envFileBuffer.readLines()
        .filter { it.isNotBlank() && !it.startsWith("#") }
        .associate {
            val (key, value) = it.split("=", limit = 2)
            key.trim() to value.trim()
        }
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "org.alwinsden.remnant.ApplicationKt"
    }
    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.common)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.bundles.exposed)
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.status.pages)
    implementation(libs.bundles.ktor)
    implementation("com.google.firebase:firebase-admin:9.4.1")
    implementation("org.postgresql:postgresql:42.7.4")
}