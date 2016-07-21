package servlet

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import shared._

import scalatags.stylesheet._

import javax.servlet.annotation.WebServlet

@WebServlet(name = "htmlBuilder", urlPatterns = Array("/index.scala", "/index.html"))
class Builder extends HttpServlet {

  private val style = new StyleSheet with Stylisch
  
  override def doGet(request: HttpServletRequest, response: HttpServletResponse) =
    response.getWriter().print(Html(style).render)

}