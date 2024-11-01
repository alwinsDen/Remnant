package org.alwinsden.remnant

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.alwinsden.remnant.api_data_class.AuthPost
import org.alwinsden.remnant.api_data_class.MessageResponseClass
import org.alwinsden.remnant.api_data_class.TestRequest
import org.alwinsden.remnant.controlUtils.configurationFirebase
import org.alwinsden.remnant.models.User.UserSchemaService
import org.alwinsden.remnant.models.configureDatabase

fun main(args: Array<String>) {
    embeddedServer(Netty, commandLineEnvironment(args))
        .start(wait = true)
}

fun Application.module() {
    val applicationConfiguration = environment.config
    configurationFirebase()
    val database = configureDatabase(applicationConfiguration)
    //enable schemas
    UserSchemaService(database)
    install(ContentNegotiation) {
        json(json = Json {
            prettyPrint = true
            isLenient = true
        })
    }
    routing {
        get("/") {
            call.respond(
                TestRequest(
                    "Pinging at this port",
                    SERVER_PORT
                )
            )
        }
        post("/auth") {
            val req = call.receive<AuthPost>()
            println("${req.authMachine} ${req.authCode}")
            val jwtAuthenticator = Authenticator(applicationConfig = applicationConfiguration)
                .generalAuthenticator(authMachine = req.authMachine, accessToken = req.authCode)
            call.respond(
                MessageResponseClass(
                    200,
                    "Auth code received",
                )
            )
        }
    }
}