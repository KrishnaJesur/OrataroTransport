package com.edusunsoft.transport.orataro.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.annotation.NonNull
import com.edusunsoft.transport.orataro.OrataroApplication
import com.edusunsoft.transport.orataro.R
import com.edusunsoft.transport.orataro.ui.activitylogin.LoginResModel
import com.google.gson.Gson

object AppPreference {
    private const val PREFERENCE_NAME = "APP_DATA"
    private const val key_isLogin = "IS_LOGIN"
    private const val KEY_ACCESS_TOKEN = "ACCESS_TOKEN"
    private const val key_userId = "USER_ID"
    private const val key_userName = "USER_NAME"
    private const val key_userMobile = "USER_MOBILE"
    private const val key_userProfile = "USER_PROFILE"
    private const val KEY_LOGIN_DATA = "LOGIN_DATA"

    fun setLogin(@NonNull isLogin: Boolean) {
        OrataroApplication.instance.getSharedPreferences(OrataroApplication.instance.resources.getString(R.string.app_name), MODE_PRIVATE).edit().putBoolean(key_isLogin, isLogin).apply()
    }

    fun getLogin(): Boolean {
        return OrataroApplication.instance.getSharedPreferences(OrataroApplication.instance.resources.getString(R.string.app_name), MODE_PRIVATE).getBoolean(key_isLogin, false)
    }

    fun setUserId(@NonNull userId: String) {
        OrataroApplication.instance.getSharedPreferences(OrataroApplication.instance.resources.getString(R.string.app_name), MODE_PRIVATE).edit().putString(key_userId, userId).apply()
    }

    fun getUserId(): String {
        return OrataroApplication.instance.getSharedPreferences(OrataroApplication.instance.resources.getString(R.string.app_name), MODE_PRIVATE).getString(key_userId, "")!!
    }


    fun saveAccessToken(context: Context, @NonNull token: String?) {
        context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE).edit()
                .putString(KEY_ACCESS_TOKEN, token).apply()
    }

    @NonNull
    fun loadAccessToken(context: Context): String? {
        return context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
                .getString(KEY_ACCESS_TOKEN, "")
    }

    // Set and Get  UserName
    fun setUserName(username: String) {
        OrataroApplication.instance.getSharedPreferences(OrataroApplication.instance.resources.getString(R.string.app_name), MODE_PRIVATE).edit().putString(key_userName, username).apply()
    }

    fun getUserName(): String {
        return OrataroApplication.instance.getSharedPreferences(OrataroApplication.instance.resources.getString(R.string.app_name), MODE_PRIVATE).getString(key_userName, "")!!
    }

    // Set and Get  UserName
    fun setUserMobile(usermobile: String) {
        OrataroApplication.instance.getSharedPreferences(OrataroApplication.instance.resources.getString(R.string.app_name), MODE_PRIVATE).edit().putString(key_userMobile, usermobile).apply()
    }

    fun getUserMobile(): String {
        return OrataroApplication.instance.getSharedPreferences(OrataroApplication.instance.resources.getString(R.string.app_name), MODE_PRIVATE).getString(key_userMobile, "")!!
    }

    // Set and Get  UserName
    fun setUserProfile(userprofile: String) {
        OrataroApplication.instance.getSharedPreferences(OrataroApplication.instance.resources.getString(R.string.app_name), MODE_PRIVATE).edit().putString(key_userProfile, userprofile).apply()
    }

    fun getUserProfile(): String {
        return OrataroApplication.instance.getSharedPreferences(OrataroApplication.instance.resources.getString(R.string.app_name), MODE_PRIVATE).getString(key_userProfile, "")!!
    }


    fun setPermissionDenied(context: Context, @NonNull isPermissionDenied: Boolean) {
//        SharedPreferenceUtil.putValue(Controller.SET_ERMISSION_DENIED, isPermissionDenied)
        context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE).edit()
                .putBoolean(Controller.SET_ERMISSION_DENIED, isPermissionDenied).apply()
    }

    fun isPermissionDenied(context: Context): Boolean {
        return context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
                .getBoolean(Controller.SET_ERMISSION_DENIED, false)
    }

    /**
     * Save parent data from login
     * */
    fun saveLoginData(context: Context, loginResModel: LoginResModel) {
        val parentData = Gson().toJson(loginResModel)
        context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE).edit()
                .putString(KEY_LOGIN_DATA, parentData).apply()
    }


    fun loadLoginData(context: Context): LoginResModel {
        return Gson().fromJson(
                context.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
                        .getString(KEY_LOGIN_DATA, ""), LoginResModel::class.java
        )
    }


}