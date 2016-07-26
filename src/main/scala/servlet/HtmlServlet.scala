package servlet

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import shared._

import scalatags.stylesheet._

import javax.servlet.annotation.WebServlet

@WebServlet(name = "htmlServlet", urlPatterns = Array("/index.html"))
class HtmlServlet extends HttpServlet {

  override def doGet(request: HttpServletRequest, response: HttpServletResponse) =
    response.getWriter().print(Html(new Stylisch).render)

}