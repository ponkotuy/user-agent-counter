package com.ponkotuy.ua

import scala.util.matching.Regex

sealed abstract class OS(val name: String, val regex: Regex) extends Ordered[OS] {
  override def compare(that: OS): Int = OS.ordering.compare(this, that)

  lazy val index: Int = OS.values.indexOf(this)
}

object OS {
  case object Windows10 extends OS("Windows 10", """Windows NT 10\.0;""".r)
  case object Windows8 extends OS("Windows 8.1", """Windows NT 6\.3;""".r)
  case object Windows7 extends OS("Windows 7", """Windows NT 6\.1;""".r)

  val values = Seq(Windows10, Windows8, Windows7)

  implicit val ordering: Ordering[OS] = Ordering.by(_.index)

  def find(str: String): Option[OS] = values.find(_.regex.findFirstMatchIn(str).isDefined)
}
