package com.vitor238.popcorn.data

sealed class Search<out T> {
    class Success<out T>(val value: T) : Search<T>()
    object NoResults : Search<Nothing>()
    class Error(val message: String?) : Search<Nothing>()
}