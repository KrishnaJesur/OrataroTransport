package com.edusunsoft.transport.orataro.ui.activityprofile

import com.edusunsoft.transport.orataro.base.BaseNavigator

interface ProfileNavigator : BaseNavigator {
    fun onGetDriverProfile(response: GetDriverProfile)

}