package com.example.cashappstocks.network.data

sealed class Result<T>(data: T? = null, code: Int? = null ,message: String? = null, exception: Throwable? = null) {
    class Loading<T> : Result<T>()
    data class Error<T>(val code:Int?, val message: String?, val exception: Throwable? = null) :
        Result<T>(message = message, exception = exception)

    data class Success<T>(val data: T) : Result<T>(data)
}
