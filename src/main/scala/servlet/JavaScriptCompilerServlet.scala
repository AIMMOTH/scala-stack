package servlet

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

  private val sjsVersion = "sjs0.6"
  private val scalaVersion = "2.11"
  private val versions = sjsVersion + "_" + scalaVersion // "sjs0.6_2.11"

  // Ending slash is important!
  val scalaJsSource = "/scalajs/"
  val relativeJarPath = "/WEB-INF/lib/"
  val additionalLibs = List(s"scalajs-angulate_$versions-0.2.4.jar", s"scalajs-jquery_$versions-0.9.0.jar", s"scalatags_$versions-0.6.0.jar", s"scalajs-dom_$versions-0.9.1.jar", s"sourcecode_$versions-0.1.1.jar")

  override def doGet(request : HttpServletRequest, response : HttpServletResponse) = {

    import scala.collection.JavaConversions._

    // Mutable??
    def findSource(path : String) : scala.collection.mutable.Set[String] =
      request.getServletContext.getResourcePaths(path).partition(_.endsWith("/")) match {
        case (folders, files) =>
          files ++ (folders flatMap findSource)
      }

    def read(file : String) = {
      log.debug(s"Adding $file to Scala JS compilation.")

      val is = request.getServletContext.getResourceAsStream(file)
      Source.fromInputStream(is).mkString
    }

    val sources = findSource(scalaJsSource) map read

    val optimizer = request.getParameter("optimizer") match {
      case "full" => Optimizer.Full
      case _      => Optimizer.Fast
    }

    val compiler = new ScalaJsCompiler
    response.getWriter.println(compiler.compileScalaJsStrings(request.getServletContext, sources.toList, optimizer, relativeJarPath, additionalLibs))
  }
}
