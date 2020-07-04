package com.edusunsoft.transport.orataro.ui.activitydashboard

data class RunningBusTrip(val BusTripLogID: String = "",
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


data class RouteItem(val RouteName: String = "",
                     val Latitude: Double = 0.0,
                     val PickupPointID: String = "",
                     val PointName: String = "",
                     val Longitude: Double = 0.0,
                     val Route: String = "")


data class CheckRunningbBusTripResModel(val status_code: Int = 0,
                                        val data: Data,
                                        val message: String = "")


data class StudentInfoItem(val EmergencyContact1: String = "",
                           val EmergencyContact2: String = "",
                           val PickupPerson1: String = "",
                           val StatusTerm: String = "",
                           val PickupPerson2: String = "",
                           val DropPointID: String = "",
                           val photo: String = "",
                           val Standard: String = "",
                           val Division: String = "",
                           val PickupPointID: String = "",
                           val StudentName: String = "",
                           val StudentID: String = "")


data class Data(val StudentInfo: List<StudentInfoItem>?,
                val RunningBusTrip: RunningBusTrip,
                val Route: List<RouteItem>?)


