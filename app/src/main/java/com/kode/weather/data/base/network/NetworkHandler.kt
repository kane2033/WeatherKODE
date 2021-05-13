package com.kode.weather.data.base.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat

class NetworkHandler(context: Context) {

    private val connectivityManager =
        ContextCompat.getSystemService(context, ConnectivityManager::class.java)
                as ConnectivityManager

    fun isNetworkAvailable(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork =
                connectivityManager.getNetworkCapabilities(network) ?: return false

            return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) or
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) or
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) or
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}