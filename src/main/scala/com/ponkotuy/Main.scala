package com.ponkotuy

import com.ponkotuy.log.NginxAccess
import com.ponkotuy.ua.{Browser, OS, UserAgent}

import scala.collection.mutable
import scala.io.Source

object Main extends App {
  val os = mutable.Map[OS, Long]().withDefaultValue(0L)
  val browser = mutable.Map[Browser, Long]().withDefaultValue(0L)
  Source.stdin.getLines()
      .flatMap(NginxAccess.fromLine)
      .filter(_.userAgent.length > 1)
      .map { log => UserAgent.fromString(log.userAgent) }
      .foreach { ua =>
        ua.os.foreach { x => os.update(x, os(x) + 1) }
        ua.browser.foreach { x => browser.update(x, browser(x) + 1) }
      }
  println(os)
  println(browser)
}
