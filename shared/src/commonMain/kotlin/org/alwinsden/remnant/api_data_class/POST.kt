package org.alwinsden.remnant.api_data_class


import kotlinx.serialization.Serializable

@Serializable
data class AuthPost(
    val authCode: String,
    val authMachine: String
)

@Serializable
data class MessageResponseClass(
    val token: String,
)