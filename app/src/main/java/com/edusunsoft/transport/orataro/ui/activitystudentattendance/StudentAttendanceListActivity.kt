package com.edusunsoft.transport.orataro.ui.activitystudentattendance

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseActivity
import com.edusunsoft.transport.orataro.base.BaseViewModel
import com.edusunsoft.transport.orataro.databinding.ActivityStudentAttendanceListBinding
import com.edusunsoft.transport.orataro.ui.activitymaps.MapsActivity2
import com.edusunsoft.transport.orataro.utils.Controller
import kotlinx.android.synthetic.main.app_toolbar_layout.view.*
import kotlinx.android.synthetic.main.no_data_found_layout.view.*
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.*

class StudentAttendanceListActivity : BaseActivity(), StudentAttendanceListNavigator {

    private lateinit var activityStudentAttendanceListBinding: ActivityStudentAttendanceListBinding
    private lateinit var studentlistViewModel: StudentAttendanceListViewModel

    companion object {

        // variable declaration for display student list on the base of pickuppointid
        var PICKUPPOINTID: String = "PickupPointId"
        var TITLE: String = ""

        fun getInstance(activity: Activity, pickuppointId: String, title: String): Intent {
            val intent = Intent(activity, StudentAttendanceListActivity::class.java)
            intent.putExtra(PICKUPPOINTID, pickuppointId)
            intent.putExtra(TITLE, title)
            return intent
        }

        // variable declaration for jsonbejct to take attendance
        lateinit var jsonobjforstudttendance: RequestBody

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityStudentAttendanceListBinding = DataBindingUtil.setContentView(this, R.layout.activity_student_attendance_list)
        PICKUPPOINTID = intent.getStringExtra(PICKUPPOINTID)
        TITLE = intent.getStringExtra(TITLE)
        studentlistViewModel = StudentAttendanceListViewModel(this, this, PICKUPPOINTID)
        activityStudentAttendanceListBinding.toolbar.mBaseViewModel = BaseViewModel(this, this)
        setToolbar(activityStudentAttendanceListBinding.toolbar, TITLE, true)
        activityStudentAttendanceListBinding.toolbar.rlDummy.visibility = View.GONE
        activityStudentAttendanceListBinding.mstudentattendancevieModel = studentlistViewModel

        // take attendance only if trip is Running or started successfully
        if (Controller.isRunningTrip) {
            if (studentlistViewModel.sattendanceList?.size!! > 0) {
                activityStudentAttendanceListBinding.toolbar.imgCorrect.visibility = View.VISIBLE
            } else {
                activityStudentAttendanceListBinding.toolbar.imgCorrect.visibility = View.GONE
            }
        } else {
            activityStudentAttendanceListBinding.toolbar.imgCorrect.visibility = View.GONE
        }

        activityStudentAttendanceListBinding.toolbar.imgCorrect.setOnClickListener { v ->

            jsonobjforstudttendance = Controller.convertArraylistToJsonArrayForStudentAttendance(StudentAttendanceListAdapter.SelectedStudentList, MapsActivity2.BUSROUTEID, Controller.BusTripLogID, GetCurrentDate())
            studentlistViewModel.TakeAttendancee()

        }

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }



    fun GetCurrentDate(): String {

        val c = Calendar.getInstance().getTime()

        val CurrntDate = SimpleDateFormat("yyyy-MM-dd")
        return CurrntDate.format(c)

    }

    override fun FinishActivity() {
        this.finish()
    }

    override fun ShowNoDataFoundLayout() {

        activityStudentAttendanceListBinding.lylNoDataFound.visibility = View.VISIBLE
        activityStudentAttendanceListBinding.rvRoutList.visibility = View.GONE
        activityStudentAttendanceListBinding.lylNoDataFound.txt_message.text = this.resources.getString(R.string.studentsnotavailable)
        activityStudentAttendanceListBinding.toolbar.lylToolbar.img_correct.visibility = View.GONE

    }

    override fun ShowStudentListLayout() {

        activityStudentAttendanceListBinding.lylNoDataFound.visibility = View.GONE
        activityStudentAttendanceListBinding.rvRoutList.visibility = View.VISIBLE

    }
    

}
