package com.edusunsoft.transport.orataro.ui.activitymaps

data class BusTripLog(val BusTripLogID: String = "",
                      val DriverID: String = "",
                      val BusID: String = "",
                      val InstituteID: String = "",
                      val DevicesName: String = "",
                      val EndTime: String = "",
                      val IsForPickup: Boolean = false,
                      val BusRouteID: String = "",
                      val StartTime: String = "",
                      val ClientID: String = "",
                      val RouteID: String = "",
                      val BusRouteDate: String = "")


data class InsertBusTripResponseModel(val status_code: Int = 0,
                                      val data: Data,
                                      val message: String = "")


data class Data(val BusTripLog: BusTripLog)


