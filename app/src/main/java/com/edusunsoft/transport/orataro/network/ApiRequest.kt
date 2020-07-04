package com.edusunsoft.transport.orataro.network

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.edusunsoft.transport.orataro.dialog.LoadingDialog
import com.edusunsoft.transport.orataro.ui.activitylogin.LoginActivity
import com.edusunsoft.transport.orataro.utils.AppPreference
import com.edusunsoft.transport.orataro.utils.Connectivity
import com.edusunsoft.transport.orataro.utils.Controller
import com.edusunsoft.transport.orataro.utils.Toaster
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class ApiRequest(var context: Context, var isShowLoading: Boolean) {
    suspend fun <T : Any> apiRequest(call: Deferred<T>, callBack: NetworkCallBack) {
        if (Connectivity.isNetworkConnected(context)) {
            GlobalScope.launch(Dispatchers.Main) {

                val response = call.await()
                try {
                    val finalResponse = (response as Response<*>)

                    if (response.isSuccessful) {
                        callBack.onSuccess(finalResponse.body())
//                        response.code() == 401; callBack.onError("Unauthorised request")
//                        response.code() == 404; callBack.onError("Service unavailable")
                    } else {
                        LoadingDialog.hideLoading()
                        Toaster.showShortToast(context, finalResponse.message().toString())
                        callBack.onError(finalResponse.message().toString())
                        Controller.RedirectToLoginActivity(context)
                    }
                } catch (e: Exception) {
                    e.message?.let { callBack.onError(it) }
                }
            }
        } else {
            callBack.onError("Check Your Internet Connection")
            return
        }
    }
}