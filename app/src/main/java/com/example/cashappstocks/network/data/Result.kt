package com.example.cashappstocks.network.data

sealed class Result<T>(data: T?, message: String?, exception: Throwable?) {
    class Loading<T> : Result<T>(null, null, null)
    data class Error<T>(val message: String, val exception: Throwable? = null) :
        Result<T>(null, message = message, exception = exception)
    data class Success<T>(val data: T) : Result<T>(data, null, null)
}
