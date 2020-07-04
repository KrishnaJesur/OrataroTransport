package com.edusunsoft.transport.orataro.ui.activitytc

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseActivity
import com.edusunsoft.transport.orataro.base.BaseViewModel
import com.edusunsoft.transport.orataro.databinding.ActivityAboutUsBinding
import com.edusunsoft.transport.orataro.databinding.ActivityTcBinding
import com.edusunsoft.transport.orataro.helper.LocationHelper


class TCActivity : BaseActivity(),TCNavigator {


    private val mTag = TCActivity::class.java.simpleName

    private lateinit var mBinding: ActivityTcBinding
    private lateinit var mViewModel: TCViewModel

    companion object {
        fun getInstance(activity: Activity): Intent {
            return Intent(activity, TCActivity::class.java)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tc)
        mViewModel = TCViewModel(this, this)

        setToolbar(mBinding.toolbar,"Terms & Conditions",true)

        mBinding.mtcViewModel = mViewModel

    }

}
