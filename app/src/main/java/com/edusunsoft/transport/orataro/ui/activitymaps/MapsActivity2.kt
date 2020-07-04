package com.edusunsoft.transport.orataro.ui.activitymaps

import android.Manifest.permission
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.GpsStatus
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseActivity
import com.edusunsoft.transport.orataro.databinding.ActivityMapsBinding
import com.edusunsoft.transport.orataro.ui.activityaboutus.AboutusActivity
import com.edusunsoft.transport.orataro.ui.activitycontactus.ContactusActivity
import com.edusunsoft.transport.orataro.ui.activitydashboard.DashboardActivity
import com.edusunsoft.transport.orataro.ui.activitylicense.LDActivity
import com.edusunsoft.transport.orataro.ui.activitylogin.LoginActivity
import com.edusunsoft.transport.orataro.ui.activityprofile.ProfileActivity
import com.edusunsoft.transport.orataro.ui.activityroute.RoutActivity
import com.edusunsoft.transport.orataro.ui.activityselectrout.SelectRoutActivity
import com.edusunsoft.transport.orataro.ui.activitysplash.SplashActivity
import com.edusunsoft.transport.orataro.ui.activitystudentattendance.StudentAttendanceListActivity
import com.edusunsoft.transport.orataro.ui.activitytc.TCActivity
import com.edusunsoft.transport.orataro.ui.activitytripdetails.TripDetailsActivity
import com.edusunsoft.transport.orataro.utils.*
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.CancelableCallback
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.nav_header_dashboard.view.*


class MapsActivity2 : BaseActivity(), OnMapReadyCallback, MapsNavigator, DrawerLayout.DrawerListener, NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GpsStatus.Listener {


    override fun RedirectToLogin() {
        startActivity(LoginActivity.getInstance(this@MapsActivity2))
        finish()
    }

    override fun onBackButtonClick() {
        startActivity(SelectRoutActivity.getInstance(this))
        finish()
    }

    private val TAG: String = MapsActivity2::class.java.simpleName
    lateinit var drawer_layout: DrawerLayout
    private lateinit var binding: ActivityMapsBinding
    private var bangaloreRoute: MutableList<LatLng>? = ArrayList()
    private lateinit var mapsViewModel: MapsViewModel
    lateinit var marker: BitmapDescriptor
    var markerPoint: Marker? = null

    var context: Context? = null

    // Variables to get Current Location
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var googleApiClient: GoogleApiClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationManager: LocationManager? = null
    private var location: Location? = null
    private val latLng = DoubleArray(2)
    private var currentLongitude: Double = 0.0
    private var currentLatitude = 0.0
    private var currentlatLng: LatLng? = null

    // variable for activity is in background or foreground
    private var isActivityForground: Boolean = false

    private val FIREBASE_UPDATE_INTERVAL: Long = 5 * 1000  /* 5 secs */
    val REQUEST_FINE_LOCATION: Int = 101
    private val ERROR_DIALOG_REQUEST = 9001

    private lateinit var dialog: Dialog

    var busLocationMarker: Marker? = null
    private var pathLatLngs: List<LatLng>? = null

    companion object {


        const val EXTRA_START_LAT = "start_lat"
        const val EXTRA_START_LONG = "start_long"
        const val EXTRA_STOP_LAT = "stop_lat"
        const val EXTRA_STOP_LONG = "stop_long"

        // variables for InsertBusTrip
        var BUSROUTEID: String = ""
        var BUSROUTENAME: String = ""
        var VEHICLEREGNO: String = ""
        var ROUTEID: String = ""
        var ISFORPICKUP: String = ""
        var BUSROUTEDATE: String = ""
        var DEVICEID: String = ""
        var STARTTIME: String = ""
        var FROSTR: String = ""
        var isForpickup: Boolean = false


        fun getInstance(selectRoutActivity: SelectRoutActivity, startLat: Double, startLong: Double, stopLat: Double, stopLong: Double): Intent {
            val intent = Intent(selectRoutActivity, MapsActivity2::class.java)
            intent.putExtra(EXTRA_START_LAT, startLat)
            intent.putExtra(EXTRA_START_LONG, startLong)
            intent.putExtra(EXTRA_STOP_LAT, stopLat)
            intent.putExtra(EXTRA_STOP_LONG, stopLong)
            return intent
        }

        fun getInstance(mapsActivity2: MapsActivity2): Intent? {
            return Intent(mapsActivity2, DashboardActivity::class.java)
        }

        const val EXTRA_TRIP_ID = "trip_id"
        const val EXTRA_DRIVER_ID = "driver_id"

        fun getInstance(selectRoutActivity: SelectRoutActivity, tripId: String, driverId: String): Intent {
            val intent = Intent(selectRoutActivity, MapsActivity2::class.java)
            intent.putExtra(EXTRA_TRIP_ID, tripId)
            intent.putExtra(EXTRA_DRIVER_ID, driverId)
            return intent
        }

        fun getInstance(selectRoutActivity: SelectRoutActivity, BUSROUTEID: String): Intent? {
            val intent = Intent(selectRoutActivity, MapsActivity2::class.java)
            intent.putExtra(BUSROUTEID, BUSROUTEID)
            return intent
        }

    }

