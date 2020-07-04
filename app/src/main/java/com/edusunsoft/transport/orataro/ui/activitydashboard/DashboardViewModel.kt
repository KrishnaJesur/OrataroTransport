package com.edusunsoft.transport.orataro.ui.activitydashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseViewModel
import com.edusunsoft.transport.orataro.dialog.LoadingDialog
import com.edusunsoft.transport.orataro.network.ApiProvider
import com.edusunsoft.transport.orataro.network.ApiRequest
import com.edusunsoft.transport.orataro.network.NetworkCallBack
import com.edusunsoft.transport.orataro.ui.activitylogin.LoginActivity
import com.edusunsoft.transport.orataro.ui.activitymaps.*
import com.edusunsoft.transport.orataro.utils.AppPreference
import com.edusunsoft.transport.orataro.utils.Controller
import com.edusunsoft.transport.orataro.utils.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DashboardViewModel(private var dashboardNavigator: DashboardNavigator, context: Context)
    : BaseViewModel<DashboardNavigator>(dashboardNavigator, context), NetworkCallBack {

    var CurrentDate: String = ""
    var CurrentTime: String = ""

    fun GetCurrentDate() {

        val c = Calendar.getInstance().getTime()

        val CurrntDate = SimpleDateFormat("yyyy-MM-dd")
        CurrentDate = CurrntDate.format(c)

        val curenttime = SimpleDateFormat("hh:mm")
        CurrentTime = curenttime.format(c)

    }

    override fun onSuccess(response: Any?) {

        if (response is CheckRunningbBusTripResModel) {

            when {

                response.status_code == 0 -> {
                    LoadingDialog.hideLoading()
                }

                response.status_code == 1 -> {

                    Log.d("getbusrunningtrip", response.data.Route.toString())
                    Log.d("getbusrunningtrip123", response.data.StudentInfo.toString())
                    Controller.isRunningTrip = true
                    GetCurrentDate()
                    LoadingDialog.hideLoading()
                    val message = response.message
                    Toaster.showShortToast(context, message)
                    //create a Bundle object
                    val extras = Bundle()
                    //Adding key value pairs to this bundle
                    //there are quite a lot data types you can store in a bundle
                    Controller.BusTripLogID = response.data.RunningBusTrip.BusTripLogID
                    extras.putString(context.resources.getString(R.string.busRouteid), response.data.RunningBusTrip.BusRouteID)
                    extras.putString(context.resources.getString(R.string.Routeid), response.data.RunningBusTrip.RouteID)
                    extras.putBoolean(context.resources.getString(R.string.isForPickup), response.data.RunningBusTrip.IsForPickup)
                    extras.putString(context.resources.getString(R.string.currentdate), CurrentDate)
                    extras.putString(context.resources.getString(R.string.deviceId), Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID))
                    extras.putString(context.resources.getString(R.string.currenttime), CurrentTime)
                    extras.putString(context.resources.getString(R.string.FromStr), "FromRunningTrip")
                    val intent = Intent(context, MapsActivity2::class.java)
                    intent.putExtras(extras)
                    context.startActivity(intent)

                }

                response.status_code == 2 -> {

                    val message = response.message
                    Toaster.showShortToast(context, message)
                    LoadingDialog.hideLoading()
                    AppPreference.saveAccessToken(context, "")
                    Controller.RedirectToLoginActivity(context)

                }

            }

        }

    }

    override fun onError(errorBody: String) {

        Toaster.showShortToast(context, errorBody)
        AppPreference.setUserId("")
        AppPreference.setLogin(false)
        Intent(context, LoginActivity::class.java)
    }

    init {

        CheckRunningTrip()

    }

    private fun CheckRunningTrip() {

        LoadingDialog.showLoading(context)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, false).apiRequest(
                    ApiProvider.provideApi(context).CheckRunningBusTrip(),
                    this@DashboardViewModel
            )

        }

    }

    fun onRouteListClick() {
        dashboardNavigator.onRouteListClick()
    }

    fun SendInstantMessages() {
        dashboardNavigator.getInstantMessages()
    }

    fun LicenseAndDocuments() {
        dashboardNavigator.getLicenseAndDocuments()
    }

    fun ContactUs() {
        dashboardNavigator.contactus()
    }

    fun AboutUs() {
        dashboardNavigator.aboutus()
    }

    fun MyProfile() {
        dashboardNavigator.myProfile()
    }
}