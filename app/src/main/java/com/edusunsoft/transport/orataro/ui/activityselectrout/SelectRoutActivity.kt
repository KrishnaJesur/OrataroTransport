package com.edusunsoft.transport.orataro.ui.activityselectrout

import android.app.Activity
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseActivity
import com.edusunsoft.transport.orataro.databinding.ActivitySelectRoutBinding
import com.edusunsoft.transport.orataro.helper.LocationHelper
import com.edusunsoft.transport.orataro.ui.activitylogin.LoginActivity
import com.edusunsoft.transport.orataro.ui.activitydashboard.DashboardActivity
import com.edusunsoft.transport.orataro.ui.activitysplash.SplashActivity
import com.edusunsoft.transport.orataro.utils.REQUEST_CODE_ENABLE_GPS
import com.google.android.gms.location.LocationSettingsStates
import android.provider.Settings
import android.view.View
import com.edusunsoft.transport.orataro.ui.activityinstantmessage.StudentListActivity
import com.edusunsoft.transport.orataro.ui.activitymaps.MapsActivity2
import com.edusunsoft.transport.orataro.utils.Controller
import kotlinx.android.synthetic.main.activity_select_rout.view.*
import kotlinx.android.synthetic.main.no_data_found_layout.view.*
import java.text.SimpleDateFormat
import java.util.*


class SelectRoutActivity : BaseActivity(), SelectRoutNavigator {


    private val mTag = SelectRoutActivity::class.java.simpleName

    private lateinit var activitySelectRoutBinding: ActivitySelectRoutBinding
    private lateinit var selectRoutViewModel: SelectRoutViewModel

    private var CurrentDate: String = ""
    private var CurrentTime: String = ""

    override fun GetCurrentDate() {

        val c = Calendar.getInstance().getTime()

        val CurrntDate = SimpleDateFormat("yyyy-MM-dd")
        CurrentDate = CurrntDate.format(c)

        val curenttime = SimpleDateFormat("hh:mm")
        CurrentTime = curenttime.format(c)

    }

    override fun RedirectToLogin() {
        startActivity(LoginActivity.getInstance(this@SelectRoutActivity))
        finish()
    }

    override fun onRoutItemClick(rout: SelectRouteResponseModel.RouteItem) {
        if (Controller.FROMSTRING.equals("InstantMessages")) {
            val intent = Intent(this, StudentListActivity::class.java)
            intent.putExtra(resources.getString(R.string.busRouteid), rout.BusRouteID)
            startActivity(intent)
//            finish()
        } else {
            //create a Bundle object
            val extras = Bundle()
            //Adding key value pairs to this bundle
            //there are quite a lot data types you can store in a bundle
            Log.d("getbusroutename",rout.BusRouteName)
            Controller.BUSROUTEID = rout.BusRouteID
            Controller.BUSROUTENAME = rout.BusRouteName
            Controller.VEHICLEREGNO = rout.VehicleRegNo
            extras.putString(resources.getString(R.string.busRouteid), rout.BusRouteID)
            extras.putString(resources.getString(R.string.busRoutename), rout.BusRouteName)
            extras.putString(resources.getString(R.string.vehicleregno), rout.VehicleRegNo)
            extras.putString(resources.getString(R.string.Routeid), rout.RouteID)
            extras.putBoolean(resources.getString(R.string.isForPickup), rout.IsForPickup)
            extras.putString(resources.getString(R.string.currentdate), CurrentDate)
            extras.putString(resources.getString(R.string.deviceId), Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID))
            extras.putString(resources.getString(R.string.currenttime), CurrentTime)
            extras.putString(resources.getString(R.string.FromStr), "FromRouteSelection")
            val intent = Intent(this, MapsActivity2::class.java)
            intent.putExtras(extras)
            startActivity(intent)
        }

    }

    override fun onRouteGetList(selectRouteResponseModel: SelectRouteResponseModel) {

        if (selectRouteResponseModel.data.route.isNotEmpty()) {
            selectRoutViewModel.routListAdapter.setData(selectRouteResponseModel.data.route)
        } else {
            activitySelectRoutBinding.lylNoDataFound.txt_message.text = this.resources.getString(R.string.noroutesavailable)
            selectRoutViewModel.isShow.set(true)
        }

    }

    companion object {

        fun getInstance(splashActivity: SplashActivity): Intent {
            return Intent(splashActivity, DashboardActivity::class.java)
        }

        fun getInstance(loginActivity: LoginActivity): Intent {
            return Intent(loginActivity, DashboardActivity::class.java)
        }

        fun getInstance(dashboardActivity: DashboardActivity): Intent? {
            return Intent(dashboardActivity, SelectRoutActivity::class.java)
        }

        fun getInstance(mapsActivity2: MapsActivity2): Intent? {
            return Intent(mapsActivity2, SelectRoutActivity::class.java)
        }

        fun getInstance(studentListActivity: StudentListActivity): Intent? {
            return Intent(studentListActivity, SelectRoutActivity::class.java)
        }

        fun getInstance(selectRoutActivity: SelectRoutActivity): Intent? {
            return Intent(selectRoutActivity, DashboardActivity::class.java)
        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        activitySelectRoutBinding = DataBindingUtil.setContentView(this, R.layout.activity_select_rout)
        selectRoutViewModel = SelectRoutViewModel(this, this)
        activitySelectRoutBinding.toolbar.iv_back.setOnClickListener { view ->

            startActivity(SelectRoutActivity.getInstance(this))
            this.finish()

        }

        if (Controller.FROMSTRING.equals("InstantMessages")) {
            activitySelectRoutBinding.toolbar.tv_activity_title.text = getString(R.string.select_route)
        } else {
            activitySelectRoutBinding.toolbar.tv_activity_title.text = getString(R.string.route_list)
        }

        activitySelectRoutBinding.mSelectRoutViewModel = selectRoutViewModel

        activitySelectRoutBinding.toolbar.img_select_all.visibility = View.GONE
        activitySelectRoutBinding.toolbar.img_correct.visibility = View.GONE

        LocationHelper.enableGPS(this)

    }

    override fun onRoutFetchSuccess(selectRoutResModel: SelectRoutResModel) {
//        selectRoutViewModel.routListAdapter.setData(selectRoutResModel.routList)

    }

    override fun getCurrentLocation() {
        //todo get current location here when device location is already enabled
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ENABLE_GPS -> when (resultCode) {
                Activity.RESULT_OK -> {
                    val states = LocationSettingsStates.fromIntent(data!!)
                    Log.d(mTag, "GPS Status ${states.isLocationPresent.toString() + ""}")
                    // All required changes were successfully made
                    //todo when location enable programmatically
                    //Toast.makeText(this@SelectRoutActivity, states.isLocationPresent.toString() + "", Toast.LENGTH_SHORT).show()
                }
                Activity.RESULT_CANCELED -> {
                    // The user was asked to change settings, but chose not to
                    //todo when location enable canceled
                    //Toast.makeText(this@SelectRoutActivity, "Canceled", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    //todo when location enable unresolved result
                    //Toast.makeText(this@SelectRoutActivity, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(SelectRoutActivity.getInstance(this))
        this.finish()
    }
}
