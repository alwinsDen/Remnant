package org.alwinsden.remnant.models

import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.routing.*
import org.alwinsden.remnant.models.User.UserSchemaService
import org.jetbrains.exposed.sql.Database

fun configureDatabase(applicationConfiguration: ApplicationConfig) : Database {
    return Database.connect(
        url = applicationConfiguration.property("postgresConfig.url").getString(),
        user = applicationConfiguration.property("postgresConfig.user").getString(),
        driver = applicationConfiguration.property("postgresConfig.driver").getString(),
        password = applicationConfiguration.property("postgresConfig.password").getString(),
    )
}