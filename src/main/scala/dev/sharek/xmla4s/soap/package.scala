package dev.sharek.xmla4s

package object soap {
  type SOAPAction = String

  final case class SoapParseException(private val message: String = "", private val cause: Throwable = None.orNull) extends Exception(message, cause)
}
