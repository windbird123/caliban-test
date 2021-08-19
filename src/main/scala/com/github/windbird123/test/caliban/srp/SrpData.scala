package com.github.windbird123.test.caliban.srp

import com.github.windbird123.test.caliban.srp.SrpData.Engine.{ GOOGLE, NAVER }

object SrpData {
  sealed trait Engine

  object Engine {
    case object NAVER  extends Engine
    case object GOOGLE extends Engine
  }

  case class Info(rawHtml: String, urls: List[String])
  case class SrpResult(engine: Engine, query: String, info: Info)

  case class EngineArgs(engine: Option[Engine])

  val sampleResults = List(
    SrpResult(NAVER, "q1", Info("Naver-rawHtml1", List("N-url_1-1", "N-url_1-2"))),
    SrpResult(GOOGLE, "q1", Info("Google-rawHtml1", List("G-url_1-1"))),
    SrpResult(NAVER, "q2", Info("Naver-rawHtml2", List("N-url_2-1", "url_1-2"))),
    SrpResult(GOOGLE, "q2", Info("Google-rawHtml2", List("G-url_2-1")))
  )
}
