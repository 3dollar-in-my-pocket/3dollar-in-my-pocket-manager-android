package app.threedollars.domain.entity.user

import app.threedollars.domain.LoginMethod
import java.time.LocalDateTime

data class UserDetail(
    val businessNumber: String,
    val bossId: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val name: String,
    val socialMethod: LoginMethod,
    val isSetupNotification: Boolean
)
