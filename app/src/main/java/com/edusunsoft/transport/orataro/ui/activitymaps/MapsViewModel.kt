package com.edusunsoft.transport.orataro.ui.activitymaps

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseViewModel
import com.edusunsoft.transport.orataro.dialog.LoadingDialog
import com.edusunsoft.transport.orataro.network.ApiProvider
import com.edusunsoft.transport.orataro.network.ApiRequest
import com.edusunsoft.transport.orataro.network.GetDirectionInterface
import com.edusunsoft.transport.orataro.network.NetworkCallBack
import com.edusunsoft.transport.orataro.ui.activitylogin.LoginActivity
import com.edusunsoft.transport.orataro.utils.AppPreference
import com.edusunsoft.transport.orataro.utils.Controller
import com.edusunsoft.transport.orataro.utils.Toaster
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MapsViewModel(val mapsNavigator: MapsNavigator, context: Context) :
        BaseViewModel<MapsNavigator>(mapsNavigator, context), NetworkCallBack {

    companion object {

        //jsonobject for get gcmid string array
        var jsonobj: JSONObject? = null
        var GCMIDS:String = ""
        // variable declaration for jsonbejct to take attendance
        lateinit var jsonobjfornotification: RequestBody

        lateinit var model: GetRouteListReqModel
        lateinit var insertBusTripReqModel: InsertBusTripReqModel
        lateinit var closeTripReqModel: CloseTripReqModel
        var BusTripLogID: String = ""
        var FlagFrom: String = ""
        var RouteList: ArrayList<GetRouteListModel.RouteItem>? = ArrayList()
        var StudentList: ArrayList<GetRouteListModel.StudentInfoItem>? = ArrayList()
        var markerLatLng: LatLng? = null
        var Polyline: String = ""

        // request list for send notification to parent on trip start and close
        var NotificationIdList: ArrayList<String>? = ArrayList()

    }


    init {
        getRoutListForMap()
    }

    private fun getRoutListForMap() {

//        LoadingDialog.showLoading(context)
        model = GetRouteListReqModel(MapsActivity2.BUSROUTEID)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, false).apiRequest(
                    ApiProvider.provideApi(context).GetRoute(model),
                    this@MapsViewModel
            )

        }

    }

    override fun onSuccess(response: Any?) {

        if (response is GetRouteListModel) {

            when {

                response.status_code == 0 -> {
                    LoadingDialog.hideLoading()
                    val message = response.message
                    Toaster.showShortToast(context, message)
                }

                response.status_code == 1 -> {

                    Polyline = ""
                    LoadingDialog.hideLoading()
                    RouteList = response.data.Route
                    StudentList = response.data.StudentInfo
                    Polyline = response.data.polyline

                    mapsNavigator.drawPolyline()

                    GCMIDS = ""
                    for (k in 0 until StudentList!!.size){
                        GCMIDS += response.data.StudentInfo[k].GCMID
                    }

                    for (i in 0 until RouteList!!.size) {

                        markerLatLng = GetLatLong(RouteList!![i].Latitude, RouteList!![i].Longitude)

                        for (j in 0 until StudentList!!.size) {

                            if (MapsActivity2.ISFORPICKUP == context.resources.getString(R.string.True)) {

                                if (RouteList!![i].PickupPointID == StudentList!![j].PickupPointID) {

                                    var totalstudentcount = StudentList!![j]

                                }

                            } else if (MapsActivity2.ISFORPICKUP == context.resources.getString(R.string.False)) {

                                if (RouteList!![i].PickupPointID == StudentList!![j].DropPointID) {

                                    var totalstudentcount = StudentList!!.size

                                }

                            }

                        }

                    }

                }

                response.status_code == 2 -> {

                    val message = response.message
                    Toaster.showShortToast(context, message)
                    LoadingDialog.hideLoading()
                    AppPreference.setUserId("")
                    AppPreference.setLogin(false)
                    mapsNavigator.RedirectToLogin()

                }

                else -> {

                    LoadingDialog.hideLoading()

                }

            }

        } else if (response is InsertBusTripResponseModel) {

            when (response.status_code) {
                0 -> {

                    val message = response.message
                    Toaster.showShortToast(context, message)
                    LoadingDialog.hideLoading()

                }
                1 -> {


                    LoadingDialog.hideLoading()
                    val message = response.message
                    Toaster.showShortToast(context, message)
                    Controller.BusTripLogID = response.data.BusTripLog.BusTripLogID

                    if (FlagFrom == "StartTrip") {
                        Controller.isRunningTrip = true
                        mapsNavigator.onStartTrip()
                    } else {
                        Controller.isRunningTrip = false
                        mapsNavigator.onCloseTrip()
                    }

                }
                2 -> {

                    val message = response.message
                    Toaster.showShortToast(context, message)
                    LoadingDialog.hideLoading()
                    Controller.RedirectToLoginActivity(context)

                }
                else -> {

                    Toaster.showShortToast(context, context.resources.getString(R.string.warningmesage))

                }
            }

        } else if (response is UpdateLatLongResModel) {
            when (response.status_code) {
                0 -> {
                    //                    Toaster.showShortToast(context, message)
                    LoadingDialog.hideLoading()

                }
                1 -> {

                    LoadingDialog.hideLoading()
                    val message = response.message
                    //                    Toaster.showShortToast(context, message)

                }
                2 -> {
                    //                    Toaster.showShortToast(context, message)
                    LoadingDialog.hideLoading()
                    Controller.RedirectToLoginActivity(context)

                }

                else -> {

                    LoadingDialog.hideLoading()
                    Toaster.showShortToast(context, context.resources.getString(R.string.warningmesage))

                }
            }
        }

    }

    private fun GetLatLong(latitude: Double, longitude: Double): LatLng? {

        return if (!latitude.equals(null) && !longitude.equals(null)) {
            LatLng(latitude, longitude)
        } else null

    }

    override fun onError(errorBody: String) {

        Toaster.showShortToast(context, errorBody)
        AppPreference.setUserId("")
        AppPreference.setLogin(false)
        Intent(context, LoginActivity::class.java)

    }

    fun onNotificationClick() {
        mapsNavigator.onNotificationClick()
    }

    fun onTripDetailsClick() {
        mapsNavigator.onTripDetailsClick()
    }

    fun onLiveLocationClick() {
        mapsNavigator.onLiveLocationClick()
    }

    fun onMenuClick() {
        mapsNavigator.onMenuClick()
    }

    fun onStatusClick() {
        mapsNavigator.onStatusClick()
    }

    fun onBackButtonClick() {
        mapsNavigator.onBackButtonClick()
    }

    fun onMoveToCurrentLocation() {
        mapsNavigator.onMoveToCurrentLocation()
    }

    fun onStartTrip() {

        AlertDialog.Builder(context).setIcon(R.mipmap.ic_launcher)
                .setTitle(context.resources.getString(R.string.starttrip))
                .setMessage(context.resources.getString(R.string.starttripwarningdialog))
                .setPositiveButton(context.resources.getString(R.string.yes)) { dialog, _ ->
                    FlagFrom = "StartTrip"
                    LoadingDialog.showLoading(context)
                    insertBusTripReqModel = InsertBusTripReqModel(MapsActivity2.BUSROUTEID, MapsActivity2.ROUTEID, MapsActivity2.ISFORPICKUP, MapsActivity2.BUSROUTEDATE, MapsActivity2.DEVICEID, MapsActivity2.STARTTIME)
                    GlobalScope.launch(Dispatchers.Main) {
                        ApiRequest(context, false).apiRequest(
                                ApiProvider.provideApi(context).InsertBusTrip(insertBusTripReqModel),
                                this@MapsViewModel
                        )

                    }

                }.setNegativeButton(context.resources.getString(R.string.no)) { dialog, _ ->
                    dialog.dismiss()
                }.show()


    }

    fun onCloseTrip() {

        AlertDialog.Builder(context).setIcon(R.mipmap.ic_launcher)
                .setTitle(context.resources.getString(R.string.closetrip))
                .setMessage(context.resources.getString(R.string.closetripwarningdialog))
                .setPositiveButton(context.resources.getString(R.string.yes)) { dialog, _ ->
                    FlagFrom = "EndTrip"
                    LoadingDialog.showLoading(context)
                    closeTripReqModel = CloseTripReqModel(Controller.BusTripLogID, Controller.GetCurrentTime())
                    GlobalScope.launch(Dispatchers.Main) {
                        ApiRequest(context, false).apiRequest(
                                ApiProvider.provideApi(context).CloseBusTrip(closeTripReqModel),
                                this@MapsViewModel
                        )

                    }

                }.setNegativeButton(context.resources.getString(R.string.no)) { dialog, _ ->
                    dialog.dismiss()
                }.show()


    }

    fun findDirection(fromLatLng: LatLng, pWaypoints: LatLng, toLatLng: LatLng) {

        val fromLat: String = fromLatLng.latitude.toString()
        val fromLong: String = fromLatLng.longitude.toString()
        val toLat: String = toLatLng.latitude.toString()
        val toLong: String = toLatLng.longitude.toString()
        val waypointsLat: String = pWaypoints.latitude.toString()
        val waypointsLong: String = pWaypoints.longitude.toString()

        val origin = "$fromLat, $fromLong"
        val destination = "$toLat, $toLong"
        val waypoints = "$waypointsLat, $waypointsLong"

        val retrofit = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val apiService = retrofit.create(GetDirectionInterface::class.java)

        val call: Call<DirectionsResModel> = apiService.getDirections(origin, destination, waypoints, false, "driving", "AIzaSyBny1MZbF2VX62RkgpFC66vvZE6iqzSzOY", true)

        call.enqueue(object : Callback<DirectionsResModel> {

            override fun onResponse(call: Call<DirectionsResModel>, response: Response<DirectionsResModel>) {

                if (response.isSuccessful) {
                    response.body()?.let { mapsNavigator.onDirectionFound(it) }
                } else {
                    mapsNavigator.onErrorOccur(response.message())
                }

            }

            override fun onFailure(call: Call<DirectionsResModel>, t: Throwable) {
                mapsNavigator.onErrorOccur("Somethings went wrong happens")
                t.printStackTrace()
            }

        })

    }

    fun updateLiveLatLong(latitude: Double, longitude: Double, busTripLogID: String) {

        getCurrentDate()
        val livelatlongModel = UpdateLiveLatLongReqModel(latitude, longitude, getCurrentDate(), busTripLogID, Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))

        try {
            GlobalScope.launch(Dispatchers.Main) {
                ApiRequest(context, false).apiRequest(
                        ApiProvider.provideApi(context).UpdateLiveLatLong(livelatlongModel),
                        this@MapsViewModel
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun getCurrentDate(): String {

        val c = Calendar.getInstance().time

        val CurrntDate = SimpleDateFormat("yyyy-MM-dd")
        return CurrntDate.format(c)

    }

    fun sendNotification() {
        jsonobjfornotification = GCMIDS.let { Controller.jsonRequestfornotification(it, Controller.BUSROUTENAME + " " + Controller.VEHICLEREGNO) }
        try {
            GlobalScope.launch(Dispatchers.Main) {
                ApiRequest(context, false).apiRequest(
                        ApiProvider.provideApi(context).sendNotification(jsonobjfornotification), this@MapsViewModel

                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
