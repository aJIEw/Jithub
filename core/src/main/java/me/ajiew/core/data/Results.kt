package me.ajiew.core.data


sealed class Results<out T> {
    data class Success<out T>(val data: T) : Results<T>()

    data class Error(
        val error: Throwable,
        val message: String = "Network request error!"
    ) : Results<Nothing>()
}