package com.edusunsoft.transport.orataro.network

import java.io.Reader

interface NetworkCallBack {

    fun onSuccess(response: Any?)

    fun onError(errorBody: String)

}