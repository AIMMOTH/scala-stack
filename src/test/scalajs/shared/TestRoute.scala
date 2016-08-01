package shared

import scalatags.Text.all._
import scalatags.Text._
import org.junit.Test
import org.junit.Assert
import scala.xml.XML

class TestRoute {

  @Test
  def testRoute: Unit = {
    List(Route(None), Route(Some("")), Route(Some("/")), Route(Some("/index.html"))) match {
      case routes =>
        Assert.assertTrue(routes.tail.forall(_ == routes.head))
        routes.head match {
          case html =>
            val xml = XML.loadString(html.render)
            xml.child match {
              case Seq(head, body) =>
                Assert.assertEquals(head.label, "head")
                Assert.assertEquals(body.label, "body")
                Assert.assertTrue(body.descendant.find(element => element.label == "a" && element \@ "href" == "https://github.com/AIMMOTH/scala-stack/tree/jquery").isDefined)
            }
        }
    }

  }
}