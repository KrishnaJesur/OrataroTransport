package com.edusunsoft.transport.orataro.ui.activitymaps

import com.google.gson.annotations.SerializedName

data class CloseTripReqModel(@SerializedName("BusTripLogID") var BusTripLogID: String = "",
                             @SerializedName("EndTime") var EndTime: String = ""){
}