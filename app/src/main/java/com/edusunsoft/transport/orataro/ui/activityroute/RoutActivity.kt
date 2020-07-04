package com.edusunsoft.transport.orataro.ui.activityroute

import android.app.Activity
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseActivity
import com.edusunsoft.transport.orataro.databinding.ActivityRoutBinding
import com.edusunsoft.transport.orataro.helper.LocationHelper
import com.edusunsoft.transport.orataro.ui.activitydashboard.DashboardActivity
import com.edusunsoft.transport.orataro.ui.activityselectrout.SelectRoutNavigator
import com.edusunsoft.transport.orataro.ui.activityselectrout.SelectRoutResModel
import com.edusunsoft.transport.orataro.ui.activityselectrout.SelectRoutViewModel
import com.edusunsoft.transport.orataro.ui.activityselectrout.SelectRouteResponseModel
import com.edusunsoft.transport.orataro.utils.REQUEST_CODE_ENABLE_GPS
import com.google.android.gms.location.LocationSettingsStates


class RoutActivity : BaseActivity(), SelectRoutNavigator {
    override fun GetCurrentDate() {

    }

    override fun RedirectToLogin() {

    }

    override fun onRoutItemClick(rout: SelectRouteResponseModel.RouteItem) {

    }

    override fun onRouteGetList(selectRouteResponseModel: SelectRouteResponseModel) {
        mViewModel.routListAdapter.setData(selectRouteResponseModel.data.route)
    }


    private val mTag = DashboardActivity::class.java.simpleName

    private lateinit var mBinding: ActivityRoutBinding
    private lateinit var mViewModel: SelectRoutViewModel

    companion object {
        fun getInstance(activity: Activity): Intent {
            return Intent(activity, DashboardActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_rout)
        mViewModel = SelectRoutViewModel(this, this)

        setToolbar(mBinding.toolbar, getString(R.string.title_rout), true)

        mBinding.mRoutViewModel = mViewModel

        LocationHelper.enableGPS(this)

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

    override fun onRoutFetchSuccess(selectRoutResModel: SelectRoutResModel) {
//        mViewModel.routListAdapter.setData(selectRoutResModel.routList)
        Log.d("getroutelist", "")
    }

}
