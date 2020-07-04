package com.edusunsoft.transport.orataro.ui.activityaboutus

import android.content.Context
import com.edusunsoft.transport.orataro.base.BaseViewModel

class AboutUsViewModel(private var navigator: AboutusNavigator, context: Context)
    : BaseViewModel<AboutusNavigator>(navigator, context) {

    fun onBackClicked(){

        navigator.onBack()
    }
}