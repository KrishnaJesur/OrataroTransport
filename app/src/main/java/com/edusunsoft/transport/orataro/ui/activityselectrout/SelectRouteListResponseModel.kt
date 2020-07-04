package com.edusunsoft.transport.orataro.ui.activityselectrout

data class SelectRouteResponseModel(val status_code: Int = 0,
                                    val data: Data,
                                    val message: String = "") {


    data class RouteItem(val BusID: String = "",
                         val InstituteID: String = "",
                         val EndTime: String = "",
                         val IsForPickup: Boolean = false,
                         val StartTime: String = "",
                         val RouteName: String = "",
                         val BusRouteName: String = "",
                         val ClientID: String = "",
                         val StartPoint: String = "",
                         val RouteID: String = "",
                         val EndPoint: String = "",
                         val VehicleRegNo: String = "",
                         val BusRouteID: String = "",
                         val IsForDrop: Boolean = false)


    data class Data(val DriverId: String = "",
                    val UserName: String = "",
                    val route: ArrayList<RouteItem> = ArrayList())
}





