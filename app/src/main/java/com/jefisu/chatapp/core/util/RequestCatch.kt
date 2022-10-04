package com.jefisu.chatapp.core.util

import com.jefisu.chatapp.R
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import java.io.IOException

suspend inline fun <T> requestCatch(
    block: () -> T
): Resource<T> {
    return try {
        Resource.Success(block())
    } catch (e: ResponseException) {
        val responseText = e.response.bodyAsText()
        if (responseText.isNotBlank()) {
            Resource.Error(UiText.DynamicString(responseText))
        } else {
            Resource.Error(
                UiText.StringResource(R.string.oops_something_went_wrong)
            )
        }
    } catch (e: IOException) {
        Resource.Error(
            UiText.StringResource(R.string.error_couldnt_reach_server)
        )
    }
}