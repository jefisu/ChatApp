package com.jefisu.chatapp.core.util

typealias SimpleResource = Resource<Unit>
sealed class Resource<T> {
    data class Success<T>(val data: T?) : Resource<T>()
    data class Error<T>(val uiText: UiText) : Resource<T>()
}