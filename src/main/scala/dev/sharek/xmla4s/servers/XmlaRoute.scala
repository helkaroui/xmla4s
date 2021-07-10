package dev.sharek.xmla4s.servers

import cats.effect.IO
import org.http4s._

class XmlaRoute(soapRoute: SoapRoute) extends Route[IO] {

  def route[Req, Res](xmlaHandler: (IO[Req]) => IO[Response[IO]])(implicit decoder: EntityDecoder[IO, Req]): HttpRoutes[IO] =
    soapRoute.route[Req, Res](xmlaHandler)
}

object XmlaRoute {

  def apply(uri: Uri): XmlaRoute = new XmlaRoute(SoapRoute(uri))

}