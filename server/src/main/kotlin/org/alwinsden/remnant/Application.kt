package org.alwinsden.remnant

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.alwinsden.remnant.models.configureDatabase

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "localhost", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureDatabase()
    routing {
        get("/") {
            call.respondText("Pinging at port :8080")
        }
    }
}