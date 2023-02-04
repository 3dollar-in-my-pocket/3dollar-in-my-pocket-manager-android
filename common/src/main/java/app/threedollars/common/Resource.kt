package app.threedollars.common

sealed class Resource<T>(val data: T? = null, val errorMessage: String? = null, val code : String? ="") {

    class Success<T>(data: T) : Resource<T>(data = data)

    class Error<T>(errorMessage: String?,code: String?) : Resource<T>(errorMessage = errorMessage, code = code)

}