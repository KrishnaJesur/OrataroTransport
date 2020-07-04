package com.edusunsoft.transport.orataro.ui.activitylicense

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseActivity
import com.edusunsoft.transport.orataro.databinding.ActivityLdBinding
import com.edusunsoft.transport.orataro.ui.activitydocdetail.DocumentDetailActivity
import com.edusunsoft.transport.orataro.ui.activitylogin.LoginActivity
import kotlinx.android.synthetic.main.app_toolbar_layout.*
import kotlinx.android.synthetic.main.no_data_found_layout.view.*


class LDActivity : BaseActivity(), LDNavigator {

    private val mTag = LDActivity::class.java.simpleName

    private lateinit var mBinding: ActivityLdBinding
    private lateinit var mViewModel: LDViewModel

    companion object {
        fun getInstance(activity: Activity): Intent {
            return Intent(activity, LDActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_ld)
        mViewModel = LDViewModel(this, this)
        setToolbar(mBinding.toolbar, "Licence & Documents", true)
        mBinding.mldViewModel = mViewModel

    }

    override fun onGetLDList(driverLDResModel: DriverLDResModel) {

        if (driverLDResModel.data.DriverDocument!!.isNotEmpty()) {
            mViewModel.LDListAdapter.setData(driverLDResModel.data.DriverDocument as ArrayList<DriverLDResModel.DriverDocumentItem>)
        } else {
            mBinding.lylNoDataFound.txt_message.text = this.resources.getString(R.string.docnotavailable)
            mViewModel.isShow.set(true)
        }

    }

    override fun RedirectToLogin() {
        startActivity(LoginActivity.getInstance(this@LDActivity))
    }

    override fun onLDItemClick(driverDocumentItem: DriverLDResModel.DriverDocumentItem) {

        startActivity(DocumentDetailActivity.getInstance(this, driverDocumentItem.DocumentPath, driverDocumentItem.DocumentTitle))

    }

}
