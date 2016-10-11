package com.ponkotuy.log

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

import scala.util.Try

case class NginxAccess(
    ip: String,
    date: ZonedDateTime,
    request: Request,
    status: Int,
    bytes: Int,
    referer: String,
    userAgent: String)

object NginxAccess {
  val regex = """^([\d\.]+) - - \[(.+)\] "(.+)" (\d+) (\d+) "(.+)" "(.+)"$""".r
  val formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss ZZ", Locale.US)
  def fromLine(str: String): Option[NginxAccess] = {
    str match {
      case regex(ip, date, req, status, bytes, referer, ua) =>
        for {
          r <- Request.fromString(req)
          zoned <- Try { ZonedDateTime.parse(date, formatter) }.toOption
        } yield NginxAccess(ip, zoned, r, status.toInt, bytes.toInt, referer, ua)
      case _ => None
    }
  }
}

case class Request(method: Method, url: String, httpVer: String)

object Request {
  def fromString(str: String): Option[Request] = {
    Try {
      val Array(method, url, http) = str.split(' ')
      Request(Method.fromString(method).get, url, http)
    }.toOption
  }
}

sealed abstract class Method(val value: String)

object Method {
  case object GET extends Method("GET")
  case object HEAD extends Method("HEAD")
  case object POST extends Method("POST")
  case object PUT extends Method("PUT")
  case object PATCH extends Method("PATCH")
  case object DELETE extends Method("DELETE")
  case object OPTIONS extends Method("OPTIONS")
  case object TRACE extends Method("TRACE")
  case object LINK extends Method("LINK")
  case object UNLINK extends Method("UNLINK")

  val values = Seq(GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE, LINK, UNLINK)
  def fromString(str: String) = values.find(_.value == str)
}
