package org.alwinsden.remnant.requests.openApi

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.alwinsden.remnant.SERVER_PORT
import org.alwinsden.remnant.api_data_class.TestRequest
import org.jetbrains.exposed.sql.Database

class OpenApiGET(private val database: Database) {
    fun Route.serverStatusCheck() {
        get("/") {
            call.respond(
                TestRequest(
                    "Pinging at this port",
                    SERVER_PORT
                )
            )
        }
    }
}