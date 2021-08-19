package com.github.windbird123.test.caliban.srp

import caliban.ZHttpAdapter
import zhttp.http._
import zhttp.service.Server
import zio.magic._
import zio.{ App, ExitCode, Has, URIO, ZIO }

object SrpServer extends App {
  val routes = for {
    interpreter <- SrpApi.api.interpreter
    http = Http.route[Request] {
      case _ -> Root / "api" / "graphql" =>
        ZHttpAdapter.makeHttpService(interpreter)
    }
  } yield http

  val app: ZIO[Has[SrpService], Throwable, Unit] = for {
    http <- routes
    _    <- Server.start(9000, http)
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    app.inject(SrpService.live).forever.exitCode
}
