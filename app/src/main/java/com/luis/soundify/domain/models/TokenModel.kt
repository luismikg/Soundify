package com.luis.soundify.domain.models

data class TokenModel(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Int,
    val refreshToken: String?,
    val scope: String
)