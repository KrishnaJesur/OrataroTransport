package com.edusunsoft.transport.orataro.ui.activitymaps

import android.Manifest.permission
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.utils.AppPreference
import com.edusunsoft.transport.orataro.utils.Connectivity
import com.edusunsoft.transport.orataro.utils.Controller

class PermissionActivity : AppCompatActivity(), View.OnClickListener {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1234
    private var tv_permission: TextView? = null
    private var btn_permission: Button? = null
    private var locationManager: LocationManager? = null

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_permission -> if (!AppPreference.isPermissionDenied(this)) {
                requestPermissionRuntime()
            } else {
                gotoSettingForLocationPermission("To enable,go to Settings and turn on Location permissions")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission)
        tv_permission = findViewById(R.id.tv_permission)
        btn_permission = findViewById(R.id.btn_permission)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        btn_permission!!.setOnClickListener(this)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {

            MapsActivity2.FROSTR = bundle.getString(resources.getString(R.string.FromStr))
            MapsActivity2.BUSROUTEID = bundle.getString(resources.getString(R.string.busRouteid))
            MapsActivity2.ROUTEID = bundle.getString(resources.getString(R.string.Routeid))
            MapsActivity2.isForpickup = bundle.getBoolean(resources.getString(R.string.isForPickup))

            if (MapsActivity2.isForpickup) {
                MapsActivity2.ISFORPICKUP = resources.getString(R.string.True)
            } else {
                MapsActivity2.ISFORPICKUP = resources.getString(R.string.False)
            }

            MapsActivity2.BUSROUTEDATE = bundle.getString(resources.getString(R.string.currentdate))
            MapsActivity2.DEVICEID = bundle.getString(resources.getString(R.string.deviceId))
            MapsActivity2.STARTTIME = bundle.getString(resources.getString(R.string.currenttime))
        }

    }

    private fun requestPermissionRuntime() {
        ActivityCompat.requestPermissions(this, arrayOf(permission.ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
    }

    private fun gotoSettingForLocationPermission(message: String) {
        AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK") { dialog, which ->
                    dialog.dismiss()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", packageName, null))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                }
                .setNegativeButton("Cancel") { dialog, which ->
                    dialog.dismiss()
                }
                .create()
                .show()
    }

    private fun isPermissionGranted(): Boolean {
        val result: Int = ContextCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION)
        val result1: Int = ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if (locationAccepted && cameraAccepted) {
                        if (Connectivity.isGpsEnable(locationManager)) {

                            //create a Bundle object
                            val extras = Bundle()
                            //Adding key value pairs to this bundle
                            //there are quite a lot data types you can store in a bundle
                            extras.putString(resources.getString(R.string.busRouteid), MapsActivity2.BUSROUTEID)
                            extras.putString(resources.getString(R.string.Routeid), MapsActivity2.ROUTEID)
                            extras.putBoolean(resources.getString(R.string.isForPickup), MapsActivity2.isForpickup)
                            extras.putString(resources.getString(R.string.currentdate), MapsActivity2.BUSROUTEDATE)
                            extras.putString(resources.getString(R.string.deviceId), Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID))
                            extras.putString(resources.getString(R.string.currenttime), MapsActivity2.STARTTIME)
                            if (Controller.isRunningTrip) {
                                extras.putString(resources.getString(R.string.FromStr), "FromRunningTrip")
                            } else {
                                extras.putString(resources.getString(R.string.FromStr), "FromRouteSelection")
                            }

                            val intent = Intent(this, MapsActivity2::class.java)
                            intent.putExtras(extras)
                            startActivity(intent)

                        } else {
                            val intent1 = Intent(this, LocationServiceActivity::class.java)
                            startActivity(intent1)
                            finish()
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(permission.ACCESS_FINE_LOCATION) || shouldShowRequestPermissionRationale(permission.ACCESS_COARSE_LOCATION)) {
                                return
                            } else {
                                AppPreference.setPermissionDenied(this, true)
                                setPermissionLocationText(2)
                            }
                        }
                    }
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (isPermissionGranted()) {
            if (Connectivity.isGpsEnable(locationManager)) {
                //create a Bundle object
                val extras = Bundle()
                //Adding key value pairs to this bundle
                //there are quite a lot data types you can store in a bundle
                extras.putString(resources.getString(R.string.busRouteid), MapsActivity2.BUSROUTEID)
                extras.putString(resources.getString(R.string.Routeid), MapsActivity2.ROUTEID)
                extras.putBoolean(resources.getString(R.string.isForPickup), MapsActivity2.isForpickup)
                extras.putString(resources.getString(R.string.currentdate), MapsActivity2.BUSROUTEDATE)
                extras.putString(resources.getString(R.string.deviceId), Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID))
                extras.putString(resources.getString(R.string.currenttime), MapsActivity2.STARTTIME)
                if (Controller.isRunningTrip) {
                    extras.putString(resources.getString(R.string.FromStr), "FromRunningTrip")
                } else {
                    extras.putString(resources.getString(R.string.FromStr), "FromRouteSelection")
                }
                val intent = Intent(this, MapsActivity2::class.java)
                intent.putExtras(extras)
                startActivity(intent)
            } else {
                val intent1 = Intent(this, LocationServiceActivity::class.java)
                startActivity(intent1)
                finish()
            }

        } else {

            if (AppPreference.isPermissionDenied(this)) {
                setPermissionLocationText(2)
            } else {
                setPermissionLocationText(1)
            }

        }

    }

    private fun setPermissionLocationText(isFrom: Int) {
        if (isFrom == 1) {
            btn_permission!!.text = "Allow"
        } else if (isFrom == 2) {
            btn_permission!!.text = "open settings"
        }
    }

}
