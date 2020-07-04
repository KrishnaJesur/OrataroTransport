package com.edusunsoft.transport.orataro.ui.activitymaps

import android.Manifest.permission
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseActivity
import com.edusunsoft.transport.orataro.databinding.ActivityMapsBinding
import com.edusunsoft.transport.orataro.dialog.studentattendance.RecyclerviewCallbacks
import com.edusunsoft.transport.orataro.dialog.studentattendance.StudentAttendanceAdapter
import com.edusunsoft.transport.orataro.ui.activityaboutus.AboutusActivity
import com.edusunsoft.transport.orataro.ui.activitycontactus.ContactusActivity
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
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.FirebaseDatabase
import com.google.maps.android.PolyUtil
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.nav_header_dashboard.view.*


class MapsActivity : BaseActivity(), OnMapReadyCallback, MapsNavigator, DrawerLayout.DrawerListener, NavigationView.OnNavigationItemSelectedListener {


    override fun RedirectToLogin() {
        startActivity(LoginActivity.getInstance(this@MapsActivity))
        finish()
    }

    override fun onBackButtonClick() {
        this.finish()
    }


    private val TAG: String = MapsActivity::class.java.simpleName
    lateinit var drawer_layout: DrawerLayout
    private lateinit var binding: ActivityMapsBinding
    private val POINT_A = LatLng(22.9952, 72.6041) //MANINAGAR
    private val POINT_B = LatLng(23.0387, 72.6308) // BAPUNAGAR
    private val POINT_C = LatLng(23.0459, 72.6697) // NIKOL
    private var bangaloreRoute: MutableList<LatLng>? = ArrayList()
    private lateinit var mapsViewModel: MapsViewModel
    lateinit var marker: BitmapDescriptor


    // object declaration for draw polyline
    var lineOptions: PolylineOptions? = null
    private var blackPolyLine: Polyline? = null
    private val decodedPath: List<LatLng>? = null
    var markerLatLng: LatLng? = null
    var hashMap: HashMap<Marker, String> = HashMap<Marker, String>()

    var context: Context? = null

    // Variables to get Current Location
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var googleApiClient: GoogleApiClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationManager: LocationManager? = null
    private var location: Location? = null
    private var currentLongitude: Double = 0.0
    private var currentLatitude = 0.0
    private var currentlatLng: LatLng? = null

    // variable for activity is in background or foreground
    private var isActivityForground: Boolean = false

    private val UPDATE_INTERVAL: Long = 1 * 1000  /* 5 secs */
    private val FASTEST_INTERVAL: Long = 1000 /* 2 sec */
    val REQUEST_FINE_LOCATION: Int = 101
    private val ERROR_DIALOG_REQUEST = 9001


    companion object {

        const val EXTRA_START_LAT = "start_lat"
        const val EXTRA_START_LONG = "start_long"
        const val EXTRA_STOP_LAT = "stop_lat"
        const val EXTRA_STOP_LONG = "stop_long"

        // variables for InsertBusTrip
        var BUSROUTEID: String = ""
        var ROUTEID: String = ""
        var ISFORPICKUP: String = ""
        var BUSROUTEDATE: String = ""
        var DEVICEID: String = ""
        var STARTTIME: String = ""
        var FROSTR: String = ""


        fun getInstance(selectRoutActivity: SelectRoutActivity, startLat: Double, startLong: Double, stopLat: Double, stopLong: Double): Intent {
            val intent = Intent(selectRoutActivity, MapsActivity::class.java)
            intent.putExtra(EXTRA_START_LAT, startLat)
            intent.putExtra(EXTRA_START_LONG, startLong)
            intent.putExtra(EXTRA_STOP_LAT, stopLat)
            intent.putExtra(EXTRA_STOP_LONG, stopLong)
            return intent
        }

        const val EXTRA_TRIP_ID = "trip_id"
        const val EXTRA_DRIVER_ID = "driver_id"

        fun getInstance(selectRoutActivity: SelectRoutActivity, tripId: String, driverId: String): Intent {
            val intent = Intent(selectRoutActivity, MapsActivity::class.java)
            intent.putExtra(EXTRA_TRIP_ID, tripId)
            intent.putExtra(EXTRA_DRIVER_ID, driverId)
            return intent
        }

        fun getInstance(selectRoutActivity: SelectRoutActivity, BUSROUTEID: String): Intent? {
            val intent = Intent(selectRoutActivity, MapsActivity::class.java)
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

            if (FROSTR.equals("FromRunningTrip")) {
                Controller.isRunningTrip = true
                tv_btn_end_trip.visibility = View.VISIBLE
                tv_btn_start_trip.visibility = View.GONE
                img_menu.visibility = View.GONE
            }

            val isForpickup = bundle.getBoolean(resources.getString(R.string.isForPickup))

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

        initialization()

        mapsViewModel = MapsViewModel(this, this)
        binding.mMapsViewModel = mapsViewModel

        drawer_layout = binding.drawerLayout
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

//        GlobalScope1.launch(Dispatchers.Main) {
//            mapsViewModel.findDirection(POINT_A, POINT_B, POINT_C)
//        }

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
        if (available == ConnectionResult.SUCCESS) { //everything is fine and the user can make map requests
            Log.d("isServiceWorking", "isServicesOK: Google Play Services is working")
            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) { //an error occured but we can resolve it
            Log.d("errorOccured", "isServicesOK: an error occured but we can fix it")
            val dialog = GoogleApiAvailability.getInstance().getErrorDialog(this, available, ERROR_DIALOG_REQUEST)
            dialog.show()
        } else {
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
            mMap.isMyLocationEnabled = true
        }

        if (Controller.isRunningTrip) {
            startLocationUpdates()
        }

        //TODO need to check draw map or not. or HIDE progress bar.
        // prepare poly line
        //TODO need to check draw map or not. or HIDE progress bar.
        // prepare poly line


        mMap.setOnMapLoadedCallback {

            lineOptions = PolylineOptions()
                    .addAll(PolyUtil.decode(MapsViewModel.Polyline))
                    .width(resources.getDimension(R.dimen._2sdp))
                    .endCap(RoundCap())
                    .startCap(RoundCap())
                    .jointType(JointType.ROUND)
                    .color(resources.getColor(R.color.blue))

            blackPolyLine = mMap.addPolyline(lineOptions)

            // setMuliple Marker on Polyline
            for (i in 0 until MapsViewModel.RouteList!!.size) {

                var markerPoint: Marker? = null

                var LatLongList = LatLng(MapsViewModel.RouteList!![i].Latitude, MapsViewModel.RouteList!![i].Longitude)

                marker = when (i) {

                    0 -> {
//                        val iconFactory = new IconGenerator (this)
//                        BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("Your text"))).
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                    }

                    MapsViewModel.RouteList!!.size - 1 -> {
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                    }

                    else -> {
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                    }

                }

                markerPoint = mMap.addMarker(LatLongList.let {
                    MarkerOptions()
                            .position(it)
                            .title(MapsViewModel.RouteList!![i].PointName)
                            .icon(marker)

                })

                markerPoint.showInfoWindow()
                markerPoint.tag = MapsViewModel.RouteList!![i].PickupPointID


                var cameraPosition = CameraPosition.Builder()
                        .target(LatLng(MapsViewModel.RouteList!![i].Latitude, MapsViewModel.RouteList!![i].Longitude)).zoom(13.5F).build()

                mMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(cameraPosition))

                mMap.setOnMarkerClickListener { marker ->
                    startActivity(StudentAttendanceListActivity.getInstance(this@MapsActivity, marker.tag.toString(), marker.title))
                    false
                }
            }

        }


    }

