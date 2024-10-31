package org.alwinsden.remnant.api_data_class

import kotlinx.serialization.Serializable

@Serializable
data class TestRequest(
    val testString: String,
    val localPort: Int
)