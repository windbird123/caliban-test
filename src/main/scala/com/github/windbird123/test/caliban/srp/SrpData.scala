package com.github.windbird123.test.caliban.srp

object SrpData {

  case class Info(rawHtml: String, urls: List[String])
  case class SrpResult(query: String, naver: Info, google: Info)

  case class QueryArgs(query: Option[String])

  val sampleResults = List(
    SrpResult("q1", Info("Naver-rawHtml1", List("N-url_1-1", "N-url_1-2")), Info("Google-rawHtml1", List("G-url_1-1"))),
    SrpResult("q2", Info("Naver-rawHtml2", List("N-url_2-1", "url_2-2")), Info("Google-rawHtml2", List("G-url_2-1")))
  )
}
