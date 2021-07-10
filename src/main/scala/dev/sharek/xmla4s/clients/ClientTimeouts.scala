package dev.sharek.xmla4s.clients

import scala.concurrent.duration._

case class ClientTimeouts(
                           idleTimeout: Duration,
                           requestTimeout: Duration
                         )


object DefaultTimeouts extends ClientTimeouts(idleTimeout = 1.minutes, requestTimeout = 1.minutes)