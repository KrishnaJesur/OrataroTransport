package com.edusunsoft.transport.orataro.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.edusunsoft.transport.orataro.R


object Toaster {

    fun showShortToast(@NonNull context: AppCompatActivity, @NonNull message: String) {
        when (message) {
            "" -> {
                throw Exception(context.resources.getString(R.string.messagenotempty))
            }
        }
        context.runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun showLongToast(@NonNull context: AppCompatActivity, @NonNull message: String) {
        when (message) {
            "" -> {
                throw Exception("Message should not empty")
            }
        }
        context.runOnUiThread {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    fun showShortToast(@NonNull context: Context, @NonNull message: String) {
        when (message) {
            "" -> {
                throw Exception("Message should not empty")
            }
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun shortToast(context: Context, message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

}