package com.edusunsoft.transport.orataro.ui.activitytripdetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseActivity
import com.edusunsoft.transport.orataro.base.BaseViewModel
import com.edusunsoft.transport.orataro.databinding.ActivityContactUsBinding
import com.edusunsoft.transport.orataro.databinding.ActivityTripDetailsBinding
import com.edusunsoft.transport.orataro.helper.LocationHelper
import com.edusunsoft.transport.orataro.ui.activitylogin.LoginReqModel
import com.edusunsoft.transport.orataro.ui.activitymaps.MapsViewModel
import kotlinx.android.synthetic.main.no_data_found_layout.view.*


class TripDetailsActivity : BaseActivity(), TripDetailsNavigator {

    private val mTag = TripDetailsActivity::class.java.simpleName

    private lateinit var mBinding: ActivityTripDetailsBinding
    private lateinit var mViewModel: TripDetailsViewModel

    companion object {
        fun getInstance(activity: Activity): Intent {
            return Intent(activity, TripDetailsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_trip_details)
        mViewModel = TripDetailsViewModel(this, this)

        setToolbar(mBinding.toolbar, getString(R.string.title_trip_details), true)
        mBinding.mTripDetailsViewModel = mViewModel
        mBinding.toolbar.rlDummy.visibility = View.GONE

    }

    override fun DisplayMessageForNoRouteListAvailable() {
        mBinding.lylNoDataFound.txt_message.text = this.resources.getString(R.string.noroutesavailable)
    }

}
