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
import io.ktor.server.response.*
import io.ktor.server.routing.*
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

    //Routing class
    val openAPI_GET = OpenApiGET(database)
    val openAPI_POST = OpenApiPOST(
        jwkProvider = jwkProvider,
        database = database,
        applicationConfiguration = applicationConfiguration,
    )
    val userVerificationAPI_GET = UserVerificationGET(database)
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
                if (
                    credential.payload.getClaim("email").asString() != ""
                    &&
                    credential.payload.getClaim("name").asString() != ""
                    &&
                    credential.payload.getClaim("id").asString() != ""
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
            with(userVerificationAPI_GET) {
                CoroutineScope(Dispatchers.IO).launch {
                    jwtVerification()
                    getUserProfile()
                }
            }
            with(GeneralManagementAPI_POST){
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
        static(".well-known") {
            staticRootFolder = File("server/certs")
            file("jwks.json")
        }
    }
}