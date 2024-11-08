package org.alwinsden.remnant.requests.openApi

import com.auth0.jwk.JwkProvider
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.alwinsden.remnant.Authenticator
import org.alwinsden.remnant.api_data_class.AuthPost
import org.alwinsden.remnant.api_data_class.MessageResponseClass
import org.alwinsden.remnant.api_data_class.ResponseMessage
import org.jetbrains.exposed.sql.Database

class OpenApiPOST(
    private val database: Database,
    private val applicationConfiguration: ApplicationConfig,
    private val jwkProvider: JwkProvider
) {
    fun Route.generateJwtAuth() {
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
    }
}