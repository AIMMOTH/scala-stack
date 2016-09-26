package filter

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import shared._
import shared.html._

import scalatags.stylesheet._

import javax.servlet.annotation.WebServlet
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.FilterConfig
import javax.ws.rs.GET
import filter.compiler.JavascriptCompiler

import org.slf4j.LoggerFactory
import shared.util.JsLogger
import builder.LoggerBuilder

class HtmlFilter extends Filter {

  implicit val logger = LoggerBuilder(LoggerFactory.getLogger(getClass)) 

  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) = {

    try {
      implicit def toHttpRequest(request: ServletRequest) = request.asInstanceOf[HttpServletRequest]
      implicit def toHttpResponse(response: ServletResponse) = response.asInstanceOf[HttpServletResponse]

      logger.debug(request.getRequestURI)

      request.getRequestURI match {

        case uri if uri.startsWith("/javascript") => JavascriptCompiler(request, response)
        case uri if uri.startsWith("/api")        => chain.doFilter(request, response) // Jersey
        case "/favicon.ico"                       => chain.doFilter(request, response) // Icon
        /*
         * These resources should be bundled into the HTML output and not used as separate files
         */
        case uri if uri.startsWith("/js")         => chain.doFilter(request, response) // Javascript
        case uri if uri.startsWith("/css")        => chain.doFilter(request, response) // CSS
        case uri => Route(uri) match {
          case Right(html)    => response.getWriter.print(html.toString)
          case Left(redirect) => response.sendRedirect(redirect)
        }
      }
    } catch {
      case error: Throwable =>
        error.printStackTrace()
        response.getWriter.print(ServerError.InternalServerError().toString)
    }
  }

  def destroy(): Unit = {}

  def init(config: FilterConfig): Unit = {}

}
