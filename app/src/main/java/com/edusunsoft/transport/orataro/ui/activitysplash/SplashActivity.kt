package com.edusunsoft.transport.orataro.ui.activitysplash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.ui.activityaboutus.AboutusActivity
import com.edusunsoft.transport.orataro.ui.activitylogin.LoginActivity
import com.edusunsoft.transport.orataro.ui.activityselectrout.SelectRoutActivity
import com.edusunsoft.transport.orataro.utils.AppPreference

class SplashActivity : AppCompatActivity() {

    companion object {
        fun getInstance(activity: Activity): Intent {
            return Intent(activity, SplashActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (!AppPreference.getLogin()) {
            Handler().postDelayed({
                startActivity(LoginActivity.getInstance(this))
                finish()
            }, 3000)
        } else {
            Handler().postDelayed({
                startActivity(SelectRoutActivity.getInstance(this))
                finish()
            }, 3000)
        }
    }
}
