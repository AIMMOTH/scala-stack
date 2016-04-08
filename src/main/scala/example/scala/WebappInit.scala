package example.scala

import java.util.logging.Logger
import scala.collection.JavaConverters.asScalaSetConverter
import com.google.common.io.ByteStreams
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import java.util.zip.ZipFile
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
    
class WebappInit extends HttpServlet {
 
  val log = Logger.getLogger(classOf[WebappInit].getName())

//  override def contextDestroyed(context :ServletContextEvent) = {
//    log.info("kthxbye");
//  }

//  override def contextInitialized(contextEvent : ServletContextEvent) {
  override def doPost(request: HttpServletRequest, response: HttpServletResponse) = doGet(request, response)
  override def doGet (request: HttpServletRequest, response: HttpServletResponse) = {
    
    request.getSession().getServletContext match {
      case context =>
        val f = context.getResourcePaths("/WEB-INF/classes/libs").asInstanceOf[java.util.Set[String]]
        JarFiles.files = f.asScala.map(_.substring("/WEB-INF/classes".length)).toSeq

        val source = context.getResourcePaths("/WEB-INF/classes/example/scalajs").asInstanceOf[java.util.Set[String]]
        JarFiles.source = source.asScala.map(_.substring("/WEB-INF/classes".length)).toSeq
        
        log.info("all files:" + JarFiles.files.mkString)
        log.info("all source:" + JarFiles.source.mkString)
    }
  }
}

object JarFiles {

  val log = Logger.getLogger("JarFiles")
  
  var files : Seq[String] = null
  var source: Seq[String] = null
  lazy val jarFiles : Seq[(String, Array[Byte])] = toBytes(files)
  lazy val sourceFiles : Seq[(String, Array[Byte])] = toBytes(source)
  
  private def toBytes(f : Seq[String]) = { f.map{
      case file => 
//        file -> new ZipFile(file)
        val b = ByteStreams.toByteArray(getClass.getResourceAsStream(file))
        log.info(s"Loading ${file} with size ${b.length}")
        file -> b
    }.seq
  }
  
}