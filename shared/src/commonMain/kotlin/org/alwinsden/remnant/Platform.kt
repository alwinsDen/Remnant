package org.alwinsden.remnant

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform