package app.threedollars.common

sealed class Resource<T>(val data: T? = null, val errorMessage: String? = null, val code: String? = "") {

    class Success<T>(data: T, code: String?) : Resource<T>(data = data, code = code)

    class Error<T>(errorMessage: String?, code: String?) : Resource<T>(errorMessage = errorMessage, code = code)

}