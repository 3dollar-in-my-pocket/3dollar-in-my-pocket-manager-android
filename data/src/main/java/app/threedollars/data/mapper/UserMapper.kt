package app.threedollars.data.mapper

import app.threedollars.data.user.response.SignResponse
import app.threedollars.domain.entity.user.User

object UserMapper {

    fun loginUserMapper(loginResponse: SignResponse?): User {
        return User(
            bossId = loginResponse?.bossId ?: "",
            token = loginResponse?.token ?: "",
        )
    }
}