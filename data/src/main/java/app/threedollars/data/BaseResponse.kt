package app.threedollars.data

open class BaseResponse<T>(
    data: T? = null,
    message: String? = "",
    resultCode: String? = ""
)