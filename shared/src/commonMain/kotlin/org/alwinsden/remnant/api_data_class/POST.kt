package org.alwinsden.remnant.api_data_class


import kotlinx.serialization.Serializable
import org.alwinsden.remnant.enum_class.GenderEnum

@Serializable
data class AuthPost(
    val authCode: String,
    val authMachine: String
)

@Serializable
data class MessageResponseClass(
    val token: String,
)

@Serializable
data class UserBasicDetails(
    val gender: Int = GenderEnum.NOT_SPECIFIED.value,
    val city: String = "not selected",
    val userAge: Int = 18,
    val workingHrStart: Int = 9,
    val workingMinuteStart: Int = 0,
    val workingHrEnd: Int = 18,
    val workingMinuteEnd: Int = 30,
    val userPrompt: String
)