package com.edusunsoft.transport.orataro.ui.activityselectrout

interface SelectRoutNavigator {
    fun onRoutFetchSuccess(selectRoutResModel: SelectRoutResModel)
    fun onRouteGetList(selectRouteResponseModel: SelectRouteResponseModel)
    fun onRoutItemClick(rout: SelectRouteResponseModel.RouteItem)
    fun RedirectToLogin()
    fun GetCurrentDate()

}