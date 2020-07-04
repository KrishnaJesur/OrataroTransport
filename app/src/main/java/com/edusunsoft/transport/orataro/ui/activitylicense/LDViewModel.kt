package com.edusunsoft.transport.orataro.ui.activitylicense

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.recyclerview.widget.LinearLayoutManager
import com.edusunsoft.transport.orataro.base.BaseViewModel
import com.edusunsoft.transport.orataro.dialog.LoadingDialog
import com.edusunsoft.transport.orataro.network.ApiProvider
import com.edusunsoft.transport.orataro.network.ApiRequest
import com.edusunsoft.transport.orataro.network.NetworkCallBack
import com.edusunsoft.transport.orataro.utils.AppPreference
import com.edusunsoft.transport.orataro.utils.Controller
import com.edusunsoft.transport.orataro.utils.Toaster
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LDViewModel(private var navigator: LDNavigator, context: Context)
    : BaseViewModel<LDNavigator>(navigator, context), NetworkCallBack {

    var layoutManager: LinearLayoutManager
    var LDListAdapter: LDListAdapter
    var isLoading: ObservableBoolean = ObservableBoolean()
    var isShow: ObservableBoolean = ObservableBoolean(false)

    override fun onSuccess(response: Any?) {

        if (response is DriverLDResModel) {

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
                    navigator.onGetLDList(response)

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
    }

    init {

        getLDList()
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        LDListAdapter = LDListAdapter(this, ArrayList())

    }

    private fun getLDList() {

        LoadingDialog.showLoading(context)

        GlobalScope.launch(Dispatchers.Main) {
            ApiRequest(context, true).apiRequest(
                    ApiProvider.provideApi(context).GetdriverLD(),
                    this@LDViewModel
            )

        }

    }

    fun onLDItemClick(driverDocumentItem: DriverLDResModel.DriverDocumentItem) {
        navigator.onLDItemClick(driverDocumentItem)
    }


}