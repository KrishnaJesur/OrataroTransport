package com.edusunsoft.transport.orataro.ui.activitylicense

import com.edusunsoft.transport.orataro.base.BaseNavigator

interface LDNavigator : BaseNavigator {

    fun onGetLDList(driverLDResModel: DriverLDResModel)
    fun RedirectToLogin()
    fun onLDItemClick(driverDocumentItem: DriverLDResModel.DriverDocumentItem)

}