package dev.sharek.xmla4s.soap

sealed class SoapObject(headers: xml.NodeSeq, body: xml.NodeSeq)

case class SoapRequest(headers: xml.NodeSeq, body: xml.NodeSeq) extends SoapObject(headers, body)
case class SoapResponse(headers: xml.NodeSeq, body: xml.NodeSeq) extends SoapObject(headers, body)