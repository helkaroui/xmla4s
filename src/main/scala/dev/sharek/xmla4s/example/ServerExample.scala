package dev.sharek.xmla4s.example

import cats.data.Kleisli
import cats.effect.{ExitCode, IO, IOApp}
import dev.sharek.xmla4s.servers.{ServerConfig, XmlaServer}
import org.http4s.{HttpRoutes, Request, Response}
import org.http4s.dsl.io._
import org.http4s.implicits._

import scala.concurrent.ExecutionContext.Implicits.global

object ServerExample extends IOApp {

  val helloWorldService = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name =>
      Ok(s"Hello, $name.")
  }.orNotFound

  val conf = ServerConfig("localhost", 8000)
  val server = XmlaServer(conf)
    .makeServer(helloWorldService)

  def run(args: List[String]): IO[ExitCode] =
    XmlaServer(conf)
      .makeServer(helloWorldService)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)

}
