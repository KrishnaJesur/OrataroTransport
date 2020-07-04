package com.edusunsoft.transport.orataro.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import com.edusunsoft.transport.orataro.ui.activitylogin.LoginActivity
import com.edusunsoft.transport.orataro.ui.activitymaps.GetRouteListModel
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class Controller {

    companion object {

        var studentDataStr = ""
        var SET_ERMISSION_DENIED = "set_permission_denied"
        var BusTripLogID: String = ""
        var BUSROUTEID: String = ""
        var BUSROUTENAME: String = ""
        var VEHICLEREGNO: String = ""
        var FROMSTRING: String = ""
        var MESSAGE: String = ""
        var jsonStr: String = ""
        var GCMIDStr: String = ""
        var contactsObj = JSONObject()
        var gcmidsObj = JSONObject()
        var isRunningTrip: Boolean = false
        var jsonObjforstuattendance = JSONObject()
        var isTakenAttendance: String = "false"

        fun GetCurrentTime(): String {

            val c = Calendar.getInstance().getTime()

            val curenttime = SimpleDateFormat("hh:mm")
            curenttime.format(c)

            return curenttime.format(c)

        }

        fun GetCurrentDate(): String {

            val c = Calendar.getInstance().getTime()

            val CurrntDate = SimpleDateFormat("yyyy-MM-dd")
            CurrntDate.format(c)

            return CurrntDate.format(c)

        }

        fun convertArraylistToJsonArray(selectedStudentList: ArrayList<String>, Message: String): JSONObject? {

            // declaration of jsonobj and jsonarray
            contactsObj = JSONObject()
            val contactsArray = JSONArray()

            //convert string arraylist into jsonarray / JsonObject
            try {
                for (i in 0 until selectedStudentList.size) {
                    val contact = JSONObject()
                    contact.put("Mobileno", selectedStudentList[i])
                    contactsArray.put(i, contact)
                }
                contactsObj.put("MobileNos", contactsArray)
                contactsObj.put("Message", Message)

            } catch (e: JSONException) { // TODO Auto-generated catch block
                e.printStackTrace()
            }

            jsonStr = contactsObj.toString()
            Log.e("CONTACTS", jsonStr) // adds only last array to json object

            return contactsObj


        }

        fun jsonRequestfornotification(studentgcmidlist: String, Message: String): RequestBody {

            // declaration of jsonobj and jsonarray
            gcmidsObj = JSONObject()
            val gcmidsArray = JSONArray()

            //convert string arraylist into jsonarray / JsonObject
            try {

//                studentDataStr = "$studentDataStr$studentgcmidlist"
                gcmidsObj.put("GCMIDs", studentgcmidlist)
                gcmidsObj.put("Message", Message)

            } catch (e: JSONException) { // TODO Auto-generated catch block
                e.printStackTrace()
            }

            GCMIDStr = gcmidsObj.toString()
            // requestbody need to be sent to remove navaluepair from jsonobject as request.
            val bodyRequest: RequestBody = RequestBody.create(MediaType.parse("application/json"), gcmidsObj.toString())
            return bodyRequest

        }

        fun RedirectToLoginActivity(context: Context) {

            AppPreference.setUserId("")
            AppPreference.saveAccessToken(context, "")
            AppPreference.setUserProfile("")
            AppPreference.setUserMobile("")
            AppPreference.setUserName("")
            AppPreference.setLogin(false)
            context.startActivity(Intent(context, LoginActivity::class.java))

        }

        fun convertArraylistToJsonArrayForStudentAttendance(sattendanceList: ArrayList<GetRouteListModel.StudentInfoItem>?, busrouteId: String, bustripLogId: String, createdDate: String): RequestBody {

            // declaration of jsonobj and jsonarray
            jsonObjforstuattendance = JSONObject()
            val contactsArray = JSONArray()

            //convert string arraylist into jsonarray / JsonObject
            try {
                if (sattendanceList != null) {
                    for (i in 0 until sattendanceList.size) {
                        val contact = JSONObject()
                        contact.put("StudentID", sattendanceList[i].StudentID)
                        if (sattendanceList[i].StatusTerm.equals("")) {
                            contact.put("StatusTerm", "Present")
                        } else {
                            contact.put("StatusTerm", sattendanceList[i].StatusTerm)
                        }

                        contactsArray.put(i, contact)

                    }

                }

                jsonObjforstuattendance.put("BusRouteID", busrouteId)
                jsonObjforstuattendance.put("BusTripLogID", bustripLogId)
                jsonObjforstuattendance.put("CreatedDate", createdDate)
                jsonObjforstuattendance.put("BusStudentAttendancelist", contactsArray)


            } catch (e: JSONException) { // TODO Auto-generated catch block
                e.printStackTrace()
            }

            jsonStr = jsonObjforstuattendance.toString()
            Log.e("studeattenObj1234", jsonStr) // adds only last array to json object

            val bodyRequest: RequestBody = RequestBody.create(MediaType.parse("application/json"), jsonObjforstuattendance.toString())
            return bodyRequest

        }

    }


}