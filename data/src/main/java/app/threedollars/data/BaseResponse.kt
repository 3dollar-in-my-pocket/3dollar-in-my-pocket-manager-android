package app.threedollars.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
open class BaseResponse<T>(
    data: T? = null,
    message: String? = "",
    resultCode: String? = ""
)