package com.edusunsoft.transport.orataro.ui.activitylicense

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.request.RequestOptions
import com.edusunsoft.transport.orataro.BuildConfig
import com.edusunsoft.transport.orataro.R

data class DriverLDResModel(val status_code: Int = 0,
                            val data: Data,
                            val message: String = "") {


    data class DriverDocumentItem(val DocumentPath: String = "",
                                  val DocumentTitle: String = "")


    data class Data(val DriverDocument: List<DriverDocumentItem>?)

    object DataBindingAdapter {

        @BindingAdapter("loadimageUrl")
        @JvmStatic
        fun loadImageurl(view: ImageView, imageUrl: String) {

            @GlideModule
            if (imageUrl == null || imageUrl.equals("") || imageUrl.equals("null")) {
                view.setImageResource(R.drawable.user)
            } else {
                Glide.with(view.getContext())
                        .asBitmap().dontTransform()
                        .load(BuildConfig.IMAGE_URL + imageUrl).placeholder(R.drawable.license_img)
                        .into(view)
            }

        }

    }

}



