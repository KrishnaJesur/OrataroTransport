package com.edusunsoft.transport.orataro.ui.activitylogin

import com.google.gson.annotations.SerializedName

data class LoginReqModel(@SerializedName("username") var userId: String = "",
                         @SerializedName("password") var userPassword: String = "")
