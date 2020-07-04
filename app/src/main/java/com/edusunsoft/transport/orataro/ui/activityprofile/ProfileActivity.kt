package com.edusunsoft.transport.orataro.ui.activityprofile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.edusunsoft.transport.orataro.BuildConfig
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseActivity
import com.edusunsoft.transport.orataro.base.BaseViewModel
import com.edusunsoft.transport.orataro.databinding.ActivityProfileBinding
import com.edusunsoft.transport.orataro.helper.LocationHelper
import com.edusunsoft.transport.orataro.utils.AppPreference

class ProfileActivity : BaseActivity(), ProfileNavigator {

    private val mTag = ProfileActivity::class.java.simpleName

    private lateinit var mBinding: ActivityProfileBinding
    private lateinit var mViewModel: ProfileViewModel

    companion object {
        fun getInstance(activity: Activity): Intent {
            return Intent(activity, ProfileActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        mViewModel = ProfileViewModel(this, this)
        mBinding.mProfileViewModel = mViewModel

        val mBaseViewModel = BaseViewModel(this, this)
        mBaseViewModel.isBackShow.set(true)

        mBinding.mBaseViewModel = mBaseViewModel

    }

    override fun onGetDriverProfile(response: GetDriverProfile) {
        mBinding.tvName.text = Editable.Factory.getInstance().newEditable(response.data.DriverInfo.DriverName)
        mBinding.txtMobileNumber.text = Editable.Factory.getInstance().newEditable(response.data.DriverInfo.DriverMobileNo)
        mBinding.txtBloodGroup.text = Editable.Factory.getInstance().newEditable(response.data.DriverInfo.BloodGroup_Term)
        mBinding.txtAddress.text = Editable.Factory.getInstance().newEditable(response.data.DriverInfo.Add1)

        if (response.data.DriverInfo.IsDirver) {
            mBinding.txtType.text = Editable.Factory.getInstance().newEditable("Driver")
        } else if (response.data.DriverInfo.IsSupporter) {
            mBinding.txtType.text = Editable.Factory.getInstance().newEditable("Supporter")
        }

        if (response.data.DriverInfo.Photo == null || response.data.DriverInfo.Photo == "" || response.data.DriverInfo.Photo == "null") {
            mBinding.ivPhoto.setImageResource(R.drawable.user)
        } else {
            Glide.with(this)
                    .asBitmap().apply(RequestOptions.circleCropTransform())
                    .load(BuildConfig.IMAGE_URL + response.data.DriverInfo.Photo).placeholder(R.drawable.user)
                    .into(mBinding.ivPhoto)
        }

    }

}
