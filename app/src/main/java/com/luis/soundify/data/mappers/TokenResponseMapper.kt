package com.luis.soundify.data.mappers

import com.luis.soundify.data.entities.TokenResponse
import com.luis.soundify.domain.models.TokenModel

fun TokenResponse.map(): TokenModel {
    return TokenModel(
        accessToken = this.accessToken,
        tokenType = this.tokenType,
        expiresIn = this.expiresIn,
        refreshToken = this.refreshToken,
        scope = this.scope
    )
}