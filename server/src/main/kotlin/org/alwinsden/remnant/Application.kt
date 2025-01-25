package org.alwinsden.remnant

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.file
import io.ktor.server.http.content.static
import io.ktor.server.http.content.staticRootFolder
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.alwinsden.remnant.controlUtils.configurationFirebase
import org.alwinsden.remnant.models.User.UserSchemaService
import org.alwinsden.remnant.models.configureDatabase
import org.alwinsden.remnant.requests.generalManagement.GeneralManagementPOST
import org.alwinsden.remnant.requests.openApi.OpenApiGET
import org.alwinsden.remnant.requests.openApi.OpenApiPOST
import org.alwinsden.remnant.requests.userVerification.UserVerificationGET
import org.alwinsden.remnant.requests.userVerification.UserVerificationPOST
import java.io.File
import java.util.UUID
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

    //Routing class
    val openAPI_GET = OpenApiGET(database)
    val openAPI_POST = OpenApiPOST(
        jwkProvider = jwkProvider,
        database = database,
        applicationConfiguration = applicationConfiguration,
    )
    val userVerificationAPI_GET = UserVerificationGET(database)
    val userVerificationAPI_POST = UserVerificationPOST(database)
    val GeneralManagementAPI_POST = GeneralManagementPOST(database)

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
                val credEmail = credential.payload.getClaim("email").asString()
                val credName = credential.payload.getClaim("name").asString()
                val credId = credential.payload.getClaim("id").asInt()
                val credJtiIdentifier = credential.payload.getClaim("jti_identifier").asString()
                if (
                    credEmail.isNotEmpty() && credName.isNotEmpty() && credId !== null && credJtiIdentifier.isNotEmpty()
                ) {
                    val checkUserExist = UserSchemaService(database)
                        .verifyUserExistence(
                            email = credEmail,
                            id = credId,
                            name = credName,
                            uuid_token = UUID.fromString(credJtiIdentifier)
                        )
                    if (checkUserExist > 0) {
                        JWTPrincipal(credential.payload)
                    } else {
                        null
                    }
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
        static(".well-known") {
            staticRootFolder = File("server/certs")
            file("jwks.json")
        }
        authenticate("auth-jwt") {
            with(userVerificationAPI_GET) {
                CoroutineScope(Dispatchers.IO).launch {
                    jwtVerification()
                    getUserProfile()
                }
            }
            with(userVerificationAPI_POST) {
                CoroutineScope(Dispatchers.IO).launch {
                    postUserBasicDetails()
                }
            }
            with(GeneralManagementAPI_POST) {
                CoroutineScope(Dispatchers.IO).launch {
                    completeDemoState()
                }
            }
        }
        with(openAPI_GET) {
            CoroutineScope(Dispatchers.IO).launch {
                serverStatusCheck()
            }
        }
        with(openAPI_POST) {
            CoroutineScope(Dispatchers.IO).launch {
                generateJwtAuth()
            }
        }
    }
}