package org.alwinsden.remnant

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
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
import java.io.File
import java.util.concurrent.TimeUnit

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "localhost", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val applicationConfiguration = environment.config
    configurationFirebase()
    val database = configureDatabase()
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
            generalAuthenticator(
                accessToken = req.authCode,
                authMachine = req.authMachine,
                applicationConfig = applicationConfiguration
            )
            call.respond(
                MessageResponseClass(
                    200,
                    "Auth code received",
                )
            )
        }
    }
}