    private lateinit var mMap: GoogleMap


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)

        // get selected routelist data using intent
        val bundle: Bundle? = intent.extras
        if (bundle != null) {

            FROSTR = bundle.getString(resources.getString(R.string.FromStr))
            BUSROUTEID = bundle.getString(resources.getString(R.string.busRouteid))
            ROUTEID = bundle.getString(resources.getString(R.string.Routeid))
//            BUSROUTENAME = bundle.getString(resources.getString(R.string.busRoutename))
//            VEHICLEREGNO = bundle.getString(resources.getString(R.string.vehicleregno))

            if (FROSTR == "FromRunningTrip") {
                Controller.isRunningTrip = true
                tv_btn_end_trip.visibility = View.VISIBLE
                tv_btn_start_trip.visibility = View.GONE
                img_menu.visibility = View.GONE
            }

            isForpickup = bundle.getBoolean(resources.getString(R.string.isForPickup))

            ISFORPICKUP = if (isForpickup) {
                resources.getString(R.string.True)
            } else {
                resources.getString(R.string.False)
            }

            BUSROUTEDATE = bundle.getString(resources.getString(R.string.currentdate))
            DEVICEID = bundle.getString(resources.getString(R.string.deviceId))
            STARTTIME = bundle.getString(resources.getString(R.string.currenttime))

            /*END*/

        }

        mapsViewModel = MapsViewModel(this, this)
        binding.mMapsViewModel = mapsViewModel

        drawer_layout = binding.drawerLayout
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        if (Connectivity.isNetworkConnected(this)) {
            try {
                locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (isPermissionGranted()) {
                    if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
                        return
                    }

//                    locationManager!!.addGpsStatusListener(this)
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            // Get last known recent location using new Google Play Services SDK (v11+)
            mFusedLocationProviderClient = getFusedLocationProviderClient(this)

        } else {
            Toaster.shortToast(this, resources.getString(R.string.checknwconnection))
        }

        initialization()

        drawer_layout.addDrawerListener(this)
        nav_view.setNavigationItemSelectedListener(this)

        //Header profile click event
        val headerView = binding.navView.getHeaderView(0)
        headerView.cl_profile.setOnClickListener {
            startActivity(ProfileActivity.getInstance(this))
        }

    }


    fun initialization() {


        if (!Connectivity.isNetworkConnected(this)) {
            Toaster.shortToast(this, resources.getString(R.string.checknwconnection))
        } else {
            checkPlayService()
        }

    }

    private fun isGoogleServiceAvailable(): Boolean {

        Log.d("isServiceOK", "isServicesOK: checking google services version")
        val available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        val dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, ERROR_DIALOG_REQUEST)
        if (available == ConnectionResult.SUCCESS) { //everything is fine and the user can make map requests
            Log.d("isServiceWorking", "isServicesOK: Google Play Services is working")
            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) { //an error occured but we can resolve it
            Log.d("errorOccured", "isServicesOK: an error occured but we can fix it")
//            val dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, ERROR_DIALOG_REQUEST)
            dialog.show()
        } else {
            dialog.dismiss()
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show()
        }

        return false
    }

    private fun checkPlayService() {

        if (isGoogleServiceAvailable()) {
            initMap()
        }
    }

    private fun initMap() {

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


    }


    override fun onMapReady(map: GoogleMap) {

        mMap = map

        if (checkPermissions()) {
            mMap.isMyLocationEnabled = false
            mMap.uiSettings.isMyLocationButtonEnabled = false
            mMap.uiSettings.isCompassEnabled = false

        }


        //TODO need to check draw map or not. or HIDE progress bar.
        // prepare poly line
        //TODO need to check draw map or not. or HIDE progress bar.
        // prepare poly line


        mMap.setOnMapLoadedCallback {

            var countStudentList: ArrayList<GetRouteListModel.StudentInfoItem>? = ArrayList()

            // show polyline
            showpath()
            /*END*/

            // setMuliple Marker on Polyline and also display student list count for particular pickup point
            for (i in 0 until MapsViewModel.RouteList!!.size) {
                var LatLongList = LatLng(MapsViewModel.RouteList!![i].Latitude, MapsViewModel.RouteList!![i].Longitude)

                countStudentList?.clear()
                val pickupDropPoint = MarkerOptions()

                for (j in 0 until MapsViewModel.StudentList!!.size) {

                    if (ISFORPICKUP == "true") {

                        if (MapsViewModel.RouteList!![i].PickupPointID == MapsViewModel.StudentList!![j].PickupPointID) {
                            countStudentList?.add(MapsViewModel.StudentList!![j])
                        }

                    } else if (ISFORPICKUP == "false") {
                        if (MapsViewModel.RouteList!![i].PickupPointID == MapsViewModel.StudentList!![j].DropPointID) {
                            countStudentList?.add(MapsViewModel.StudentList!![j])
                        }
                    }

                }


                if (i == 1) {
                    pickupDropPoint.title(MapsViewModel.RouteList!![i].PointName).snippet("Pickup Point").icon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.writeTextOnDrawable(this, R.drawable.marker_light_green, countStudentList?.size.toString())))
                } else if (i > 1 && i < MapsViewModel.RouteList!!.size) {
                    pickupDropPoint.title(MapsViewModel.RouteList!![i].PointName).snippet("Pickup Point").icon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.writeTextOnDrawable(this, R.drawable.marker_blue, countStudentList?.size.toString())))
                } else {
                    pickupDropPoint.title(MapsViewModel.RouteList!![i].PointName).snippet("Drop Point").icon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.writeTextOnDrawable(this, R.drawable.marker_red, countStudentList?.size.toString())))
                }

                pickupDropPoint.position(LatLongList)
                markerPoint = mMap.addMarker(LatLongList.let {
                    pickupDropPoint
                })

                markerPoint!!.tag = MapsViewModel.RouteList!![i].PickupPointID


                var cameraPosition = CameraPosition.Builder()
                        .target(LatLng(MapsViewModel.RouteList!![i].Latitude, MapsViewModel.RouteList!![i].Longitude)).zoom(13.5F).build()

                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition))

                mMap.setOnMarkerClickListener { marker ->
                    startActivity(StudentAttendanceListActivity.getInstance(this@MapsActivity2, marker.tag.toString(), marker.title))
                    true

                }
            }
        }


    }

    private fun showpath() {

        pathLatLngs = decodePoly(MapsViewModel.Polyline)
        val polyLineOptions = PolylineOptions()
        polyLineOptions.jointType(JointType.ROUND)
        polyLineOptions.startCap(RoundCap())
        polyLineOptions.endCap(RoundCap())
        polyLineOptions.addAll(pathLatLngs)
        polyLineOptions.width(8f)
        polyLineOptions.color(resources.getColor(R.color.blue))
        zoomToPolyline()

        mMap.addPolyline(polyLineOptions)
    }

    @Synchronized
    protected fun buildApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()
        googleApiClient!!.connect()
    }

    private fun checkPermissions(): Boolean {

        if (ContextCompat.checkSelfPermission(this,
                        permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        } else {
            requestPermissions()
            return false
        }

    }

    override fun onCloseTrip() {

        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient!!.removeLocationUpdates(locationCallback)

        }
        mapsViewModel.sendNotification()
        val database = FirebaseDatabase.getInstance()
        var latlongdatabasereference = database.reference.child(Controller.BusTripLogID)
        latlongdatabasereference.child("status").setValue("TripClose")
        startActivity(MapsActivity2.getInstance(this))
        finish()

    }

    override fun onMoveToCurrentLocation() {
        zoomToPolyline()
    }

    override fun drawPolyline() {

        if (map != null) {

            var countStudentList: ArrayList<GetRouteListModel.StudentInfoItem>? = ArrayList()

            showpath()
            // setMuliple Marker on Polyline and also display student list count for particular pickup point
            for (i in 0 until MapsViewModel.RouteList!!.size) {
                var LatLongList = LatLng(MapsViewModel.RouteList!![i].Latitude, MapsViewModel.RouteList!![i].Longitude)

                countStudentList?.clear()
                val pickupDropPoint = MarkerOptions()

                for (j in 0 until MapsViewModel.StudentList!!.size) {

                    if (ISFORPICKUP == "true") {

                        if (MapsViewModel.RouteList!![i].PickupPointID == MapsViewModel.StudentList!![j].PickupPointID) {
                            countStudentList?.add(MapsViewModel.StudentList!![j])
                        }

                    } else if (ISFORPICKUP == "false") {
                        if (MapsViewModel.RouteList!![i].PickupPointID == MapsViewModel.StudentList!![j].DropPointID) {
                            countStudentList?.add(MapsViewModel.StudentList!![j])
                        }
                    }

                }


                if (i == 1) {
                    pickupDropPoint.title(MapsViewModel.RouteList!![i].PointName).snippet("Pickup Point").icon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.writeTextOnDrawable(this, R.drawable.marker_light_green, countStudentList?.size.toString())))
                } else if (i > 1 && i < MapsViewModel.RouteList!!.size) {
                    pickupDropPoint.title(MapsViewModel.RouteList!![i].PointName).snippet("Pickup Point").icon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.writeTextOnDrawable(this, R.drawable.marker_blue, countStudentList?.size.toString())))
                } else {
                    pickupDropPoint.title(MapsViewModel.RouteList!![i].PointName).snippet("Drop Point").icon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.writeTextOnDrawable(this, R.drawable.marker_red, countStudentList?.size.toString())))
                }

                pickupDropPoint.position(LatLongList)
                markerPoint = mMap.addMarker(LatLongList.let {
                    pickupDropPoint
                })
