package com.edusunsoft.transport.orataro.utils

import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager

object Connectivity {
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    fun isGpsEnable(locationManager: LocationManager?): Boolean {
        if (locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)!!) {
            return true
        } else if (locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true
        }
        return false
    }
}