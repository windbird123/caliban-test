package com.github.windbird123.test.caliban.srp

import com.github.windbird123.test.caliban.srp.SrpData.{ Engine, SrpResult }
import zio.{ Function0ToLayerSyntax, Has, UIO, URLayer, ZIO }

trait SrpService {
  def getResults(engine: Option[Engine]): UIO[List[SrpResult]]
}

object SrpService {
  def getResults(engine: Option[Engine]): ZIO[Has[SrpService], Nothing, List[SrpResult]] =
    ZIO.serviceWith[SrpService](_.getResults(engine))

  val live: URLayer[Any, Has[SrpService]] = SrpServiceLive.toLayer[SrpService]
}

case class SrpServiceLive() extends SrpService {
  override def getResults(engine: Option[Engine]): UIO[List[SrpResult]] = UIO(
    SrpData.sampleResults.filter { r =>
      if (engine.isEmpty) true
      else (engine.get == r.engine)
    }
  )
}
