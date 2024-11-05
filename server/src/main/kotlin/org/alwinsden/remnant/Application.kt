package org.alwinsden.remnant

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.http.*
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
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.alwinsden.remnant.api_data_class.*
import org.alwinsden.remnant.controlUtils.configurationFirebase
import org.alwinsden.remnant.models.User.UserSchemaService
import org.alwinsden.remnant.models.configureDatabase
import java.io.File
import java.util.concurrent.TimeUnit

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

    val issuer = applicationConfiguration.property("jwt.issuer").getString()
    val realmJWT = applicationConfiguration.property("jwt.realm").getString()
    val jwkProvider = JwkProviderBuilder(issuer)
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    //installations
    install(ContentNegotiation) {
        json(json = Json {
            prettyPrint = true
            isLenient = true
        })
    }
    install(Authentication) {
        jwt("auth-jwt") {
            realm = realmJWT
            verifier(jwkProvider, issuer) {
                acceptLeeway(3)
            }
            validate { credential ->
                if (
                    credential.payload.getClaim("email").asString() != ""
                    &&
                    credential.payload.getClaim("name").asString() != ""
                ) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired.")
            }
        }
    }
    routing {
        authenticate("auth-jwt") {
            get("/generate_jwt") {
                val principal = call.principal<JWTPrincipal>()
                val emailIdentifier = principal!!.payload.getClaim("email").asString()
                val username = principal.payload.getClaim("name").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello $emailIdentifier with $username, token expires at $expiresAt")
            }
            get("/profile") {
                val principal = call.principal<JWTPrincipal>()
                val userEmail = principal!!.payload.getClaim("email").asString()
                val userInstance = UserSchemaService(database)
                runBlocking {
                    val userData = userInstance.readEmail(
                        email = userEmail
                    )
                    if (userData != null) {
                        call.respond(
                            HttpStatusCode.Accepted,
                            UserProfileClass(
                                profile = userData
                            )
                        )
                    }
                    call.respond(
                        HttpStatusCode.Unauthorized,
                        ResponseMessage(
                            message = "Failed authentication"
                        )
                    )
                }
            }
        }
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
            val authenticator =
                Authenticator(
                    applicationConfig = applicationConfiguration,
                    jwkProvider = jwkProvider,
                    database = database
                )
            val verifiedDetails =
                authenticator.generalAuthenticator(authMachine = req.authMachine, accessToken = req.authCode)
            if (verifiedDetails != null) {
                val jwtTokenResponse =
                    authenticator.generateJWT(email = verifiedDetails.email, name = verifiedDetails.name)
                call.respond(
                    HttpStatusCode.Accepted,
                    MessageResponseClass(
                        token = jwtTokenResponse,
                    )
                )
            }
            call.respond(
                HttpStatusCode.Unauthorized,
                ResponseMessage(message = "failed the authentication.")
            )
        }
        static(".well-known") {
            staticRootFolder = File("server/certs")
            file("jwks.json")
        }
    }
}