package com.github.armkrtchyan.common.state

sealed interface State {
    object Loading : State
    object Empty : State
    data class Error(val throwable: Throwable) : State
    object Success : State
}