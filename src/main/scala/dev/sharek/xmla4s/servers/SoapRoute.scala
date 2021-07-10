package dev.sharek.xmla4s.servers

import cats.effect.IO
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.headers.Allow
import org.typelevel.ci.CIString

class SoapRoute(uri: Uri) extends Route[IO] {
  lazy val defaultHeaders: Headers = Headers(
    Header.Raw(CIString("Content-Type"), "application/soap+xml; charset=utf-8"),
  )

  def route[Req, Res](soapHandler: (IO[Req]) => IO[Response[IO]])(implicit decoder: EntityDecoder[IO, Req]): HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req@POST -> uri.path => {
      val parsedRequest: IO[Req] = req.as[Req]
      soapHandler(parsedRequest).map(_.withHeaders(defaultHeaders))
    }

    case request if request.method != POST => MethodNotAllowed(Allow(Method.POST))
  }

}

object SoapRoute {

  def apply(uri: Uri): SoapRoute = new SoapRoute(uri)

}