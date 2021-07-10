package dev.sharek.xmla4s.example

import java.io.ByteArrayInputStream

import dev.sharek.xmla4s.soap.XmlTranscoders

import scala.xml.{InputSource, XML}

object XmlParser extends App {

  val raw =
    """<?xml version='1.0' encoding='UTF-8'?>
      |<soap:Envelope xmlns:m="http://www.example.org" xmlns:soap="http://www.w3.org/2003/05/soap-envelope">
      |            <soap:Header>
      |      <header1></header1>
      |    </soap:Header><soap:Body>
      |      <content></content>
      |    </soap:Body>
      |          </soap:Envelope>""".stripMargin

  val saxParser = XmlTranscoders.saxFactory.newSAXParser()
  val source = new InputSource()
  source.setByteStream(new ByteArrayInputStream(raw.getBytes))
  println(XML.loadXML(source, saxParser))

}
