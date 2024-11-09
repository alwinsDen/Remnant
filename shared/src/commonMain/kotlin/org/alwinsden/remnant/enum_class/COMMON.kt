package org.alwinsden.remnant.enum_class

enum class GenderEnum(val value: Int, val displayName: String) {
    NOT_SPECIFIED(0, "not-specified"),
    MALE(1, "male"),
    FEMALE(2, "female"), ;

    companion object {
        fun fromValue(value: Int) = entries.find { it.value == value }
    }
}