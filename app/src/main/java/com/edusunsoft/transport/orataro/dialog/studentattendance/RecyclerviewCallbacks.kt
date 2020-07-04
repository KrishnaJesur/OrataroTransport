package com.edusunsoft.transport.orataro.dialog.studentattendance

interface RecyclerviewCallbacks<T> {
    fun onItemClick( position: Int, item: T,isChecked:Boolean)
}