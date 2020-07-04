package com.edusunsoft.transport.orataro.ui.activitycontactus

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseActivity
import com.edusunsoft.transport.orataro.base.BaseViewModel
import com.edusunsoft.transport.orataro.databinding.ActivityContactUsBinding
import com.edusunsoft.transport.orataro.helper.LocationHelper
import kotlinx.android.synthetic.main.app_toolbar_layout.*


class ContactusActivity : BaseActivity(), ContactusNavigator {


    private val mTag = ContactusActivity::class.java.simpleName

    private lateinit var activityContactUsBinding: ActivityContactUsBinding
    private lateinit var mViewModel: ContactUsViewModel

    companion object {
        fun getInstance(activity: Activity): Intent {
            return Intent(activity, ContactusActivity::class.java)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        
        super.onCreate(savedInstanceState)
        activityContactUsBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us)
        activityContactUsBinding.toolbar.rlDummy.visibility = View.GONE

        mViewModel = ContactUsViewModel(this, this)

        setToolbar(activityContactUsBinding.toolbar, getString(R.string.title_contact_us), true)
        rl_dummy.visibility = View.GONE

        activityContactUsBinding.mContactUsViewModel = mViewModel

    }

}
