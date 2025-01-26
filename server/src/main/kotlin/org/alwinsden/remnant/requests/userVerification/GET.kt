package org.alwinsden.remnant.requests.userVerification

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
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
                        userData.demo_completed,
                        userData.filled_user_details
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