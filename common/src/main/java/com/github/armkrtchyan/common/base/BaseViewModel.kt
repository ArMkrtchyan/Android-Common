package com.github.armkrtchyan.common.base

import androidx.lifecycle.ViewModel
import com.github.armkrtchyan.common.shared.extensions.log
import com.github.armkrtchyan.common.state.State
import kotlinx.coroutines.flow.*

abstract class BaseViewModel : ViewModel(), IRetry {
    val stateFlow = MutableStateFlow<State>(State.Success)

    protected suspend inline fun <reified T> handleNetworkResult(
        request: Flow<T>,
        showLoading: Boolean = true,
        showError: Boolean = true,
        crossinline onSuccess: (T) -> Unit
    ) {
        request
            .onStart { if (showLoading) stateFlow.value = State.Loading }
            .catch { if (showError) stateFlow.value = State.Error(it) }
            .onCompletion { stateFlow.value = if (it is List<*> && it.isEmpty()) State.Empty else State.Success }
            .collectLatest {
                onSuccess(it.log())
            }
    }

    protected suspend inline fun <reified T, K> handlePairNetworkResult(
        first: Flow<T>,
        second: Flow<K>,
        showLoading: Boolean = true,
        showError: Boolean = true,
        crossinline onSuccess: (Pair<T, K>) -> Unit
    ) {
        first.zip(second) { firstResponse, secondResponse ->
            onSuccess(Pair(firstResponse, secondResponse).log())
        }
            .onStart { if (showLoading) stateFlow.value = State.Loading }
            .catch { if (showError) stateFlow.value = State.Error(it) }
            .onCompletion { stateFlow.value = State.Success }
            .collect()
    }

    protected suspend inline fun <reified T, K, F> handleTripleNetworkResult(
        first: Flow<T>,
        second: Flow<K>,
        third: Flow<F>,
        showLoading: Boolean = true,
        showError: Boolean = true,
        crossinline onSuccess: (Triple<T, K, F>) -> Unit
    ) {
        var firstRes: T? = null
        var secondRes: K? = null
        var thirdRes: F? = null
        first.zip(second) { firstResponse, secondResponse ->
            firstRes = firstResponse
            secondRes = secondResponse
        }.zip(third) { _, thirdResponse ->
            thirdRes = thirdResponse
        }
            .onStart { if (showLoading) stateFlow.value = State.Loading }
            .catch { if (showError) stateFlow.value = State.Error(it) }
            .onCompletion { stateFlow.value = State.Success }
            .collectLatest {
                if (firstRes != null && secondRes != null && thirdRes != null) {
                    onSuccess(Triple(firstRes!!, secondRes!!, thirdRes!!).log())
                }
            }
    }

    override fun retry() {
    }
}