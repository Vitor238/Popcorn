package com.vitor238.popcorn.data

sealed class NetworkResult<out T> {
    class Success<out T>(val value: T) : NetworkResult<T>()
    class Error(val message: String?) : NetworkResult<Nothing>()
}