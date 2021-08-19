package com.github.windbird123.test.caliban.srp

import caliban.GraphQL.graphQL
import caliban.schema.{ GenericSchema, Schema }
import caliban.{ GraphQL, RootResolver }
import com.github.windbird123.test.caliban.srp.SrpData._
import zio._

object SrpApi extends GenericSchema[Has[SrpService]] {
  case class Queries(
    results: EngineArgs => URIO[Has[SrpService], List[SrpResult]]
  )

  // schema 정의 순서 중요!
  implicit val engineSchema: Schema[Has[SrpService], Engine]         = gen[Engine]
  implicit val infoSchema: Schema[Has[SrpService], Info]             = gen[Info]
  implicit val engineArgsSchema: Schema[Has[SrpService], EngineArgs] = gen[EngineArgs]
  implicit val srpResultSchema: Schema[Has[SrpService], SrpResult]   = gen[SrpResult]

  val api: GraphQL[Has[SrpService]] = graphQL(
    RootResolver(
      Queries(args => SrpService.getResults(args.engine))
    )
  )

  def main(args: Array[String]): Unit =
    println(api.render)

}
