package dev.sharek.xmla4s.clients

import cats.effect.{IO, Resource}
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import org.http4s.client.middleware.Logger

import scala.concurrent.ExecutionContext

case class XmlaClientBuilder(
                              config: XmlaClientConfig,
                              timeouts: ClientTimeouts = DefaultTimeouts
                            ) {

  def makeClient()(implicit executionContext: ExecutionContext): Resource[IO, XmlaClient] = {
    BlazeClientBuilder[IO](executionContext)
      .withIdleTimeout(timeouts.idleTimeout)
      .withRequestTimeout(timeouts.requestTimeout)
      .resource
      .flatMap { client: Client[IO] =>
        Resource.pure(XmlaClient(Logger(logBody = true, logHeaders = true)(client), config))
      }
  }

}


object XmlaClientBuilder {

  def apply(config: XmlaClientConfig): XmlaClientBuilder = new XmlaClientBuilder(config)

  def apply(config: XmlaClientConfig, timeouts: ClientTimeouts): XmlaClientBuilder = new XmlaClientBuilder(config, timeouts)

}