package dev.sharek.xmla4s.proto

import cats.effect.IO
import dev.sharek.xmla4s.clients.XmlaClient
import org.http4s.{EntityDecoder, EntityEncoder}

class XmlaProtoClient(client: XmlaClient) {

  implicit val discoverEncoder: EntityEncoder[IO, DiscoverRequest] = ???
  implicit val discoverDecoder: EntityDecoder[IO, DiscoverResponse] = ???

  implicit val executeEncoder: EntityEncoder[IO, ExecuteRequest] = ???
  implicit val executeDecoder: EntityDecoder[IO, ExecuteResponse] = ???

  def discover(req: DiscoverRequest): IO[DiscoverResponse] =
    client.post[DiscoverRequest, DiscoverResponse](XmlaActions.Discover, req, client.defaultHeaders)

  def execute(req: ExecuteRequest): IO[ExecuteResponse] =
    client.post[ExecuteRequest, ExecuteResponse](XmlaActions.Execute, req, client.defaultHeaders)
}
