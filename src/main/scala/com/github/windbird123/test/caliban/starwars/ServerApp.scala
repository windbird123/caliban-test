package com.github.windbird123.test.caliban.starwars

import caliban.ZHttpAdapter
import zhttp.http._
import zhttp.service.Server
import zio.clock.Clock
import zio.console.Console
import zio.magic._
import zio.{ App, ExitCode, Has, URIO, ZIO }

object ServerApp extends App {
  val routes = for {
    interpreter <- MyApi.api.interpreter
    http = Http.route[Request] {
      case _ -> Root / "api" / "graphql" =>
        ZHttpAdapter.makeHttpService(interpreter)
    }
  } yield http

  val app: ZIO[Console with Clock with Has[MyService], Throwable, Unit] = for {
    http <- routes
    _    <- Server.start(9000, http)
  } yield ()

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    app.injectCustom(MyService.live).forever.exitCode
}
