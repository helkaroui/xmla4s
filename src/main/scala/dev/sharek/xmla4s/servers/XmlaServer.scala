package dev.sharek.xmla4s.servers

import cats.effect.IO
import org.http4s.HttpApp
import org.http4s.blaze.server.BlazeServerBuilder

import scala.concurrent.ExecutionContext

class XmlaServer(serverConfig: ServerConfig) {

  def makeServer(xmlaApp: HttpApp[IO])(implicit executionContext: ExecutionContext): BlazeServerBuilder[IO] =
    BlazeServerBuilder[IO](executionContext)
      .withBanner(getBanner(serverConfig))
      .withHttpApp(xmlaApp)
      .bindHttp(serverConfig.port, serverConfig.host)

  private def getBanner(serverConfig: ServerConfig): Seq[String] =
    serverLogo ++ Seq(s" Host : ${serverConfig.host}", s" Port : ${serverConfig.port}")
  Seq("Hello", "you")
}


object XmlaServer {

  def apply(serverConfig: ServerConfig): XmlaServer = new XmlaServer(serverConfig)

}