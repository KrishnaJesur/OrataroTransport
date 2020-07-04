package com.edusunsoft.transport.orataro.ui.activityinstantmessage

import com.edusunsoft.transport.orataro.ui.activitymaps.GetRouteListModel

interface StudentListNavigator {

    fun onStudentGetList(getRouteListModel: GetRouteListModel)
    fun RedirectToLogin()
    fun RedirectToDashboard()
    fun ChangeImageResource(checkBox: Int)
    fun RedirectToRouteListActivity()

}
