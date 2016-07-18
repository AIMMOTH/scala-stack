package example

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import fiddle.ScalaJsCompiler
import javax.servlet.http.HttpServlet
import fiddle.Optimizer
import scala.io.Source
import java.util.logging.Logger
import org.slf4j.LoggerFactory

@WebServlet(name = "javascriptCompiler", urlPatterns = Array("/javascript.js"))
class ScalaCompiler extends HttpServlet {

  val log = LoggerFactory.getLogger(getClass)
  
  val scalaJsSource = "/scalajs/example/"
  val relativeJarPath = "/WEB-INF/lib/"
  val additionalLibs = List("scalajs-angulate_sjs0.6_2.11-0.2.4.jar", "scalajs-jquery_sjs0.6_2.11-0.9.0.jar")
  
  override def doGet(request: HttpServletRequest, response: HttpServletResponse) = {

    import scala.collection.JavaConversions._

    val sources = request.getServletContext.getResourcePaths(scalaJsSource).filter(_.endsWith(".scala")).map {
      file =>

        log.debug(s"Adding $file to Scala JS compilation.")
        
        val is = request.getServletContext.getResourceAsStream(file)
        Source.fromInputStream(is).mkString
    }
    
    val optimizer = request.getParameter("optimizer") match {
      case "full" => Optimizer.Full
      case _ => Optimizer.Fast
    }

    val compiler = new ScalaJsCompiler
    response.getWriter.println(compiler.compileScalaJsStrings(request.getServletContext, sources.toList, optimizer, relativeJarPath, additionalLibs))
  }
}
