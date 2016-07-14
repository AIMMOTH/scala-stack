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
    
    import scala.collection.JavaConversions._ 
    
    val compiler = new ScalaJsCompiler
    val scripts = request.getServletContext.getResourcePaths("/scalajs")
    val example = request.getServletContext.getResourcePaths("/scalajs/example")
    val paths = request.getServletContext.getResourcePaths("/scalajs/example")
    response.getWriter.println(compiler.compileScalaJsString(this.getClass, source, Optimizer.Fast, "/WEB-INF/lib/"))
  }
    
}