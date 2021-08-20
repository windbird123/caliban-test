package com.github.windbird123.test.caliban.srp

import com.github.windbird123.test.caliban.srp.SrpData.{ SrpResult }
import zio.{ Function0ToLayerSyntax, Has, UIO, URLayer, ZIO }

trait SrpService {
  def getResults(query: Option[String]): UIO[List[SrpResult]]
}

object SrpService {
  def getResults(query: Option[String]): ZIO[Has[SrpService], Nothing, List[SrpResult]] =
    ZIO.serviceWith[SrpService](_.getResults(query))

  val live: URLayer[Any, Has[SrpService]] = SrpServiceLive.toLayer[SrpService]
}

case class SrpServiceLive() extends SrpService {
  override def getResults(query: Option[String]): UIO[List[SrpResult]] = UIO(
    SrpData.sampleResults.filter(r => if (query.isEmpty) true else (query.get == r.query))
  )
}
