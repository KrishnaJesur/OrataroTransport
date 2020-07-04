package com.edusunsoft.transport.orataro.ui.activitymaps

import com.google.gson.annotations.SerializedName

data class UpdateLiveLatLongReqModel(@SerializedName("Lat") var Lat: Double = 0.0,
                                     @SerializedName("Long") var Long: Double = 0.0,
                                     @SerializedName("BusRouteDate") var BusRouteDate: String = "",
                                     @SerializedName("BusTripLogID") var BusTripLogID: String = "",
                                     @SerializedName("DevicesName") var DevicesName: String = "") {
}