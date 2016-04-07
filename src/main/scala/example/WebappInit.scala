package example

import java.util.logging.Logger
import scala.collection.JavaConverters.asScalaSetConverter
import com.google.common.io.ByteStreams
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import java.util.zip.ZipFile
    
class WebappInit extends ServletContextListener {
 
  val log = Logger.getLogger(classOf[WebappInit].getName())

  override def contextDestroyed(context :ServletContextEvent) = {
    log.info("kthxbye");
  }

  override def contextInitialized(contextEvent : ServletContextEvent) {
    val f = contextEvent.getServletContext.getResourcePaths("/WEB-INF/classes/libs").asInstanceOf[java.util.Set[String]]
    JarFiles.files = f.asScala.map(_.substring("/WEB-INF/classes".length)).toSeq
    log.info("all files:" + JarFiles.files.mkString)
  }
}

object JarFiles {

  val log = Logger.getLogger("JarFiles")
  
  var files : Seq[String] = null
  lazy val jarFiles : Seq[(String, ZipFile)] = files.map{
      case file => 
        log.info(s"Loading ${file}")
        file -> new ZipFile(file)
//        file -> ByteStreams.toByteArray(getClass.getResourceAsStream(file))
    }.seq
  
}