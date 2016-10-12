package com.ponkotuy.ua

sealed abstract class Browser(val name: String, val version: String) extends Ordered[Browser] {
  override def compare(that: Browser): Int = Browser.ordering.compare(this, that)
}

object Browser {
  case class IE(override val version: String) extends Browser("IE", version)
  case class Edge(override val version: String) extends Browser("Edge", version)

  implicit val ordering: Ordering[Browser] = Ordering.by { b => (b.name, b.version) }

  def find(str: String): Option[Browser] =
    BrowserParser.values.toStream.flatMap(_.parse(str)).headOption
}

sealed abstract class BrowserParser {
  def parse(str: String): Option[Browser]
}

object BrowserParser {
  import Browser._

  case object IEParser extends BrowserParser {
    val regex = """Trident\/(\d)\.0""".r
    override def parse(str: String): Option[Browser] = {
      regex.findFirstMatchIn(str).map { mat =>
        val i = math.min(mat.group(1).toInt + 4, 11)
        IE(i.toString)
      }
    }
  }

  case object EdgeParser extends BrowserParser {
    val regex = """Edge\/(\d+)\.""".r
    override def parse(str: String): Option[Browser] = {
      regex.findFirstMatchIn(str).map { mat =>
        val i = mat.group(1).toInt
        Edge(i.toString)
      }
    }
  }

  val values = Seq(IEParser, EdgeParser)
}
