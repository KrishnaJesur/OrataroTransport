package com.edusunsoft.transport.orataro.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.databinding.AppToolbarLayoutBinding
import com.edusunsoft.transport.orataro.utils.Controller
import java.security.InvalidParameterException


abstract class BaseActivity : AppCompatActivity(), BaseNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        if (resources.getString(R.string.google_maps_key) == "AIzaSyC5jLWPa_hRkT8ryV11Tmi_Zpd05hG3S2A\n" +
                "\n" +
                "\n") {
            throw InvalidParameterException("Please change google maps api key")
        }


    }

    override fun onError(error: String) {

    }

    override fun onBack() {
        super.onBackPressed()
    }

    open fun getCurrentLocation() {

    }

    fun setToolbar(toolbar: AppToolbarLayoutBinding?, s: String, b: Boolean) {

        val mBaseViewModel = BaseViewModel(this, this)
        mBaseViewModel.isBackShow.set(b)
        toolbar?.mBaseViewModel = mBaseViewModel
        toolbar?.tvActivityTitle?.text = s

    }


}
