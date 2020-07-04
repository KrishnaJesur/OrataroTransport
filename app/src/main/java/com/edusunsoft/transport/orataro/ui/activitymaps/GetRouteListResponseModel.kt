package com.edusunsoft.transport.orataro.ui.activitymaps

import android.widget.CheckBox
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.edusunsoft.transport.orataro.BuildConfig
import com.edusunsoft.transport.orataro.R


data class GetRouteListModel(val status_code: Int = 0,
                             val data: Data,
                             val message: String = "") {

    data class RouteItem(val RouteName: String = "",
                         val Latitude: Double = 0.0,
                         val PickupPointID: String = "",
                         val PointName: String = "",
                         val Longitude: Double = 0.0,
                         val Route: String = "")


    data class StudentInfoItem(var DropPointID: String = "",
                               var Standard: String = "",
                               var Division: String = "",
                               var PickupPointID: String = "",
                               var StudentName: String = "",
                               var StudentID: String = "",
                               var photo: String = "",
                               var PickupPerson1: String = "",
                               var PickupPerson2: String = "",
                               var StatusTerm: String = "Absent",
                               var EmergencyContact1: String = "",
                               var EmergencyContact2: String = "",
                               var GCMID: String = "",
                               var isSelected: Boolean = true)

    data class StudentAttendance(var StudentID: String = "",
                                 var StatusTerm: String = "")


    data class Data(val StudentInfo: ArrayList<StudentInfoItem> = ArrayList(),
                    val Route: ArrayList<RouteItem>? = ArrayList(), val polyline: String)


    object DataBindingAdapter {

        @BindingAdapter("imageUrl")
        @JvmStatic
        fun loadurl(view: ImageView, imageUrl: String) {

            if (imageUrl == null || imageUrl.equals("") || imageUrl.equals("null")) {
                view.setImageResource(R.drawable.user)
            } else {
                Glide.with(view.context)
                        .load(BuildConfig.IMAGE_URL + imageUrl).placeholder(R.drawable.license_img)
                        .override(300, 100)
                        .into(view)
            }

        }

    }

}


