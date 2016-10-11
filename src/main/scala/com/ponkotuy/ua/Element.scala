package com.ponkotuy.ua

import scala.util.matching.Regex

sealed abstract class Element(val name: String, val regex: Regex)

object Element {
  case object Wow64 extends Element("Windows 64bit, IE 32bit", "WOW64;".r)
  case object Win64 extends Element("IE 64bit", "Win64;".r)
  case object Touch extends Element("Touch", "Touch;".r)

  val values = Seq(Wow64, Win64, Touch)
  def findAll(str: String): Seq[Element] = values.filter { v => v.regex.findFirstMatchIn(str).isDefined }
}
