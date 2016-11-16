package example

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import com.github.aimmoth.scala.compiler.jetty.ScalaJsCompiler
import javax.servlet.http.HttpServlet
import scala.io.Source
import com.github.aimmoth.scala.compiler.jetty.Optimizer

@WebServlet(name = "javascriptCompiler", urlPatterns = Array("/javascript.js"))
class ScalaCompiler extends HttpServlet {
  
  val extraLibs = Set("scalajs-dom_sjs0.6_2.11-0.9.0.jar")
  
  override def doGet(request : HttpServletRequest, response : HttpServletResponse) = {
    
    val is = request.getServletContext.getResourceAsStream("/scalajs/example/Main.scala")
    
    val source = Source.fromInputStream(is).mkString
    
    val compiler = new ScalaJsCompiler
    response.getWriter.println(compiler.compileScalaJsString(request.getServletContext, source, Optimizer.Fast,  "/WEB-INF/lib/", extraLibs))
  }
}
