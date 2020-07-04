package com.edusunsoft.transport.orataro.ui.activityprofile

import android.text.Editable

data class GetDriverProfile(val status_code: Int = 0,
                            val data: Data,
                            val message: String = "") {

    data class Data(val DriverInfo: DriverInfo)


    data class DriverInfo(val DriverRate: String = "",
                          val BloodGroup_Term: String = "",
                          val ExpireOn: String = "",
                          val DriverName: String = "",
                          val IsDirver: Boolean = false,
                          val Photo: String = "",
                          val IsSupporter: Boolean = false,
                          val DriverMobileNo: String = "",
                          val City: String = "",
                          val Distict: String = "",
                          val Taluka: String = "",
                          val Add1: String = "",
                          val Add2: String = "",
                          val PinCode: String = "",
                          val DriverID: String = "",
                          val SchoolNote: String = "",
                          val DriverLicenseNo: String = "",
                          val Country: String = "",
                          val FacilitatorName: String = "")

}






