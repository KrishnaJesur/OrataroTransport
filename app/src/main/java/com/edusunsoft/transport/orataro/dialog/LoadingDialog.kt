package com.edusunsoft.transport.orataro.dialog

import android.app.Dialog
import android.content.Context
import androidx.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.Paint
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import android.view.LayoutInflater
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.Window.FEATURE_NO_TITLE
import android.widget.LinearLayout
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.databinding.DialogLoadingBinding
import com.edusunsoft.transport.orataro.view.CircleImageView

object LoadingDialog {

    private lateinit var dialog: Dialog

    fun showLoading(context: Context) {
        dialog = Dialog(context)
        val circleImageView = CircleImageView(context, R.color.white)
        val circleProgressBar = CircularProgressDrawable(context)
        circleProgressBar.setColorSchemeColors(
                context.resources.getColor(R.color.red),
                context.resources.getColor(R.color.green),
                context.resources.getColor(R.color.blue),
                context.resources.getColor(R.color.yellow)
        )
        circleProgressBar.strokeCap = Paint.Cap.ROUND
        circleProgressBar.strokeWidth = context.resources.getDimension(R.dimen.progress_stroke_width)
        circleProgressBar.centerRadius = context.resources.getDimension(R.dimen.progress_center_radius)
        circleProgressBar.backgroundColor = Color.TRANSPARENT
        circleImageView.setBackgroundDrawable(circleProgressBar)
        circleProgressBar.start()
        dialog.requestWindowFeature(FEATURE_NO_TITLE)
        val dialogLoadingBinding: DialogLoadingBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_loading, null, false)
        dialog.setContentView(dialogLoadingBinding.root)
        if (dialog.window != null) {
            dialog.window?.setLayout(WRAP_CONTENT, WRAP_CONTENT)
            dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        }
        dialog.setCanceledOnTouchOutside(false)
        dialogLoadingBinding.rootView.addView(circleImageView)
        val lp: LinearLayout.LayoutParams = circleImageView.layoutParams as LinearLayout.LayoutParams
        lp.height = context.resources.getDimension(R.dimen.progress_height).toInt()
        lp.width = context.resources.getDimension(R.dimen.progress_width).toInt()
        dialog.show()
    }

    fun hideLoading() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }
}