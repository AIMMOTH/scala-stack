package jvm.util

import scalatags.Text.all._
import org.slf4j.LoggerFactory
import jvm.builder.LoggerBuilder
import scalatags.Text._
import org.junit.Assert
import scala.xml.XML
import org.junit.Test
import scalajs.shared.Route

class TestRoute {

  implicit def logger = LoggerBuilder(LoggerFactory.getLogger(getClass))

  @Test
  def testRoute : Unit = {
    Route("/en-gb/index.min.html") match {
      case Some(html : Route.Html) =>
        val rendered = html.file.render
        val xml = XML.loadString(rendered)
        xml.child match {
          case Seq(head, body) =>
            Assert.assertEquals(head.label, "head")
            Assert.assertEquals(body.label, "body")
            Assert.assertTrue(body.descendant.find(element => element.label == "a" && element \@ "href" == "https://github.com/AIMMOTH/scala-stack/tree/jquery").isDefined)
        }
      case Some(_) => Assert.assertTrue(false)
      case None    => Assert.assertTrue(false)
    }

  }

  @Test
  def testRoutes : Unit = {
    Route("") match {
      case Some(redirect : Route.Redirect) => Assert.assertTrue(true)
      case _                               => Assert.assertTrue(false)
    }
    Route("/") match {
      case Some(html : Route.Redirect) => println(html.path)
      case _                       => Assert.assertTrue(false)
    }
    Route("/en-gb/404/") match {
      case Some(html : Route.Html) => println(html.file)
      case _                       => Assert.assertTrue(false)
    }
  }
}