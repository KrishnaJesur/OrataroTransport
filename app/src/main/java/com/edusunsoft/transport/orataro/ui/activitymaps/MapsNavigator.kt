package com.edusunsoft.transport.orataro.ui.activitymaps

interface MapsNavigator {

    fun onNotificationClick()

    fun onTripDetailsClick()

    fun onLiveLocationClick()

    fun onMenuClick()

    fun onStatusClick()

    fun onDirectionFound(directionsResModel: DirectionsResModel)

    fun onErrorOccur(error: String)

    fun onBackButtonClick()

    fun RedirectToLogin()

    fun onStartTrip()

    fun onCloseTrip()

    fun onMoveToCurrentLocation()
    fun drawPolyline()


}
