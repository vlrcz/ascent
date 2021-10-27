package com.skillbox.ascentstrava.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionManager @Inject constructor(
    private val context: Context
) {

    private val networkListener = MutableLiveData(isNetworkAvailable())

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager?.registerDefaultNetworkCallback(
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        networkListener.postValue(checkNetworkConnection(connectivityManager, network))
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
                        networkListener.postValue(checkNetworkConnection(connectivityManager, null))
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        networkListener.postValue(checkNetworkConnection(connectivityManager, network))
                    }
                }
            )
        }
    }

    fun observeNetworkState(): MutableLiveData<Boolean> = networkListener

    private fun isNetworkAvailable(): Boolean {
        var result = false

        connectivityManager?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                result = checkNetworkConnection(this, this.activeNetwork)
            } else {
                val networks = this.allNetworks
                networks.forEach { network ->
                    if (checkNetworkConnection(this, network)) {
                        result = true
                    }
                }
            }
        }
        return result
    }

    private fun checkNetworkConnection(
        connectivityManager: ConnectivityManager,
        network: Network?
    ): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(network)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return true
                }
            }
        }
        return false
    }
}