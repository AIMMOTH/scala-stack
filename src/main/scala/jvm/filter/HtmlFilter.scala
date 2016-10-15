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
import scalajs.shared.util.RequestUriParser
import java.lang.ProcessBuilder.Redirect

class HtmlFilter extends Filter {

  implicit val logger = LoggerBuilder(LoggerFactory.getLogger(getClass))

  override def doFilter(request : ServletRequest, response : ServletResponse, chain : FilterChain) = {

    try {
      implicit def toHttpRequest(request : ServletRequest) = request.asInstanceOf[HttpServletRequest]
      implicit def toHttpResponse(response : ServletResponse) = response.asInstanceOf[HttpServletResponse]

      logger.info(RequestUriParser(request.getRequestURI).toString)
      
      Route(request.getRequestURI) match {
        case None => chain.doFilter(request, response)
        case Some(redirect@Route.Redirect(uri, language)) => response.sendRedirect(redirect.path)
        case Some(Route.JavascriptCompiler()) => JavascriptCompiler(request, response)
        case Some(Route.Html(html)) => response.getWriter.println(html.toString)
        case Some(_) => response.sendRedirect(Route.redirect404.path)
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
