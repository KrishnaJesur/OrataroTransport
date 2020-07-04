//package com.edusunsoft.transport.orataro.network
//
//import com.edusunsoft.transport.orataro.network.CommonElements.Companion.SoapBody
//import com.edusunsoft.transport.orataro.network.CommonElements.Companion.SoapEnvelope
//import org.simpleframework.xml.Element
//import org.simpleframework.xml.Namespace
//import org.simpleframework.xml.NamespaceList
//import org.simpleframework.xml.Root
//
//
//@Root(name = SoapEnvelope)
//@NamespaceList(
//        Namespace(reference = "http://www.w3.org/2001/XMLSchema-instance", prefix = "xsi"),
//        Namespace(reference = "http://www.w3.org/2001/XMLSchema", prefix = "xsd"),
//        Namespace(reference = "http://schemas.xmlsoap.org/soap/encoding/", prefix = "enc"),
//        Namespace(reference = "http://schemas.xmlsoap.org/soap/envelope/", prefix = "soap"))
//class ReqEnvelope internal constructor(@field:Element(name = SoapBody) private val body: ReqBody)