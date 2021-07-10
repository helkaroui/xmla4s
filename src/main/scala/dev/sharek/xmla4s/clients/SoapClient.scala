package dev.sharek.xmla4s.clients

import cats.effect.IO
import org.http4s._
import org.http4s.client.Client

trait SoapClient {

  def post[Req, Res](uri: Uri, request: Req, headers: Headers)(implicit encoder: EntityEncoder[IO, Req], decoder: EntityDecoder[IO, Res]): IO[Res]

  def post[Req, Res](uri: Uri, request: Req, headers: Headers, errorHandler: (Response[IO]) => IO[Throwable])(implicit encoder: EntityEncoder[IO, Req], decoder: EntityDecoder[IO, Res]): IO[Res]

}


class Http4sSoapClient(client: Client[IO]) extends SoapClient {

  override def post[Req, Res](uri: Uri, request: Req, headers: Headers)(implicit encoder: EntityEncoder[IO, Req], decoder: EntityDecoder[IO, Res]): IO[Res] = {
    val req = Request(Method.POST, uri, httpVersion = HttpVersion.`HTTP/1.1`, headers.put(), encoder.toEntity(request).body)
    client.expect(req)
  }

  override def post[Req, Res](uri: Uri, request: Req, headers: Headers, errorHandler: Response[IO] => IO[Throwable])(implicit encoder: EntityEncoder[IO, Req], decoder: EntityDecoder[IO, Res]): IO[Res] = {
    val req = Request(Method.POST, uri, httpVersion = HttpVersion.`HTTP/1.1`, headers, encoder.toEntity(request).body)
    client.expectOr(req)(errorHandler)
  }
}

object Http4sSoapClient {

  def apply(client: Client[IO]): Http4sSoapClient = new Http4sSoapClient(client)

}