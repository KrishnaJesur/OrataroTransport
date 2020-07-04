package com.edusunsoft.transport.orataro.ui.activityinstantmessage

data class ListOfMobileItem(val Mobileno: String = "")


data class Data(val ListOfMobile: List<ListOfMobileItem>?)


data class SendMessageResponseModel(val status_code: Int = 0,
                                    val data: Data,
                                    val message: String = "")


