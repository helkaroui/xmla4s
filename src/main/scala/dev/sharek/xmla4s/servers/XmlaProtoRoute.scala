package dev.sharek.xmla4s.servers

import cats.effect.IO
import org.http4s.HttpRoutes

class XmlaProtoRoute(xmlaRoute: XmlaRoute) {

  def discoverRoute(): HttpRoutes[IO] = ???

  def excuteRoute(): HttpRoutes[IO] = ???

}
