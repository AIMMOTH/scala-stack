package example

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import fiddle.ScalaJsCompiler
import javax.servlet.http.HttpServlet
import fiddle.Optimizer

@WebServlet(name = "javascriptCompiler", urlPatterns = Array("/javascript.js"))
class ScalaCompiler extends HttpServlet {
  
    val source = """
package example
import scala.scalajs.js
import js.annotation._
import org.scalajs.dom
@JSExport
class Foo(val x: Int) {
  override def toString(): String = s"Foo($x)"
}
@JSExportAll
object HelloWorld extends js.JSApp {
  def main(): Unit = {
    println("Hello world!")
  }
  
  def alert = dom.window.alert("Hello!")
}
      """
    
  override def doGet(request : HttpServletRequest, response : HttpServletResponse) = {
    
    val compiler = new ScalaJsCompiler
    response.getWriter.println(compiler.compileScalaJsString(request.getServletContext, source, Optimizer.Fast,  "/WEB-INF/lib/"))
  }
}
