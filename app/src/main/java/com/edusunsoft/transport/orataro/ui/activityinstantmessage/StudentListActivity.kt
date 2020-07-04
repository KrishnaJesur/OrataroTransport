package com.edusunsoft.transport.orataro.ui.activityinstantmessage

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseActivity
import com.edusunsoft.transport.orataro.base.BaseViewModel
import com.edusunsoft.transport.orataro.databinding.ActivityStudentListBinding
import com.edusunsoft.transport.orataro.dialog.studentattendance.RecyclerviewCallbacks
import com.edusunsoft.transport.orataro.ui.activitymaps.GetRouteListModel
import com.edusunsoft.transport.orataro.ui.activitydashboard.DashboardActivity
import com.edusunsoft.transport.orataro.ui.activityselectrout.SelectRoutActivity
import com.edusunsoft.transport.orataro.utils.Controller
import com.edusunsoft.transport.orataro.utils.Toaster
import kotlinx.android.synthetic.main.app_toolbar_layout.view.*
import kotlinx.android.synthetic.main.no_data_found_layout.view.*
import org.json.JSONObject
import java.util.ArrayList

class StudentListActivity : BaseActivity(), StudentListNavigator {

    private lateinit var activityStudentListBinding: ActivityStudentListBinding
    private lateinit var studentlistViewModel: StudentListViewModel
    var StudentList = ArrayList<GetRouteListModel.StudentInfoItem>()

    companion object {
        fun getInstance(selectRoutActivity: SelectRoutActivity): Intent {
            return Intent(selectRoutActivity, StudentListActivity::class.java)
        }

        var BUSROUTEID: String = ""
        var MESSAGEFORPARENT: String = ""
        var jsonobj: JSONObject? = null

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        activityStudentListBinding = DataBindingUtil.setContentView(this, R.layout.activity_student_list)
        BUSROUTEID = intent.getStringExtra(resources.getString(R.string.busRouteid))
        studentlistViewModel = StudentListViewModel(this, this)
        activityStudentListBinding.toolbar.mBaseViewModel = BaseViewModel(this, this)
        setToolbar(activityStudentListBinding.toolbar, getString(R.string.select_student), true)
        activityStudentListBinding.studentlistViewModel = studentlistViewModel

        activityStudentListBinding.toolbar.imgSelectAll.visibility = View.VISIBLE
        activityStudentListBinding.toolbar.imgCorrect.visibility = View.VISIBLE
        activityStudentListBinding.toolbar.imgSelectAll.setOnClickListener { _ ->

            studentlistViewModel.selectall(StudentList)

        }

        activityStudentListBinding.toolbar.imgCorrect.setOnClickListener { onClick ->
            if (StudentListAdapter.SelectedStudentList.isEmpty()) {
                Toaster.showShortToast(this@StudentListActivity, this.resources.getString(R.string.studentselectionvalidation))
            } else {
                SendMessage()
            }

        }

    }

    private fun SendMessage() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.send_message_layout)
        val window: Window? = dialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawableResource(R.color.transparent)
        window?.setGravity(Gravity.CENTER)
        val autotextView = dialog.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        val btn_cancel = dialog.findViewById<TextView>(R.id.btn_cancel)
        val btn_send = dialog.findViewById<TextView>(R.id.btn_send)
        val languages = resources.getStringArray(R.array.default_messages)
        // Create adapter and add in AutoCompleteTextView
        val adapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1, languages)
        autotextView.setAdapter(adapter)
        Controller.MESSAGE = autotextView.text.toString()

        btn_cancel.setOnClickListener { _ ->
            dialog.dismiss()
        }

        btn_send.setOnClickListener { _ ->

            dialog.dismiss()
            jsonobj = Controller.convertArraylistToJsonArray(StudentListAdapter.SelectedStudentList, autotextView.text.toString())
            studentlistViewModel.SendMessagetoParent()

        }

        dialog.show()

    }


    override fun onStudentGetList(getRouteListModel: GetRouteListModel) {

        if (getRouteListModel.data.StudentInfo.isNotEmpty()) {

            studentlistViewModel.studentListAdapter.setData(getRouteListModel.data.StudentInfo)
            StudentList = getRouteListModel.data.StudentInfo
            studentlistViewModel.studentListAdapter.setOnClick(object : RecyclerviewCallbacks<GetRouteListModel.StudentInfoItem> {
                override fun onItemClick(position: Int, item: GetRouteListModel.StudentInfoItem, isChecked: Boolean) {
                }

            })

        } else {

            activityStudentListBinding.lylNoDataFound.txt_message.text = this.resources.getString(R.string.studentsnotavailable)
            studentlistViewModel.isShow.set(true)
            activityStudentListBinding.toolbar.lylToolbar.img_correct.visibility = View.GONE
            activityStudentListBinding.toolbar.lylToolbar.img_select_all.visibility = View.GONE
            activityStudentListBinding.toolbar.rlDummy.visibility = View.GONE

        }

    }

    override fun RedirectToLogin() {

    }

    override fun RedirectToDashboard() {
        startActivity(DashboardActivity.getInstance(this@StudentListActivity))
        finish()
    }

    override fun ChangeImageResource(checkBox: Int) {
        activityStudentListBinding.toolbar.imgSelectAll.setImageResource(checkBox)
    }

    override fun RedirectToRouteListActivity() {
//        startActivity(SelectRoutActivity.getInstance(this))
        finish()
    }


}
