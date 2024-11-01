package org.alwinsden.remnant.models

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.alwinsden.remnant.models.User.UserSchemaService
import org.jetbrains.exposed.sql.Database

fun configureDatabase() : Database {
    return Database.connect(
        url = "jdbc:postgresql:///testdb",
        user = "postgres",
        driver = "org.postgresql.Driver",
        password = "1234",
    )
}