package example

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import scalatags.Text.all._
import scalatags.stylesheet._

class Builder extends HttpServlet {

  override def doGet(request: HttpServletRequest, response: HttpServletResponse) = {

    val style = Sheet[Style]

    response.getWriter().print(Html(style).render)
  }
}