package com.edusunsoft.transport.orataro.ui.activitymaps

data class UpdateLatLongResModel(val status_code: Int = 0,
                                 val data: Data,
                                 val message: String = "") {

    data class BusRouteLiveLatLong(val BusTripLogID: String = "",
                                   val InstituteID: String = "",
                                   val DevicesName: String = "",
                                   val BusRouteLiveLatLongID: String = "",
                                   val Long: Double = 0.0,
                                   val ClientID: String = "",
                                   val Lat: Double = 0.0,
                                   val BusRouteDate: String = "")


    data class Data(val BusRouteLiveLatLong: BusRouteLiveLatLong)
}