    override fun drawPolyline() {

        if (map != null) {

//            showpath()

        }
    }

    private fun startLocationUpdates() {

        locationRequest = LocationRequest()
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest!!.interval = UPDATE_INTERVAL
        locationRequest!!.fastestInterval = FASTEST_INTERVAL

        // Create LocationSettingsRequest object using location request
        var builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest!!)
        var locationSettingsRequest = builder.build()

        // Check whether location settings are satisfied
        var settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        try {
            // Get last known recent location using new Google Play Services SDK (v11+)
            mFusedLocationProviderClient = getFusedLocationProviderClient(this)
            mFusedLocationProviderClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
        val database = FirebaseDatabase.getInstance()
        var latlongdatabasereference = database.reference.child(Controller.BusTripLogID)
        latlongdatabasereference.child("status").setValue("TripClosed")
        finish()

    }

    override fun onMoveToCurrentLocation() {

    }


    override fun onPause() {
        super.onPause()

        if (!isActivityForground) {

            startLocationUpdates()

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
        isActivityForground = true
    }


    fun onLocationChanged(location: Location) {

        this.location = location
        if (currentLongitude != location.longitude || currentLatitude != location.latitude) {
            currentLongitude = location.longitude
            currentLatitude = location.latitude
            currentlatLng = LatLng(location.latitude, location.longitude)
        }

        if (Controller.isRunningTrip) {

            val database = FirebaseDatabase.getInstance()
            var latlongdatabasereference = database.reference.child(Controller.BusTripLogID)
            val user = UpdateLiveLatLong("TripRunning", location?.latitude.toString(), location?.longitude.toString())
            latlongdatabasereference.setValue(user)

        }


        /*END*/
        Log.e("onLocationChnaged", "onLocationChanged: " + location.latitude + " " + location.longitude)

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

    fun resetAnimation(view: View) {
        startAnim()
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

    fun enableMenu() {
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    fun disableMenu() {
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun showStudentAttendancePupUp() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.pop_up_student_attendance)
        val window: Window? = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawableResource(R.color.transparent)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(recyclerView.context, DividerItemDecoration.VERTICAL))
        val adapter = StudentAttendanceAdapter(this)
        adapter.addItemList(getFilterItems())
        recyclerView.adapter = adapter
        adapter.setOnClick(object : RecyclerviewCallbacks<String> {
            override fun onItemClick(position: Int, item: String, isChecked: Boolean) {
                Toaster.shortToast(this@MapsActivity, "$item $isChecked")
            }
        })

        dialog.show()
    }

    private fun getFilterItems(): List<String> {
        val filterItemList = mutableListOf<String>()
        filterItemList.add("Student name 1")
        filterItemList.add("Student name 2")
        filterItemList.add("Student name 3")
        filterItemList.add("Student name 4")
        filterItemList.add("Student name 5")
        filterItemList.add("Student name 6")
        filterItemList.add("Student name 11")
        filterItemList.add("Student name 12")
        filterItemList.add("Student name 13")
        filterItemList.add("Student name 14")
        filterItemList.add("Student name 15")
        filterItemList.add("Student name 16")
        filterItemList.add("Student name 21")
        filterItemList.add("Student name 22")
        filterItemList.add("Student name 23")
        filterItemList.add("Student name 24")
        filterItemList.add("Student name 25")
        filterItemList.add("Student name 26")
        return filterItemList
    }

    override fun onStartTrip() {

        startLocationUpdates()

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

}
