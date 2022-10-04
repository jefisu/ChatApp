package com.jefisu.chatapp.core.validate

import com.jefisu.chatapp.core.util.UiText

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: UiText? = null
)