package com.jefisu.chatapp.core.util

sealed class Resource<T> {
    data class Success<T>(val data: T?) : Resource<T>()
    data class Error<T>(val uiText: UiText) : Resource<T>()
}