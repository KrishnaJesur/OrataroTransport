package com.edusunsoft.transport.orataro.ui.activitylogin

import com.edusunsoft.transport.orataro.base.BaseNavigator

interface LoginNavigator : BaseNavigator {
    fun onLoginSuccess(loginResModel: LoginResModel)
    fun onLoginFailed(error: String)
    fun inValidUserId(error: String)
    fun inValidPassword(error: String)
    fun showPassword()
    fun hidePassword()
}