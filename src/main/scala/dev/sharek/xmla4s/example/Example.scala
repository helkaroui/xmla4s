package dev.sharek.xmla4s.example

import cats.effect.{ExitCode, IO, IOApp}
import dev.sharek.xmla4s.clients.{XmlaClientBuilder, XmlaClientConfig}
import dev.sharek.xmla4s.soap.SoapRequest
import org.http4s.Headers

import scala.concurrent.ExecutionContextExecutor
import CatsDebug._
object Example extends IOApp {

  val conf = XmlaClientConfig("http://httpbin.org/anything")

  implicit val context: ExecutionContextExecutor = scala.concurrent.ExecutionContext.global

  val client = XmlaClientBuilder(conf).makeClient()

  import dev.sharek.xmla4s.soap.SoapTranscoders._

  val res = client.use { client =>
    val req = SoapRequest(<header1></header1>, <content></content>)
    client.post("discover", req, Headers.empty)
  }.debug.as(ExitCode.Success)

  override def run(args: List[String]): IO[ExitCode] = res
}


object CatsDebug {
  implicit class Implicits[A](io: IO[A]) {
    def debug: IO[A] = io.map { value => println(s"[${Thread.currentThread().getName}] $value"); value }
  }
}