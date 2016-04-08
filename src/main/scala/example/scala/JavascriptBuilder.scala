package example.scala

import scalatags.stylesheet.Sheet
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest

class JavascriptBuilder extends HttpServlet {

  override def doGet(request: HttpServletRequest, response: HttpServletResponse) = {

    response.getWriter().print(VirtualSjsCompiler().getOrElse("""<script>//empty</script>"""))
  }
}