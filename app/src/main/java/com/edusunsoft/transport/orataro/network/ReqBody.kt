//package com.edusunsoft.transport.orataro.network
//
//import com.edusunsoft.transport.orataro.network.CommonElements.Companion.SoapBody
//import com.edusunsoft.transport.orataro.network.ElementsAndSoapAction.Companion.mLoginAction
//import com.edusunsoft.transport.orataro.ui.activitylogin.LoginReqModel
//import org.simpleframework.xml.Element
//import org.simpleframework.xml.Root
//
//@Root(name = SoapBody)
//class ReqBody {
//
//    @Element(name = mLoginAction, required = false)
//    var mLoginReqModel: LoginReqModel
//
//    constructor(loginReqModel: LoginReqModel) {
//        this.mLoginReqModel = loginReqModel
//    }
//}