package com.edusunsoft.transport.orataro.network

import com.edusunsoft.transport.orataro.ui.activitydashboard.CheckRunningbBusTripResModel
import com.edusunsoft.transport.orataro.ui.activityinstantmessage.SendMessageResponseModel
import com.edusunsoft.transport.orataro.ui.activitylicense.DriverLDResModel
import com.edusunsoft.transport.orataro.ui.activitylogin.LoginReqModel
import com.edusunsoft.transport.orataro.ui.activitylogin.LoginResModel
import com.edusunsoft.transport.orataro.ui.activitymaps.*
import com.edusunsoft.transport.orataro.ui.activityprofile.GetDriverProfile
import com.edusunsoft.transport.orataro.ui.activityselectrout.SelectRouteResponseModel
import com.edusunsoft.transport.orataro.ui.activitystudentattendance.TakeAttendanceResModel
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService {

    @POST("DriverLogin")
    fun doLoginAsync(@Body loginModel: LoginReqModel): Deferred<Response<LoginResModel>>

    @POST("DriverLoginRoute")
    fun GetDriverRoute(): Deferred<Response<SelectRouteResponseModel>>

    @POST("GetRoute")
    fun GetRoute(@Body getroutelistreqmodel: GetRouteListReqModel): Deferred<Response<GetRouteListModel>>

    @POST("InsertBusTrip")
    fun InsertBusTrip(@Body insertBusTripReqModel: InsertBusTripReqModel): Deferred<Response<InsertBusTripResponseModel>>

    @POST("UpdateBusTripEndTime")
    fun CloseBusTrip(@Body closeTripReqModel: CloseTripReqModel): Deferred<Response<InsertBusTripResponseModel>>

    @POST("CheckRunningBusTrip")
    fun CheckRunningBusTrip(): Deferred<Response<CheckRunningbBusTripResModel>>

    @POST("SendMessage")
    fun SendMessagesToParent(@Body jsonObject: JSONObject?): Deferred<Response<SendMessageResponseModel>>

    @POST("GetDriverDocument")
    fun GetdriverLD(): Deferred<Response<DriverLDResModel>>

    @POST("BusStudentAttendanceInsert")
    fun TakeStudentAttendance(@Body body: RequestBody): Deferred<Response<TakeAttendanceResModel>>

    @POST("SaveBusRouteLiveLatLong")
    fun UpdateLiveLatLong(@Body updateLiveLatLongReqModel: UpdateLiveLatLongReqModel): Deferred<Response<UpdateLatLongResModel>>

    @POST("GetDriverInfo")
    fun GetdriverProfile(): Deferred<Response<GetDriverProfile>>

    @POST("SendPushNotification")
    fun sendNotification(@Body body: RequestBody?): Deferred<Response<NotificationResModel>>

}