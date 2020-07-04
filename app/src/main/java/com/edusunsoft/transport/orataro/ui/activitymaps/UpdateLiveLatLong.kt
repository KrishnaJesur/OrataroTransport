package com.edusunsoft.transport.orataro.ui.activitymaps

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UpdateLiveLatLong(
        var Status: String? = "",
        var latitude: String? = "",
        var longitude: String? = ""
)

