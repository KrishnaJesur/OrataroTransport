package com.edusunsoft.transport.orataro.ui.activitydashboard

import android.app.Activity
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseActivity
import com.edusunsoft.transport.orataro.databinding.ActivityDashboardBinding
import com.edusunsoft.transport.orataro.ui.activityaboutus.AboutusActivity
import com.edusunsoft.transport.orataro.ui.activitycontactus.ContactusActivity
import com.edusunsoft.transport.orataro.ui.activitylicense.LDActivity
import com.edusunsoft.transport.orataro.ui.activitylogin.LoginActivity
import com.edusunsoft.transport.orataro.ui.activityprofile.ProfileActivity
import com.edusunsoft.transport.orataro.ui.activityselectrout.SelectRoutActivity
import com.edusunsoft.transport.orataro.utils.Controller
import java.text.SimpleDateFormat
import java.util.*


class DashboardActivity : BaseActivity(), DashboardNavigator {
    override fun onRouteListClick() {
        Controller.FROMSTRING = ""
        startActivity(SelectRoutActivity.getInstance(this))
    }

    private val mTag = DashboardActivity::class.java.simpleName

    private lateinit var mBinding: ActivityDashboardBinding
    private lateinit var dashboardViewModel: DashboardViewModel

    companion object {

        var CurrentDate: String = ""
        var CurrentTime: String = ""

        fun getInstance(activity: Activity): Intent {
            return Intent(activity, DashboardActivity::class.java)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        dashboardViewModel = DashboardViewModel(this, this)
        mBinding.dashbpardviewModel = dashboardViewModel
        mBinding.dashboardactivity = this


    }

    override fun GetCurrentDate() {

        val c = Calendar.getInstance().getTime()

        val CurrntDate = SimpleDateFormat("yyyy-MM-dd")
        CurrentDate = CurrntDate.format(c)

        val curenttime = SimpleDateFormat("hh:mm")
        CurrentTime = curenttime.format(c)

    }

    override fun RedirectToLogin() {

        startActivity(LoginActivity.getInstance(this@DashboardActivity))
        finish()

    }

    override fun getInstantMessages() {
        Controller.FROMSTRING = "InstantMessages"
        startActivity(SelectRoutActivity.getInstance(this@DashboardActivity))
    }

    override fun getLicenseAndDocuments() {
        startActivity(LDActivity.getInstance(this))
    }

    override fun contactus() {
        startActivity(ContactusActivity.getInstance(this))
    }

    override fun aboutus() {
        startActivity(AboutusActivity.getInstance(this))
    }

    override fun myProfile() {
        startActivity(ProfileActivity.getInstance(this))
    }

    fun Logout() {
        showLogoutDialog(this)
    }

    private fun showLogoutDialog(dashboardActivity: DashboardActivity) {

        AlertDialog.Builder(dashboardActivity).setIcon(R.drawable.ic_launcher)
                .setTitle(R.string.logout)
                .setMessage(R.string.logoutmessage)
                .setPositiveButton(R.string.yes) { dialog, _ ->
                    // Redirect to Login Activity
                    dialog.dismiss()
                    finish()
                    Controller.RedirectToLoginActivity(this)
                }.setNegativeButton(R.string.no) { dialog, _ ->
                    dialog.dismiss()
                }.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

}
