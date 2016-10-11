package com.ponkotuy.ua

case class UserAgent(os: Option[OS], browser: Option[Browser], elems: Seq[Element])

object UserAgent {
  def fromString(str: String): UserAgent = {
    val os = OS.find(str)
    val browser = Browser.find(str)
    val elems = Element.findAll(str)
    UserAgent(os, browser, elems)
  }
}
