package jvm.filter

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import scalajs.shared._
import scalajs.shared.html._

import scalatags.stylesheet._

import javax.servlet.annotation.WebServlet
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.FilterConfig
import javax.ws.rs.GET
import jvm.filter.compiler.JavascriptCompiler

import org.slf4j.LoggerFactory
import scalajs.shared.util.JsLogger
import jvm.builder.LoggerBuilder

class HtmlFilter extends Filter {

  implicit val logger = LoggerBuilder(LoggerFactory.getLogger(getClass))

  override def doFilter(request : ServletRequest, response : ServletResponse, chain : FilterChain) = {

    try {
      implicit def toHttpRequest(request : ServletRequest) = request.asInstanceOf[HttpServletRequest]
      implicit def toHttpResponse(response : ServletResponse) = response.asInstanceOf[HttpServletResponse]

      request.getRequestURI match {
        case uri if uri.startsWith("/javascript") => JavascriptCompiler(request, response)
        case uri => Route(uri) match {
          case Some(Right(html))    => response.getWriter.println(html.toString)
          case Some(Left(redirect)) => response.sendRedirect(redirect)
          case None                 => chain.doFilter(request, response)
        }
      }

    } catch {
      case error : Throwable =>
        error.printStackTrace()
        response.getWriter.print(ServerError.InternalServerError(Languages.default).toString)
    }
  }

  def destroy() : Unit = {}

  def init(config : FilterConfig) : Unit = {}

}
