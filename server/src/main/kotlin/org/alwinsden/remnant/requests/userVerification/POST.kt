package org.alwinsden.remnant.requests.userVerification

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
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
                    it[gender] = req.gender
                    it[city] = req.city
                    it[user_age] = req.userAge
                    it[working_hr_start] = req.workingHrStart
                    it[working_minute_start] = req.workingMinuteStart
                    it[working_hr_end] = req.workingHrEnd
                    it[working_minute_end] = req.workingMinuteEnd
                    it[user_prompt] = req.userPrompt
                    it[filled_user_details] = true
                }
            }
            call.respond(
                HttpStatusCode.Accepted,
                ResponseMessage("The details have been updated.")
            )
        }
    }
}