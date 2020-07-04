package com.edusunsoft.transport.orataro.helper

import android.app.Activity
import android.content.IntentSender
import com.edusunsoft.transport.orataro.base.BaseActivity
import com.edusunsoft.transport.orataro.utils.REQUEST_CODE_ENABLE_GPS
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import java.lang.ref.WeakReference


object LocationHelper {
    lateinit var weakRefActivity: WeakReference<Activity>

    fun enableGPS(activity: Activity) {
        this.weakRefActivity = WeakReference(activity)
        val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 1
        mLocationRequest.smallestDisplacement = 10F
        mLocationRequest.fastestInterval = 1
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()

        builder.addLocationRequest(mLocationRequest)

        val task = weakRefActivity.get()?.let {
            LocationServices.getSettingsClient(it).checkLocationSettings(builder.build())
        }

        task?.addOnCompleteListener {
            try {
                val response = it.getResult(ApiException::class.java) as LocationSettingsResponse
                // All location settings are satisfied. The client can initialize location
                // requests here.
                if (response.locationSettingsStates.isGpsPresent) {
                    (weakRefActivity.get() as BaseActivity).getCurrentLocation()
                }
            } catch (exception: ApiException) {
                when (exception.statusCode) {

                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(weakRefActivity.get(), REQUEST_CODE_ENABLE_GPS)
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {

                    }
                }
            }
        }
    }
}