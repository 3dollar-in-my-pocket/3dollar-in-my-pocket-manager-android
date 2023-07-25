package app.threedollars.common

data class ErrorResponse(
    val resultCode: String,
    val message: String,
    val data: String?
)
