package dev.sharek.xmla4s.servers

import org.http4s.{EntityDecoder, HttpRoutes, Response}

trait Route[F[_]] {
  def route[Req, Res](soapHandler: (F[Req]) => F[Response[F]])(implicit decoder: EntityDecoder[F, Req]): HttpRoutes[F]
}
