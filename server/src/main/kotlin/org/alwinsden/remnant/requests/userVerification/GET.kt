package org.alwinsden.remnant.requests.userVerification

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.alwinsden.remnant.api_data_class.ExposedUserWithId
import org.alwinsden.remnant.api_data_class.ResponseMessage
import org.alwinsden.remnant.models.User.UserSchemaService
import org.jetbrains.exposed.sql.Database


data class BaseInfo(val email: String, val id: Int, val name: String, val expiresAt: Long)

fun getUserInfo(call: ApplicationCall): BaseInfo {
    val principal = call.principal<JWTPrincipal>()
    val email = principal!!.payload.getClaim("email").asString()
    val name = principal.payload.getClaim("name").asString()
    val id = principal.payload.getClaim("id").asInt()
    val expiration = principal.expiresAt?.time?.minus(System.currentTimeMillis())
    return BaseInfo(email, id, name, expiration!!)
}

class UserVerificationGET(private val database: Database) {
    private val userInstance = UserSchemaService(database)
    suspend fun Route.jwtVerification() {
        get("/generate_jwt") {
            val userInfo = getUserInfo(call);
            call.respondText("Hello ${userInfo.id} with ${userInfo.name}, token expires at ${userInfo.expiresAt}")
        }
    }

    suspend fun Route.getUserProfile() {
        get("/profile") {
            val userInfo = getUserInfo(call);
            val userData = userInstance.readUserProfileId(
                id = userInfo.id
            )
            if (userData != null) {
                call.respond(
                    HttpStatusCode.Accepted,
                    ExposedUserWithId(
                        userData.id,
                        userData.name,
                        userData.email,
                        userData.state,
                        userData.gender,
                        userData.city,
                        userData.demo_completed
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