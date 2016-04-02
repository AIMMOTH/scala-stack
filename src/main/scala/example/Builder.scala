package example

import java.util.logging.Logger

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import scalacss.Defaults._
import scalatags.Text.all._
import scalatags.Text.tags2.{style => style2, title => title2}
import scalatags.stylesheet._

class Builder extends HttpServlet {

  private val log = Logger.getLogger(classOf[Builder].getName)

  private val simple = Sheet[Simple]

  override def doGet(request: HttpServletRequest, response: HttpServletResponse) = {

    log.info(request.getPathInfo)

    val html = buildHtml()
    response.getWriter().print(html.render)
  }

  private def buildHtml = {
    
    html(
      head(
        title2("All Scala!"),
        style2(`type` := "text/css")(simple.styleSheetText),
        script(src := "sbt-for-scala-js-build-fastopt.js")),
      body(
        h1("All Scala!"),
        p(backgroundColor := "green")("Push the red button below for an alert!"),
        button(simple.redBackground)(onclick := "example.Alerter().click()")("Press me!"))
      )
  }
}