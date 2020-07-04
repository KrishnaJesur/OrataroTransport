package com.edusunsoft.transport.orataro.ui.activitynotification

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseActivity
import com.edusunsoft.transport.orataro.base.BaseViewModel
import com.edusunsoft.transport.orataro.databinding.ActivityContactUsBinding
import com.edusunsoft.transport.orataro.databinding.ActivityNotificationBinding
import com.edusunsoft.transport.orataro.helper.LocationHelper


class NotificationActivity : BaseActivity(),NotificationNavigator {


    private val mTag = NotificationActivity::class.java.simpleName

    private lateinit var mBinding: ActivityNotificationBinding
    private lateinit var mViewModel: NotificationViewModel

    companion object {
        fun getInstance(activity: Activity): Intent {
            return Intent(activity, NotificationActivity::class.java)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_notification)

        mViewModel = NotificationViewModel(this, this)

        setToolbar(mBinding.toolbar,"Notification",true)

        mBinding.mNotificationViewModel = mViewModel

    }

}
