package com.edusunsoft.transport.orataro.ui.activitylogin

import com.google.gson.annotations.SerializedName

data class LoginResModel(

        @SerializedName("status_code")
        var status_code: String,
        @SerializedName("message")
        var message: String,
        @SerializedName("data")
        var data: Data

)

data class Data(
        @SerializedName("accesstoken")
        val accesstoken: String,
        @SerializedName("Name")
        val username: String,
        @SerializedName("Photo")
        val userprofile: String,
        @SerializedName("MobileNo")
        val usermobile: String
)