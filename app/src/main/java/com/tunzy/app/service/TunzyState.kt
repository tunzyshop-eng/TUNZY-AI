package com.tunzy.app.service

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

enum class TunzyState {
    IDLE,
    WAKE,
    LISTENING,
    THINKING,
    SPEAKING
}

object TunzyStateHolder {
    private val _state = MutableStateFlow(TunzyState.IDLE)
    val state: StateFlow<TunzyState> = _state

    fun setState(newState: TunzyState) {
        _state.value = newState
    }
}