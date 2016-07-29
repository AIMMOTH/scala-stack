package shared

import scalatags.Text.all._
import scalatags.Text._
import org.junit.Test
import org.junit.Assert

class TestRoute {

  @Test
  def testRoute : Unit = {
    List(Route(None), Route(Some("")), Route(Some("/")), Route(Some("/index.html"))) match {
      case routes =>
        Assert.assertTrue(routes.tail.forall(_ == routes.head))
        routes.head match {
          case html =>

            val content = html.render
            println(content)
            Assert.assertTrue(content contains "https://github.com/AIMMOTH/scala-stack/tree/jquery")
        }
    }

  }
}