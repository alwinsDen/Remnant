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
    val city: String? = null,
    val userAge: Int = 13,
    val workingHrStart: Int = 6,
    val workingHrEnd: Int = 17,
    val userPrompt: String
)