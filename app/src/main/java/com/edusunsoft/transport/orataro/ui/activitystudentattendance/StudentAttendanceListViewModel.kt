package com.edusunsoft.transport.orataro.ui.activitystudentattendance

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseViewModel
import com.edusunsoft.transport.orataro.dialog.LoadingDialog
import com.edusunsoft.transport.orataro.network.ApiProviderForApplicationJson
import com.edusunsoft.transport.orataro.network.ApiRequest
import com.edusunsoft.transport.orataro.network.NetworkCallBack
import com.edusunsoft.transport.orataro.ui.activitymaps.GetRouteListModel
import com.edusunsoft.transport.orataro.ui.activitymaps.MapsActivity
import com.edusunsoft.transport.orataro.ui.activitymaps.MapsActivity2
import com.edusunsoft.transport.orataro.ui.activitymaps.MapsViewModel
import com.edusunsoft.transport.orataro.utils.Controller
import com.edusunsoft.transport.orataro.utils.Controller.Companion.isTakenAttendance
import com.edusunsoft.transport.orataro.utils.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StudentAttendanceListViewModel(private var studentattendencelistNavigator: StudentAttendanceListNavigator, context: Context, pickuppointid: String) :
        BaseViewModel<StudentAttendanceListNavigator>(studentattendencelistNavigator, context), NetworkCallBack {

    fun TakeAttendancee() {

        LoadingDialog.showLoading(context)

        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                    ApiProviderForApplicationJson.provideApi(context).TakeStudentAttendance(StudentAttendanceListActivity.jsonobjforstudttendance),
                    this@StudentAttendanceListViewModel
            )

        }

    }

    var layoutManager: LinearLayoutManager? = null
    var studentAttendanceListAdapter: StudentAttendanceListAdapter? = null
    var sattendanceList: ArrayList<GetRouteListModel.StudentInfoItem>? = ArrayList()

    init {
        if (MapsViewModel.StudentList!!.size > 0) {
            // display list of student of pickup and drop for take attendance
            for (i in 0 until MapsViewModel.StudentList!!.size) {
                if (MapsActivity2.ISFORPICKUP == context.resources.getString(R.string.True)) {

                    if (MapsViewModel.StudentList!![i].PickupPointID == pickuppointid) {

                        sattendanceList?.add(MapsViewModel.StudentList!![i])
                        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        studentAttendanceListAdapter = StudentAttendanceListAdapter(this@StudentAttendanceListViewModel, sattendanceList)

                    }

                } else if (MapsActivity2.ISFORPICKUP == context.resources.getString(R.string.False)) {

                    if (StudentAttendanceListActivity.PICKUPPOINTID == MapsViewModel.StudentList!![i].DropPointID) {

                        sattendanceList?.add(MapsViewModel.StudentList!![i])
                        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                        studentAttendanceListAdapter = StudentAttendanceListAdapter(this@StudentAttendanceListViewModel, sattendanceList)

                    }

                }


            }

            if (sattendanceList!!.isEmpty()) {

                studentattendencelistNavigator.ShowNoDataFoundLayout()

            }

        }

    }

    override fun onSuccess(response: Any?) {

        if (response is TakeAttendanceResModel) {

            when {

                response.status_code == 0 -> {

                    Toaster.showShortToast(context, response.message)
                    LoadingDialog.hideLoading()

                }

                response.status_code == 1 -> {

                    isTakenAttendance = "true"
                    LoadingDialog.hideLoading()
                    Toaster.showShortToast(context, response.message)
                    studentattendencelistNavigator.FinishActivity()

                }

                response.status_code == 2 -> {

                    val message = response.message
                    Toaster.showShortToast(context, message)
                    LoadingDialog.hideLoading()
                    Controller.RedirectToLoginActivity(context)

                }

            }

        }

    }

    override fun onError(errorBody: String) {
        Toaster.showShortToast(context, errorBody)
        Controller.RedirectToLoginActivity(context)
    }

}
