package com.edusunsoft.transport.orataro.ui.activitymaps

import android.Manifest.permission
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.utils.Connectivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.*

class LocationServiceActivity : AppCompatActivity(), View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    // variable declaration for location mannager and location request
    val REQUEST_LOCATION = 100
    private var tv_location: TextView? = null
    private var btn_location: Button? = null
    private var locationManager: LocationManager? = null
    private var locationRequest: LocationRequest? = null
    private var locationSettingsRequest: LocationSettingsRequest.Builder? = null
    private var pendingResult: PendingResult<LocationSettingsResult>? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_service)
        tv_location = findViewById(R.id.tv_location)
        btn_location = findViewById(R.id.btn_location)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        btn_location!!.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_location -> mEnableGps()
        }
    }

    fun mEnableGps() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()
        mGoogleApiClient!!.connect()
        mLocationSetting()
    }

    private fun mLocationSetting() {
        locationRequest = LocationRequest.create()
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest!!.interval = 1 * 1000.toLong()
        locationRequest!!.fastestInterval = 1 * 1000.toLong()
        locationSettingsRequest = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
        mResult()
    }

    fun mResult() {
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, locationSettingsRequest!!.build())
        pendingResult!!.setResultCallback(ResultCallback { locationSettingsResult ->
            val status = locationSettingsResult.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                    // All location settings are satisfied. The client can initialize location
// requests here.
                    val extras = Bundle()
                    //Adding key value pairs to this bundle
                    //there are quite a lot data types you can store in a bundle
                    extras.putString(resources.getString(R.string.busRouteid), MapsActivity2.BUSROUTEID)
                    extras.putString(resources.getString(R.string.Routeid), MapsActivity2.ROUTEID)
                    extras.putBoolean(resources.getString(R.string.isForPickup), MapsActivity2.isForpickup)
                    extras.putString(resources.getString(R.string.currentdate), MapsActivity2.BUSROUTEDATE)
                    extras.putString(resources.getString(R.string.deviceId), Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID))
                    extras.putString(resources.getString(R.string.currenttime), MapsActivity2.STARTTIME)
                    extras.putString(resources.getString(R.string.FromStr), "FromRouteSelection")
                    val intent = Intent(this, MapsActivity2::class.java)
                    intent.putExtras(extras)
                    startActivity(intent)

                    overridePendingTransition(0, 0)
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    status.startResolutionForResult(this@LocationServiceActivity, REQUEST_LOCATION)
                } catch (e: SendIntentException) {
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (isPermissionGranted()) {
            if (Connectivity.isGpsEnable(locationManager)) {
                val extras = Bundle()
                //Adding key value pairs to this bundle
                //there are quite a lot data types you can store in a bundle
                extras.putString(resources.getString(R.string.busRouteid), MapsActivity2.BUSROUTEID)
                extras.putString(resources.getString(R.string.Routeid), MapsActivity2.ROUTEID)
                extras.putBoolean(resources.getString(R.string.isForPickup), MapsActivity2.isForpickup)
                extras.putString(resources.getString(R.string.currentdate), MapsActivity2.BUSROUTEDATE)
                extras.putString(resources.getString(R.string.deviceId), Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID))
                extras.putString(resources.getString(R.string.currenttime), MapsActivity2.STARTTIME)
                extras.putString(resources.getString(R.string.FromStr), "FromRouteSelection")
                val intent = Intent(this, MapsActivity2::class.java)
                intent.putExtras(extras)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        } else {
            val intent1 = Intent(this, PermissionActivity::class.java)
            startActivity(intent1)
            finish()
            overridePendingTransition(0, 0)
        }
    }

    private fun isPermissionGranted(): Boolean {

        val result: Int = ContextCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
        val result1: Int = ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_LOCATION -> when (resultCode) {
                Activity.RESULT_OK -> {
                    val extras = Bundle()
                    //Adding key value pairs to this bundle
                    //there are quite a lot data types you can store in a bundle
                    extras.putString(resources.getString(R.string.busRouteid), MapsActivity2.BUSROUTEID)
                    extras.putString(resources.getString(R.string.Routeid), MapsActivity2.ROUTEID)
                    extras.putBoolean(resources.getString(R.string.isForPickup), MapsActivity2.isForpickup)
                    extras.putString(resources.getString(R.string.currentdate), MapsActivity2.BUSROUTEDATE)
                    extras.putString(resources.getString(R.string.deviceId), Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID))
                    extras.putString(resources.getString(R.string.currenttime), MapsActivity2.STARTTIME)
                    extras.putString(resources.getString(R.string.FromStr), "FromRouteSelection")
                    val intent = Intent(this, MapsActivity2::class.java)
                    intent.putExtras(extras)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                }
                Activity.RESULT_CANCELED -> {
                }
            }
        }
    }

    override fun onConnected(@Nullable bundle: Bundle?) {}
    override fun onConnectionSuspended(i: Int) {}
    override fun onConnectionFailed(p0: ConnectionResult) {}

}
