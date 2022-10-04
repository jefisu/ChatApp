package com.jefisu.chatapp.core.util

import com.ramcosta.composedestinations.spec.Direction

sealed class UiEvent {
    data class Navigate(val destination: Direction? = null) : UiEvent()
    object ShowBottomSheet : UiEvent()
}