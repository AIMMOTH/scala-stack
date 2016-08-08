package filter

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import shared._

import scalatags.stylesheet._

import javax.servlet.annotation.WebServlet
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.FilterConfig
import javax.ws.rs.GET
import filter.compiler.JavascriptCompiler

class HtmlFilter extends Filter {

  override def doFilter(request: ServletRequest, response: ServletResponse, chain : FilterChain) = {
    
    implicit def toHttpRequest(request : ServletRequest) = request.asInstanceOf[HttpServletRequest]
    implicit def toHttpResponse(response: ServletResponse) = response.asInstanceOf[HttpServletResponse]
    
    request.getRequestURI match {
      case uri if uri.startsWith("/api") => chain.doFilter(request, response) // Jersey
      case uri if uri.startsWith("/javascript") => JavascriptCompiler(request, response)
      case uri => Route(Some(uri)) match {
          case html => response.getWriter().print(html.toString)
        }
    }
  }

  def destroy(): Unit = {}

  def init(config: FilterConfig): Unit = {}

}
