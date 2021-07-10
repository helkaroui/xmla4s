package dev.sharek.xmla4s.clients

import cats.effect.IO
import dev.sharek.xmla4s.soap.SOAPAction
import org.http4s._
import org.http4s.client.Client
import org.typelevel.ci.CIString

class XmlaClient(client: SoapClient, config: XmlaClientConfig) {

  lazy val uri: Uri = Uri.unsafeFromString(config.endpoint)
  lazy val defaultHeaders: Headers = Headers(
    Header.Raw(CIString("Content-Type"), "application/soap+xml; charset=utf-8"),
  )

  def post[Req, Res](action: SOAPAction, request: Req, headers: Headers)(implicit encoder: EntityEncoder[IO, Req], decoder: EntityDecoder[IO, Res]): IO[Res] =
    client.post[Req, Res](uri, request, defaultHeaders ++ headers.put(Header.Raw(CIString(SoapHeaders.SOAPAction), action)))

  def post[Req, Res](action: SOAPAction, request: Req, headers: Headers, errorHandler: Response[IO] => IO[Throwable])(implicit encoder: EntityEncoder[IO, Req], decoder: EntityDecoder[IO, Res]): IO[Res] =
    client.post[Req, Res](uri, request, defaultHeaders ++ headers.put(Header.Raw(CIString(SoapHeaders.SOAPAction), action)), errorHandler)
}

object XmlaClient {

  def apply(client: Client[IO], config: XmlaClientConfig): XmlaClient = new XmlaClient(new Http4sSoapClient(client), config)

  def apply(client: SoapClient, config: XmlaClientConfig): XmlaClient = new XmlaClient(client, config)

}