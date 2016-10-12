package com.ponkotuy

import com.ponkotuy.log.NginxAccess
import com.ponkotuy.ua.{Browser, OS, UserAgent}
import is.tagomor.woothee.Classifier

import scala.collection.mutable
import scala.io.Source

object Main extends App {
  val os = mutable.Map[String, Long]().withDefaultValue(0L)
  val browser = mutable.Map[String, Long]().withDefaultValue(0L)
  Source.stdin.getLines()
      .flatMap(NginxAccess.fromLine)
      .filter(_.userAgent.length > 1)
      .filter(_.request.url != "/assets/proxy.pac")
      .map(_.userAgent)
      .map(woothee)
      .foreach { ua =>
        ua.os.foreach { x => os.update(x, os(x) + 1) }
        ua.browser.foreach { x => browser.update(x, browser(x) + 1) }
      }
  os.toList.sortBy(_._1).foreach(println)
  browser.toList.sortBy(_._1).foreach(println)

  private[this] def myParser(ua: String): Result = {
    val x = UserAgent.fromString(ua)
    Result(x.os.map(_.toString), x.browser.map(_.toString))
  }

  private[this] def woothee(ua: String): Result = {
    val x = Classifier.parse(ua)
    Result(Option(x.get("os")), Option(x.get("name")))
  }
}

case class Result(os: Option[String], browser: Option[String])
