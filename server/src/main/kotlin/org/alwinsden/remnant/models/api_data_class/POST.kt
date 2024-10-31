package org.alwinsden.remnant.models.api_data_class

import kotlinx.serialization.Serializable

@Serializable
data class AuthPost(
    val authCode: String,
    val authMachine: String
)

@Serializable
data class MessageResponseClass(
    val responseCode: Int,
    val responseMessage: String,
)