package com.edusunsoft.transport.orataro.ui.activityselectrout

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.LinearLayoutManager
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.adapters.RoutListAdapter
import com.edusunsoft.transport.orataro.base.BaseViewModel
import com.edusunsoft.transport.orataro.dialog.LoadingDialog
import com.edusunsoft.transport.orataro.network.ApiProvider
import com.edusunsoft.transport.orataro.network.ApiRequest
import com.edusunsoft.transport.orataro.network.NetworkCallBack
import com.edusunsoft.transport.orataro.ui.activitylogin.LoginActivity
import com.edusunsoft.transport.orataro.ui.activitylogin.LoginResModel
import com.edusunsoft.transport.orataro.utils.AppPreference
import com.edusunsoft.transport.orataro.utils.Controller
import com.edusunsoft.transport.orataro.utils.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SelectRoutViewModel(private var selectRoutNavigator: SelectRoutNavigator, context: Context) :
        BaseViewModel<SelectRoutNavigator>(selectRoutNavigator, context), NetworkCallBack {

    override fun onSuccess(response: Any?) {

        if (response is SelectRouteResponseModel) {

            when {

                response.status_code == 0 -> {

                    isLoading.set(false)
                    val message = response.message
                    Toaster.showShortToast(context, message)
                    LoadingDialog.hideLoading()

                }

                response.status_code == 1 -> {

                    Log.d("getRouteList", response.data.toString())
                    LoadingDialog.hideLoading()
                    isLoading.set(false)
                    selectRoutNavigator.onRouteGetList(response)

                }

                response.status_code == 2 -> {

                    isLoading.set(false)
                    val message = response.message
                    Toaster.showShortToast(context, message)
                    LoadingDialog.hideLoading()
                    Controller.RedirectToLoginActivity(context)

                }
            }
        }
    }

    override fun onError(errorBody: String) {

        LoadingDialog.hideLoading()
        Toaster.showShortToast(context, errorBody)
        AppPreference.setUserId("")
        AppPreference.setLogin(false)
        Intent(context, LoginActivity::class.java)


    }

    var layoutManager: LinearLayoutManager
    var routListAdapter: RoutListAdapter
    var isLoading: ObservableBoolean = ObservableBoolean()
    var isShow: ObservableBoolean = ObservableBoolean(false)

    init {

        getRoutList()
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        routListAdapter = RoutListAdapter(this, ArrayList())
        GetCurrentDate()

    }

    private fun getRoutList() {

        LoadingDialog.showLoading(context)

        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                    ApiProvider.provideApi(context).GetDriverRoute(),
                    this@SelectRoutViewModel
            )

        }

    }

    private fun refreshRoutList() {

        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, false).apiRequest(
                    ApiProvider.provideApi(context).GetDriverRoute(),
                    this@SelectRoutViewModel
            )
        }
    }

    fun onRoutItemClick(rout: SelectRouteResponseModel.RouteItem) {
        selectRoutNavigator.onRoutItemClick(rout)
    }

    fun onRefresh() {

        isLoading.set(true)
        refreshRoutList()

    }

    fun GetCurrentDate() {
        selectRoutNavigator.GetCurrentDate()
    }

}