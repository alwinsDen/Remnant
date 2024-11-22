package org.alwinsden.remnant.requests.userVerification

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.alwinsden.remnant.api_data_class.ResponseMessage
import org.alwinsden.remnant.api_data_class.UserBasicDetails
import org.alwinsden.remnant.models.User.UserSchemaService
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.update

class UserVerificationPOST(private val database: Database) {

    private val userInstance = UserSchemaService(database)

    suspend fun Route.postUserBasicDetails() {
        post("/basic_user_details") {
            val req = call.receive<UserBasicDetails>()
            val userInfo = getUserInfo(call)
            userInstance.dbQuery {
                UserSchemaService.Users.update({ UserSchemaService.Users.id eq userInfo.id }) {
                    it[user_age] = req.userAge
                    it[gender] = req.gender
                    it[city] = req.city
                    it[working_hr_start] = req.workingHrStart
                    it[working_hr_end] = req.workingHrEnd
                    it[user_prompt] = req.userPrompt
                }
            }
            call.respond(
                HttpStatusCode.Accepted,
                ResponseMessage("The details have been updated.")
            )
        }
    }
}