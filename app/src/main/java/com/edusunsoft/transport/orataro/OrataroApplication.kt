package com.edusunsoft.transport.orataro

import android.app.Application

open class OrataroApplication : Application() {


    companion object {
        lateinit var instance: OrataroApplication private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}