//                marker = when (i) {
//                    0 -> {
//
//                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
//                    }
//
//                    MapsViewModel.RouteList!!.size - 1 -> {
//                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
//                    }
//                    else -> {
//                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
//                    }
//
//                }
//
//                markerPoint = mMap.addMarker(LatLongList.let {
//                    MarkerOptions()
//                            .position(it)
//                            .title(MapsViewModel.RouteList!![i].PointName)
//                            .icon(marker)
//
//                })
//                mMap.addMarker(pickupDropPoint)

                markerPoint!!.tag = MapsViewModel.RouteList!![i].PickupPointID


                var cameraPosition = CameraPosition.Builder()
                        .target(LatLng(MapsViewModel.RouteList!![i].Latitude, MapsViewModel.RouteList!![i].Longitude)).zoom(13.5F).build()

                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition))

                mMap.setOnMarkerClickListener { marker ->
                    startActivity(StudentAttendanceListActivity.getInstance(this@MapsActivity2, marker.tag.toString(), marker.title))
                    true

                }
            }
        }
    }

    private val callback: CancelableCallback = object : CancelableCallback {
        override fun onFinish() { //            if (!isFakeEventStarted) {
        }

        override fun onCancel() {}
    }

    private fun zoomToPolyline() {

        if (mMap == null || pathLatLngs == null || pathLatLngs!!.isEmpty()) return

        val builder = LatLngBounds.builder()

        for (latLng in pathLatLngs!!) {
            builder.include(latLng)
        }
        val bounds = builder.build()
        try {
            val width = resources.displayMetrics.widthPixels
            val height = (resources.displayMetrics.heightPixels * 0.55).toInt()
            val padding = (width * 0.08).toInt()
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
            mMap.animateCamera(cu, callback)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

    }

    override fun onPause() {
        super.onPause()
        if (mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient!!.removeLocationUpdates(locationCallback)
        }
    }


    override fun onStop() {
        super.onStop()
        isActivityForground = false
    }


    override fun onStart() {
        super.onStart()
        isActivityForground = true
    }

    override fun onResume() {
        super.onResume()
        if (Connectivity.isNetworkConnected(this)) {
            if (isPermissionGranted()) {
                if (Connectivity.isGpsEnable(locationManager)) {
                    if (googleApiClient != null && mFusedLocationProviderClient != null) {
                        if (Controller.isRunningTrip) {
                            requestLocationUpdates()
                        }
                    } else {
                        buildApiClient()
                    }

                } else {

                    val intent1 = Intent(this, LocationServiceActivity::class.java)
                    startActivity(intent1)
                    finish()
                    overridePendingTransition(0, 0)

                }

            } else {

                val intent1 = Intent(this, PermissionActivity::class.java)
                val extras = Bundle()
                //Adding key value pairs to this bundle
                //there are quite a lot data types you can store in a bundle
                extras.putString(resources.getString(R.string.busRouteid), BUSROUTEID)
                extras.putString(resources.getString(R.string.Routeid), ROUTEID)
                extras.putBoolean(resources.getString(R.string.isForPickup), isForpickup)
                extras.putString(resources.getString(R.string.currentdate), BUSROUTEDATE)
                extras.putString(resources.getString(R.string.deviceId), Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID))
                extras.putString(resources.getString(R.string.currenttime), STARTTIME)
                extras.putString(resources.getString(R.string.FromStr), "FromRouteSelection")
                startActivity(intent1)
                finish()
            }
        } else {
            Toaster.shortToast(this, resources.getString(R.string.checknwconnection))
        }
    }

    private fun isPermissionGranted(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, permission.ACCESS_COARSE_LOCATION)
        val result1 = ContextCompat.checkSelfPermission(applicationContext, permission.ACCESS_FINE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }


    fun onLocationChanged(location: Location) {

        this.location = location
        if (currentLongitude != location.longitude || currentLatitude != location.latitude) {
            currentLongitude = location.longitude
            currentLatitude = location.latitude
            currentlatLng = LatLng(location.latitude, location.longitude)

            latLng[0] = location.latitude
            latLng[1] = location.longitude

        }

        if (Controller.isRunningTrip) {

            val database = FirebaseDatabase.getInstance()
            var latlongdatabasereference = database.reference.child(Controller.BusTripLogID)
            val user = UpdateLiveLatLong("TripRunning", location?.latitude.toString(), location?.longitude.toString())
            latlongdatabasereference.setValue(user)

            Handler().postDelayed({
                mapsViewModel.updateLiveLatLong(location?.latitude, location?.longitude, Controller.BusTripLogID)
            }, FIREBASE_UPDATE_INTERVAL)


            if (currentlatLng != null) {

                moveBusMarker(LatLng(location?.latitude, location?.longitude))

            }

        }
        /*END*/
        Log.e("onLocationChnaged", "onLocationChanged: " + location.latitude + " " + location.longitude)

    }

    private fun moveBusMarker(latLng: LatLng) {

        if (busLocationMarker == null) {
            val busMarker = MarkerOptions()
            busMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup_map_icon))
            busMarker.title("Driver is here")
            busMarker.flat(true)
            busMarker.anchor(0.5f, 0.5f)
            busMarker.position(latLng)
            busLocationMarker = mMap.addMarker(busMarker)
        } else {
            MapUtil.animateMarker(busLocationMarker, latLng)
        }

    }


    var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            if (locationResult.lastLocation == null) {
                Log.d("LocationResult", "onLocationResult: " + "Location null")
            }
            for (location in locationResult.locations) {
                onLocationChanged(location)
            }
        }

    }

    private fun requestPermissions() {

        ActivityCompat.requestPermissions(this,
                arrayOf(permission.ACCESS_FINE_LOCATION),
                REQUEST_FINE_LOCATION)

    }

    private fun startAnim() {

        if (mMap != null) {
            bangaloreRoute?.let { MapAnimator.instance.animateRoute(mMap, it) }
        } else {
            Toast.makeText(applicationContext, "Map not ready", Toast.LENGTH_LONG).show()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == R.id.nav_route_list) {

            startActivity(RoutActivity.getInstance(this))

        } else if (id == R.id.nav_license) {

            startActivity(LDActivity.getInstance(this))

        } else if (id == R.id.nav_contactus) {

            startActivity(ContactusActivity.getInstance(this))

        } else if (id == R.id.nav_aboutus) {

            startActivity(AboutusActivity.getInstance(this))

        } else if (id == R.id.nav_tandc) {

            startActivity(TCActivity.getInstance(this))

        } else if (id == R.id.nav_logout) {

            showLogoutDialog(this)

        }

        closeMenu()
        return true

    }

    fun showLogoutDialog(context: Context) {

        AlertDialog.Builder(context).setIcon(R.mipmap.ic_launcher)
                .setTitle("Logout")
                .setMessage("Are you sure want to logout?")
                .setPositiveButton("Yes") { dialog, _ ->
                    startActivity(SplashActivity.getInstance(this))
                    dialog.dismiss()
                    AppPreference.setUserId("")
                    AppPreference.setLogin(false)
                    finishAffinity()
                }.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }.show()

    }

    override fun onDrawerStateChanged(newState: Int) {
        if (newState == DrawerLayout.STATE_SETTLING) {
            if (!drawer_layout.isDrawerOpen(GravityCompat.START)) {
                // Drawer started opening
                openMenu()
            } else {
                // Drawer started closing
            }
        }

    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

    }

    override fun onDrawerClosed(drawerView: View) {

    }

    override fun onDrawerOpened(drawerView: View) {

    }

    override fun onNotificationClick() {

    }

    override fun onTripDetailsClick() {
//        showStudentAttendancePupUp()
        startActivity(TripDetailsActivity.getInstance(this))
    }

    override fun onLiveLocationClick() {

    }

    override fun onMenuClick() {
        openMenu()
    }

    override fun onStatusClick() {

    }

    override fun onDirectionFound(directionsResModel: DirectionsResModel) {

        if (directionsResModel.routes != null && directionsResModel.routes.size > 0) {
            val route = directionsResModel.routes[0]

            if (route.legs != null) {
                for (i in 0 until route.legs.size) {
                    val leg = route.legs[i]
                    if (leg.steps != null) {
                        for (j in 0 until leg.steps.size) {
                            val step = leg.steps[j]

                            val polyline = step.polyline
                            if (polyline != null) {
                                val list = decodePoly(polyline.points)
                                for (l in list.indices) {
                                    bangaloreRoute!!.add(LatLng(list[l].latitude, list[l].longitude))
                                }

                            }
                        }
                    }
                }
            }
            startAnim()
        }
    }

    override fun onErrorOccur(error: String) {
        Toaster.shortToast(this, error)
    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }

    fun openMenu() {
        if (!drawer_layout.isDrawerOpen(nav_view)) {
            drawer_layout.openDrawer(nav_view)
            //TODO channge url
            //setMenuProfileData()
        } else {
            drawer_layout.closeDrawer(nav_view)
        }

    }

    fun closeMenu() {
        if (drawer_layout.isDrawerOpen(nav_view)) {
            drawer_layout.closeDrawer(nav_view)
        }
    }

    override fun onStartTrip() {

        if (googleApiClient != null && mFusedLocationProviderClient != null) {
            if (Controller.isRunningTrip) {
                requestLocationUpdates()
                mapsViewModel.sendNotification()
            }
        } else {
            buildApiClient()
        }

        tv_btn_start_trip.visibility = View.GONE
        tv_btn_end_trip.visibility = View.VISIBLE
        img_menu.visibility = View.GONE

    }

    @Override
    override fun onBackPressed() {

        if (Controller.isRunningTrip) {

        } else {
            super.onBackPressed()
        }

    }

    override fun onConnected(p0: Bundle?) {
        Log.d("connected", "onConnected: ")
        if (Controller.isRunningTrip) {
            requestLocationUpdates()
            getLastLocation()
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
            return
        }
        mFusedLocationProviderClient!!.lastLocation
                .addOnSuccessListener { location ->
                    // GPS location can be null if GPS is switched off
                    location?.let { onLocationChanged(it) }
                            ?: Log.d("OnSuccess", "onSuccess: " + "location null")
                }
                .addOnFailureListener { e ->
                    Log.e("OnSuccess", "Error trying to get last GPS location")
                    e.printStackTrace()
                }
    }

    private fun requestLocationUpdates() {

        locationRequest = LocationRequest()
        locationRequest!!.interval = 1 * 1000.toLong()
        locationRequest!!.fastestInterval = 1000
        locationRequest!!.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest!!)
        //**************************
        builder.setAlwaysShow(true) //this is the key ingredient
        //**************************
        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
            return
        }
        try {
            mFusedLocationProviderClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.d("connectionSuspended", "onConnectionSuspended: ")
        googleApiClient!!.connect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onGpsStatusChanged(event: Int) {
        when (event) {
            GpsStatus.GPS_EVENT_STARTED -> Log.e("GPS_EVENT_STARTED", "onGpsStatusChanged started")
            GpsStatus.GPS_EVENT_STOPPED -> {
                Log.e("GPS_EVENT_STOPPED", "onGpsStatusChanged stopped")
                if (Connectivity.isGpsEnable(locationManager)) {
                } else {
                    if (isActivityForground) {
                        val intent = Intent(this, LocationServiceActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
            GpsStatus.GPS_EVENT_FIRST_FIX -> Log.e("GPS_EVENT_FIRST_FIX", "onGpsStatusChanged first fix")
            GpsStatus.GPS_EVENT_SATELLITE_STATUS -> Log.e("GPS_SATELLITE_STATUS", "onGpsStatusChanged status")
        }
    }

}
