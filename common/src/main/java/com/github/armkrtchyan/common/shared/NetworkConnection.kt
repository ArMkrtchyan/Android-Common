package com.github.armkrtchyan.common.shared

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.net.InetSocketAddress
import java.net.Socket

class NetworkConnection(context: Context) {
    private val cm: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val validNetworks: MutableSet<Network> = HashSet()
    private val _networkStateFlow: MutableStateFlow<Boolean> by lazy { MutableStateFlow(true) }
    val networkStateFlow: StateFlow<Boolean>
        get() = _networkStateFlow
    private var delayTime = 5000L

    init {
        cm.registerNetworkCallback(
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(), createNetworkCallback()
        )
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                ping().collect {
                    delayTime = if (it) 5000L
                    else 1600L
                    _networkStateFlow.value = it
                }
                delay(delayTime)
            }
        }
    }

    private fun createNetworkCallback() = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            cm.getNetworkCapabilities(network)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .apply {
                    if (this == true) {
                        CoroutineScope(Dispatchers.IO).launch {
                            ping().collect {
                                if (it) {
                                    validNetworks.add(network); checkValidNetworks()
                                } else _networkStateFlow.value = false
                            }
                        }
                    } else _networkStateFlow.value = false
                }
        }

        override fun onLost(network: Network) {
            validNetworks.remove(network)
            checkValidNetworks()
        }
    }

    suspend fun ping(): Flow<Boolean> {
        return flow {
            Socket().apply { connect(InetSocketAddress("8.8.8.8", 53), 20000);close() }
            emit(true)
        }.catch { emit(false) }
    }

    private fun checkValidNetworks() {
        if (_networkStateFlow.value != validNetworks.size > 0) _networkStateFlow.value = validNetworks.size > 0
    }
}