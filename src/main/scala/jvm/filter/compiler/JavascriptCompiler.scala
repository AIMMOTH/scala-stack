package jvm.filter.compiler

import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import com.github.aimmoth.fiddle.ScalaJsCompiler
import com.github.aimmoth.fiddle.Optimizer
import scala.io.Source
import org.slf4j.LoggerFactory

object JavascriptCompiler {

  private lazy val log = LoggerFactory.getLogger(getClass)

  private lazy val sjsVersion = "sjs0.6"
  private lazy val scalaVersion = "2.11"
  private lazy val versions = sjsVersion + "_" + scalaVersion // "sjs0.6_2.11"

  // Ending slash is important!
  private lazy val scalaJsSource = "/scalajs-source/"
  private lazy val relativeJarPath = "/WEB-INF/lib/"

  /*
   * Important! These must be compiled to Scala JS!
   */
  private lazy val additionalLibs = List(
    s"scalajs-jquery_$versions-0.9.0.jar",
    s"scalatags_$versions-0.6.0.jar",
    s"scalajs-dom_$versions-0.9.1.jar",
    s"sourcecode_$versions-0.1.1.jar",
    s"scala-parser-combinators_$versions-1.0.2.jar",
    s"upickle_$versions-0.4.1.jar"
    )

  def apply(request: HttpServletRequest, response: HttpServletResponse) = {

    import scala.collection.JavaConversions._

    // Mutable??
    def findSource(path: String): scala.collection.mutable.Set[String] =
      request.getServletContext.getResourcePaths(path).partition(_.endsWith("/")) match {
        case (folders, files) => files ++ (folders flatMap findSource)
      }

    def read(file: String) = {
      log.debug(s"Adding $file to Scala JS compilation.")

      request.getServletContext.getResourceAsStream(file) match {
        case is => Source.fromInputStream(is).mkString
      }
    }

    findSource(scalaJsSource) map read match {
      case sources =>
        val optimizer = request.getRequestURI match {
          case uri if uri.endsWith(".min.js") => Optimizer.Full
          case _ => Optimizer.Fast
        }

        new ScalaJsCompiler match {
          case compiler =>
            compiler.compileScalaJsStrings(request.getServletContext, sources.toList, optimizer, relativeJarPath, additionalLibs) match {
              case script: String => response.getWriter.println(script)
            }
        }
    }

  }
}
