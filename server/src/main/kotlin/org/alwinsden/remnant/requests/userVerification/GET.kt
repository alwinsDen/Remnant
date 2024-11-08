package org.alwinsden.remnant.requests.userVerification

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import org.alwinsden.remnant.api_data_class.ResponseMessage
import org.alwinsden.remnant.api_data_class.UserProfileClass
import org.alwinsden.remnant.models.User.UserSchemaService
import org.jetbrains.exposed.sql.Database


class UserVericationGET(private val database: Database) {
    fun Route.jwtVerification() {
        get("/generate_jwt") {
            val principal = call.principal<JWTPrincipal>()
            val emailIdentifier = principal!!.payload.getClaim("email").asString()
            val username = principal.payload.getClaim("name").asString()
            val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
            call.respondText("Hello $emailIdentifier with $username, token expires at $expiresAt")
        }
    }

    fun Route.getUserProfile() {
        get("/profile") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal!!.payload.getClaim("id").asInt()
            val userInstance = UserSchemaService(database)
            runBlocking {
                val userData = userInstance.readUserProfileId(
                    id = userId
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
                        message = "User doesn't exist."
                    )
                )
            }
        }
    }
}