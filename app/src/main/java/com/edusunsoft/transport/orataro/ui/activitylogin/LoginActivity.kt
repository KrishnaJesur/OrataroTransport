package com.edusunsoft.transport.orataro.ui.activitylogin

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseActivity
import com.edusunsoft.transport.orataro.databinding.ActivityLoginBinding
import com.edusunsoft.transport.orataro.ui.activitylicense.LDActivity
import com.edusunsoft.transport.orataro.ui.activitymaps.MapsActivity
import com.edusunsoft.transport.orataro.ui.activitymaps.MapsActivity2
import com.edusunsoft.transport.orataro.ui.activitydashboard.DashboardActivity
import com.edusunsoft.transport.orataro.ui.activityselectrout.SelectRoutActivity
import com.edusunsoft.transport.orataro.ui.activitysplash.SplashActivity
import com.edusunsoft.transport.orataro.utils.AppPreference
import com.edusunsoft.transport.orataro.utils.Toaster
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), LoginNavigator {
    override fun onLoginFailed(error: String) {

        Toaster.showShortToast(this, error)
        login_progress.visibility = View.INVISIBLE
    }

    private val mTag = LoginActivity::class.java.simpleName
    private lateinit var loginBinding: ActivityLoginBinding

    companion object {
        fun getInstance(splashActivity: SplashActivity): Intent {
            return Intent(splashActivity, LoginActivity::class.java)
        }

        fun getInstance(mapsActivity: MapsActivity): Intent {
            return Intent(mapsActivity, LoginActivity::class.java)
        }

        fun getInstance(mapsActivity: MapsActivity2): Intent {
            return Intent(mapsActivity, LoginActivity::class.java)
        }

        fun getInstance(selectRoutActivity: SelectRoutActivity): Intent {
            return Intent(selectRoutActivity, LoginActivity::class.java)
        }

        fun getInstance(dashboardActivity: DashboardActivity): Intent {
            return Intent(dashboardActivity, LoginActivity::class.java)
        }

        fun getInstance(ldActivity: LDActivity): Intent {
            return Intent(ldActivity, LoginActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        loginBinding.mLoginViewModel = LoginViewModel(this, this)

        loginBinding.mLoginReqModel = LoginReqModel()
    }

    override fun onError(error: String) {
        super.onError(error)
        Log.d("getError",error)
        Log.e(mTag, "onError= $error")
    }

    override fun onLoginSuccess(loginResModel: LoginResModel) {

        AppPreference.saveLoginData(this, loginResModel)
        AppPreference.setUserId(loginBinding.edtUserId.text.toString())
        AppPreference.setLogin(true)
        startActivity(SelectRoutActivity.getInstance(this))
        finish()

    }

    override fun inValidUserId(error: String) {
        Toaster.showShortToast(this, error)
    }

    override fun inValidPassword(error: String) {
        Toaster.showShortToast(this, error)
    }

    override fun showPassword() {

        if (!loginBinding.edtPassword.text.toString().isEmpty()) {
            loginBinding.edtPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            loginBinding.imgHidePassword.visibility = View.VISIBLE
            loginBinding.imgShowPassword.visibility = View.GONE
            loginBinding.edtPassword.setSelection(loginBinding.edtPassword.text.length)
        }else{
            Toaster.showShortToast(this, resources.getString(R.string.enter_password))
        }

    }

    override fun hidePassword() {

        loginBinding.edtPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        loginBinding.imgShowPassword.visibility = View.VISIBLE
        loginBinding.imgHidePassword.visibility = View.GONE
        loginBinding.edtPassword.setSelection(loginBinding.edtPassword.text.length)

    }

}
