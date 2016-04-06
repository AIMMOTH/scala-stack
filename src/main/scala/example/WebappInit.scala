package example

import javax.servlet.ServletContextListener
import java.util.logging.Logger
import javax.servlet.ServletContextEvent
import java.io.InputStreamReader
import com.google.common.io.CharStreams
import com.google.common.base.Charsets
import com.google.common.io.Closeables
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import com.google.common.io.ByteStreams
    
class WebappInit extends ServletContextListener {
 
  val log = Logger.getLogger(classOf[WebappInit].getName())

  override def contextDestroyed(context :ServletContextEvent) = {
    log.info("kthxbye");
  }

  override def contextInitialized(contextEvent : ServletContextEvent) {
    val f = contextEvent.getServletContext.getResourcePaths("/WEB-INF/classes/lib").asInstanceOf[java.util.Set[String]]
    JarFiles.files = f.asScala.toSeq
    log.info("all files:" + JarFiles.files.mkString)
  }
}

object JarFiles {

  var files : Seq[String] = null
  lazy val jarFiles : Seq[(String, Array[Byte])] = files.map{
      case file => 
        file -> ByteStreams.toByteArray(getClass.getResourceAsStream(file))
    }.seq
  
}