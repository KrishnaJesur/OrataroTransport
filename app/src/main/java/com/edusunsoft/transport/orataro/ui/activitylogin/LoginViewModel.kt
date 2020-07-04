package com.edusunsoft.transport.orataro.ui.activitylogin

import android.content.Context
import android.os.Handler
import android.view.View
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseViewModel
import com.edusunsoft.transport.orataro.dialog.LoadingDialog
import com.edusunsoft.transport.orataro.network.ApiProvider
import com.edusunsoft.transport.orataro.network.ApiRequest
import com.edusunsoft.transport.orataro.network.NetworkCallBack
import com.edusunsoft.transport.orataro.utils.AppPreference
import com.edusunsoft.transport.orataro.utils.Toaster
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.Reader

class LoginViewModel(private var loginNavigator: LoginNavigator, context: Context)
    : BaseViewModel<LoginNavigator>(loginNavigator, context), NetworkCallBack {

    override fun onError(errorBody: String) {
        loginNavigator.onLoginFailed(errorBody)
        LoadingDialog.hideLoading()
    }

    override fun onSuccess(response: Any?) {

        if (response is LoginResModel) {

            when {

                response.status_code == "0" -> {

                    val message = response.message
                    Toaster.showShortToast(context, message)
                    LoadingDialog.hideLoading()

                }

                response.status_code == "1" -> {
                    val data = response.data
                    AppPreference.saveAccessToken(context, context.resources.getString(R.string.bearer) + " " + data.accesstoken)
//                    AppPreference.setUserName(data.username)
//                    AppPreference.setUserMobile(data.usermobile)
//                    AppPreference.setUserProfile(data.userprofile)
                    loginNavigator.onLoginSuccess(response)
                    LoadingDialog.hideLoading()
                }

                response.status_code == "2" -> {
                    val message = response.message
                    Toaster.showShortToast(context, message)
                    LoadingDialog.hideLoading()
                }

                else -> {

                    val message = response.message
                    Toaster.showShortToast(context, message)
                    LoadingDialog.hideLoading()

                }

            }

        }

    }

    private var userId: String = ""
    private var userPassword: String = ""

    fun showPassword() {
        loginNavigator.showPassword()
    }

    fun hidePassword() {

        loginNavigator.hidePassword()
    }


    fun doLogin(loginReqModel: LoginReqModel) {

        userId = loginReqModel.userId
        userPassword = loginReqModel.userPassword

        if (userId.isEmpty()) {
            loginNavigator.inValidUserId(context.resources.getString(R.string.enter_username))
            return
        }

        if (userPassword.isEmpty()) {
            loginNavigator.inValidPassword(context.resources.getString(R.string.enter_password))
            return
        }

        LoadingDialog.showLoading(context)

        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                    ApiProvider.provideApi(context).doLoginAsync(loginReqModel),
                    this@LoginViewModel
            )
        }


//        Handler().postDelayed({
//            LoadingDialog.hideLoading()
//            loginNavigator.onLoginSuccess(LoginResModel())
//        }, 1500)

    }
}

