package com.edusunsoft.transport.orataro.ui.activityinstantmessage

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.LinearLayoutManager
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.base.BaseViewModel
import com.edusunsoft.transport.orataro.dialog.LoadingDialog
import com.edusunsoft.transport.orataro.network.ApiProvider
import com.edusunsoft.transport.orataro.network.ApiRequest
import com.edusunsoft.transport.orataro.network.NetworkCallBack
import com.edusunsoft.transport.orataro.ui.activitylogin.LoginActivity
import com.edusunsoft.transport.orataro.ui.activitymaps.GetRouteListModel
import com.edusunsoft.transport.orataro.ui.activitymaps.GetRouteListReqModel
import com.edusunsoft.transport.orataro.ui.activitymaps.MapsViewModel
import com.edusunsoft.transport.orataro.utils.AppPreference
import com.edusunsoft.transport.orataro.utils.Controller
import com.edusunsoft.transport.orataro.utils.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StudentListViewModel(private var studentlistNavigator: StudentListNavigator, context: Context) :
        BaseViewModel<StudentListNavigator>(studentlistNavigator, context), NetworkCallBack {


    override fun onSuccess(response: Any?) {

        if (response is GetRouteListModel) {

            when {

                response.status_code == 0 -> {

                    isLoading.set(false)
                    val message = response.message
                    Toaster.showShortToast(context, message)
                    LoadingDialog.hideLoading()

                }

                response.status_code == 1 -> {

                    LoadingDialog.hideLoading()
                    isLoading.set(false)
                    studentlistNavigator.onStudentGetList(response)

                }

                response.status_code == 2 -> {

                    isLoading.set(false)
                    val message = response.message
                    Toaster.showShortToast(context, message)
                    LoadingDialog.hideLoading()
                    Controller.RedirectToLoginActivity(context)

                }
            }
        } else if (response is SendMessageResponseModel) {

            when {

                response.status_code == 0 -> {

                    val message = response.message
                    Toaster.showShortToast(context, message)
                    LoadingDialog.hideLoading()
                    Controller.FROMSTRING = "InstantMessages"
                    studentlistNavigator.RedirectToRouteListActivity()

                }

                response.status_code == 1 -> {

                    LoadingDialog.hideLoading()
                    val message = response.message
                    Toaster.showShortToast(context, message)
                    studentlistNavigator.RedirectToDashboard()

                }

                response.status_code == 2 -> {

                    val message = response.message
                    Toaster.showShortToast(context, message)
                    LoadingDialog.hideLoading()
                    Controller.RedirectToLoginActivity(context)

                }

                else -> {

                    Toaster.showShortToast(context, context.resources.getString(R.string.warningmesage))

                }

            }

        }

    }

    override fun onError(errorBody: String) {

        Toaster.showShortToast(context, errorBody)
        Controller.RedirectToLoginActivity(context)

    }

    var layoutManager: LinearLayoutManager
    var studentListAdapter: StudentListAdapter
    var isLoading: ObservableBoolean = ObservableBoolean()
    var isShow: ObservableBoolean = ObservableBoolean(false)

    init {

        getStudentList()
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        studentListAdapter = StudentListAdapter(this, ArrayList())

    }

    private fun getStudentList() {

        LoadingDialog.showLoading(context)
        MapsViewModel.model = GetRouteListReqModel(StudentListActivity.BUSROUTEID)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                    ApiProvider.provideApi(context).GetRoute(MapsViewModel.model),
                    this@StudentListViewModel
            )

        }

    }

    fun onRefresh() {
        isLoading.set(true)
        refreshRoutList()
    }

    private fun refreshRoutList() {
        MapsViewModel.model = GetRouteListReqModel(StudentListActivity.BUSROUTEID)
        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, false).apiRequest(
                    ApiProvider.provideApi(context).GetRoute(MapsViewModel.model),
                    this@StudentListViewModel
            )

        }

    }

    fun selectall(studentInfoItem: ArrayList<GetRouteListModel.StudentInfoItem>): ArrayList<GetRouteListModel.StudentInfoItem> {

        for (i in studentInfoItem) {

            if (i.isSelected) {
                i.isSelected = false
                studentlistNavigator.ChangeImageResource(R.drawable.untick_icon)
            } else {
                i.isSelected = true
                studentlistNavigator.ChangeImageResource(R.drawable.tick_icon)
            }
            notifyChange()

        }

        return studentInfoItem

    }

    fun SendMessagetoParent() {

        LoadingDialog.showLoading(context)

        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                    ApiProvider.provideApi(context).SendMessagesToParent(StudentListActivity.jsonobj),
                    this@StudentListViewModel
            )

        }

    }


}
