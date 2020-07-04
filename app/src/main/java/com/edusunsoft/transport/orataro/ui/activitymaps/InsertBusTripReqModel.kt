package com.edusunsoft.transport.orataro.ui.activitymaps

import com.google.gson.annotations.SerializedName

data class InsertBusTripReqModel(@SerializedName("BusRouteID") var BusRouteID: String = "",
                                 @SerializedName("BusID") var BusID: String = "",
                                 @SerializedName("IsForPickup") var IsForPickup: String = "",
                                 @SerializedName("BusRouteDate") var BusRouteDate: String = "",
                                 @SerializedName("DevicesName") var DevicesName: String = "",
                                 @SerializedName("StartTime") var StartTime: String = "") {
}