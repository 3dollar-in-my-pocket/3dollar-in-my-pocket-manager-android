package app.threedollars.domain.entity

import app.threedollars.domain.LoginMethod

data class SocialLoginToken(
    val accessToken: String,
    val refreshToken: String,
    val method: LoginMethod
)