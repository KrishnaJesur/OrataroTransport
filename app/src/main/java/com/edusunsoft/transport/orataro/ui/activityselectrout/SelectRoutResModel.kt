package com.edusunsoft.transport.orataro.ui.activityselectrout

import com.google.gson.annotations.SerializedName

data class SelectRoutResModel(@SerializedName("error_code") var errorCode: String = "",
                              @SerializedName("error_message") var errorMessage: String = "",
                              @SerializedName("routs") var routList: ArrayList<Rout> = ArrayList()) {

    data class Rout(@SerializedName("rout_title") var routTitle: String = "",
                    @SerializedName("rout_time") var routTime: String = "",
                    @SerializedName("rout_image") var routImage: String = "",
                    @SerializedName("start_lat") var startLat: Double = 0.0,
                    @SerializedName("start_long") var startLong: Double = 0.0,
                    @SerializedName("stop_lat") var stopLat: Double = 0.0,
                    @SerializedName("stop_long") var stopLong: Double = 0.0,
                    @SerializedName("rout_id") var routId: String = "")
}