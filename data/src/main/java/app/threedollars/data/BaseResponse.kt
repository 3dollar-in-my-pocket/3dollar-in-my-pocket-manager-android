package app.threedollars.data

open class BaseResponse<T>(
    val data: T? = null,
    val message: String? = "",
    val resultCode: String? = ""
)