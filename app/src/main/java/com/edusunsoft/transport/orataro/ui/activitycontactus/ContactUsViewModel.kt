package com.edusunsoft.transport.orataro.ui.activitycontactus

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseViewModel


class ContactUsViewModel(private var navigator: ContactusNavigator, context: Context)
    : BaseViewModel<ContactusNavigator>(navigator, context) {

    fun Sendemail() {

        val intent = Intent(Intent.ACTION_SEND)
        intent.setType("message/rfc822")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.resources.getString(R.string.email_address)))
        try {
            context.startActivity(Intent.createChooser(intent, "Send mail..."))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show()
        }

    }


}