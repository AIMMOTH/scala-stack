package servlet

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import shared._

import scalatags.stylesheet._

import javax.servlet.annotation.WebServlet

class Four04 extends HttpServlet {

  override def doGet(request: HttpServletRequest, response: HttpServletResponse) =
      response.getWriter().print(Four04().toString)

}