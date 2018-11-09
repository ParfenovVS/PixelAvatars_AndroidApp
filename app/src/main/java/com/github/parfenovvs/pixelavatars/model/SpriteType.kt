package com.github.parfenovvs.pixelavatars.model

enum class SpriteType {
    MALE,
    FEMALE,
    RANDOM
}

fun SpriteType.toValue() = when (this) {
    SpriteType.MALE -> "male"
    SpriteType.FEMALE -> "female"
    SpriteType.RANDOM -> "identicon"
}