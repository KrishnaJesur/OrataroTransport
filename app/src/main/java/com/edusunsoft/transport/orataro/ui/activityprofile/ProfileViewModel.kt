package com.edusunsoft.transport.orataro.ui.activityprofile

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.edusunsoft.transport.orataro.BuildConfig
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseViewModel
import com.edusunsoft.transport.orataro.dialog.LoadingDialog
import com.edusunsoft.transport.orataro.network.ApiProvider
import com.edusunsoft.transport.orataro.network.ApiRequest
import com.edusunsoft.transport.orataro.network.NetworkCallBack
import com.edusunsoft.transport.orataro.ui.activitylicense.DriverLDResModel
import com.edusunsoft.transport.orataro.utils.AppPreference
import com.edusunsoft.transport.orataro.utils.Controller
import com.edusunsoft.transport.orataro.utils.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ProfileViewModel(private var navigator: ProfileNavigator, context: Context)
    : BaseViewModel<ProfileNavigator>(navigator, context), NetworkCallBack {


    fun GetUserName(): String {
        return AppPreference.getUserName()
    }

    fun GetUserMobile(): String {
        return AppPreference.getUserMobile()
    }

    fun getImageUrl(): String? { // The URL will usually come from a model (i.e Profile)
        return AppPreference.getUserProfile()
    }

    init {

        // get Driver profile
        try {
            getMyProfile()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        /*END*/

    }

    private fun getMyProfile() {
        LoadingDialog.showLoading(context)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                    ApiProvider.provideApi(context).GetdriverProfile(),
                    this@ProfileViewModel
            )
        }
    }

    object DataBindingAdapter {

        @BindingAdapter("imageUrl")
        @JvmStatic
        fun loadImage(view: ImageView, imageUrl: String?) {

            if (AppPreference.getUserProfile() == null || AppPreference.getUserProfile() == "" || AppPreference.getUserProfile() == "null") {
                view.setImageResource(R.drawable.user)
            } else {
                Glide.with(view.context)
                        .asBitmap().apply(RequestOptions.circleCropTransform())
                        .load(BuildConfig.IMAGE_URL + imageUrl).placeholder(R.drawable.user)
                        .into(view)
            }

        }

    }

    override fun onSuccess(response: Any?) {
        if (response is GetDriverProfile) {

            when (response.status_code) {
                0 -> {

                    val message = response.message
                    Toaster.showShortToast(context, message)
                    LoadingDialog.hideLoading()

                }
                1 -> {

                    LoadingDialog.hideLoading()
                    navigator.onGetDriverProfile(response)

                }
                2 -> {

                    val message = response.message
                    Toaster.showShortToast(context, message)
                    LoadingDialog.hideLoading()
                    Controller.RedirectToLoginActivity(context)

                }
            }

        }
    }

    override fun onError(errorBody: String) {

    }

}