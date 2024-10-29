package org.alwinsden.remnant.models.api_data_class

import kotlinx.serialization.Serializable

@Serializable
data class TestRequest(
    val testString: String,
    val localPort: Int
)