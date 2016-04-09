package example.scala

import scala.collection.JavaConverters.asScalaSetConverter
import com.google.common.io.ByteStreams
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import java.util.zip.ZipFile
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletRequest
import scala.reflect.io.VirtualDirectory
import java.util.zip.ZipInputStream
import java.io.ByteArrayInputStream
import scala.reflect.io.Streamable
import scala.reflect.io.VirtualDirectory
import org.slf4j.LoggerFactory

/**
 * A webapp's all resources are placed in the  "classes" subfolder.
 * At webapp start this folder is read.
 */
class WebappInit extends ServletContextListener {

  val log = LoggerFactory.getLogger(getClass)

  /*
   * Reads files like "/unpacked_libs/unpacked-jar-folder/Class.class"
   * into byte array
   */
  private def toVirtual(f : Seq[(String, Array[Byte])]) = {
    log.info(s"Loading ${f.size} virtual files ...")
    f.map {
      case (file, b) =>
        val tokens = file.split("/")
        val dir = new VirtualDirectory(tokens.head, None)
        def r(parent : VirtualDirectory, folders : Array[String]) : VirtualDirectory = {
          if (folders.isEmpty) {
            parent
          } else {
            val p = parent.subdirectoryNamed(folders.head).asInstanceOf[VirtualDirectory]
            r(p, folders.tail)
          }
        }
        // Drop "unpacked_libs/unpacked-jar-folder"
        val folder = r(dir, tokens.dropRight(2).tail)
  
        val f = folder.fileNamed(tokens.last)
        
        if (f.name == "Object.class")
          log.info(s"${f.name} in ${f.canonicalPath}")
          
        val o = f.bufferedOutput
        o.write(b)
        o.close()
  
        dir
    }.seq
  }

  def toBytes(f : Seq[String]) = {
    log.info(s"Loading ${f.size} files to bytes ...")
    f.map {
      case file =>
        (file, ByteStreams.toByteArray(getClass.getResourceAsStream(file)))
    }
  }
    
  override def contextDestroyed(context :ServletContextEvent) = {
    log.info("kthxbye");
  }

  override def contextInitialized(contextEvent : ServletContextEvent) {

    contextEvent.getServletContext match {
      case context =>
        
        def getJarFiles(parent : String, path : String) = {
          val folder = List(parent, path).mkString("/")
          log.info(s"Reading Jar files in $folder")
          val jars = context.getResourcePaths(folder)
            .asInstanceOf[java.util.Set[String]]
            .asScala.filter(_.endsWith(".jar")).map(_.substring(parent.length)).toSeq
            
          log.info(s"Jar files ${jars.mkString(",")}")
          jars
        }
        /*
         * Reads resources on context path (src/main/webapp) and recursively returns
         * files 
         */
        def getCompiledFiles(parent : String, path : String) = {
          
          def recursive(resource : String) : Set[String] =
            if (resource.endsWith(".class") || resource.endsWith(".sjsir")) {
              Set(resource.substring(parent.length))
            } else {
              val dirs = context.getResourcePaths(resource).asInstanceOf[java.util.Set[String]]
              dirs.asScala.map(recursive).toSet.flatten
            }
          
          val folder = List(parent, path).mkString("/")
          log.info(s"Reading unpacked folder $folder")
          context.getResourcePaths(folder)
            .asInstanceOf[java.util.Set[String]]
            .asScala.map(recursive).toSeq.flatten
        }
        def unpackZips(files : Seq[(String, Array[Byte])]) : Seq[VirtualDirectory] = {
          log.info(s"Unzipping ${files.length} files ...")
          files.map {
            case (name, bytes) =>
              val in = new ZipInputStream(new ByteArrayInputStream(bytes))
              val entries = Iterator
                .continually(in.getNextEntry)
                .takeWhile(_ != null)
                .map{
                  case e =>
                    (e, Streamable.bytes(in))
                }
              val dir = new VirtualDirectory(name, None)
              for {
                (e, data) <- entries
                if !e.isDirectory
              } {
                val tokens = e.getName.split("/")
                val dir = new VirtualDirectory(tokens.head, None)
                def r(parent : VirtualDirectory, folders : Array[String]) : VirtualDirectory = {
                  if (folders.isEmpty) {
                    parent
                  } else {
                    val p = parent.subdirectoryNamed(folders.head).asInstanceOf[VirtualDirectory]
                    r(p, folders.tail)
                  }
                }
                val folder = r(dir, tokens.dropRight(1).tail)
            
                val f = folder.fileNamed(tokens.last)
                
                if (f.name == "Object.class")
                  log.info(s"${f.name} in ${f.canonicalPath}")
                  
                val o = f.bufferedOutput
                o.write(bytes)
                o.close()
            
                dir
              }
              log.info(s"Done reading ${dir.canonicalPath}")
              dir
          }
        }
        
        if (JarFiles.classes.isEmpty) {
          toBytes(getCompiledFiles("/WEB-INF/classes", "unpacked_libs")) match {
            case bytesSeq =>
              val virtual = toVirtual(bytesSeq) 
              val zips = unpackZips(toBytes(getJarFiles("/WEB-INF/classes", "libs")))
              JarFiles.classes = Some(Tuple2(bytesSeq, virtual ++ zips))
          }
        }

        if (JarFiles.sourceFiles.isEmpty) {
          // Test!
          JarFiles.sourceFiles = Some(toBytes(getCompiledFiles("/WEB-INF/classes", "example/scalajs")))
        }
    }
  }
}

object JarFiles {

   val log = LoggerFactory.getLogger("JarFiles")

  type classesAsBytes = Seq[(String, Array[Byte])]
  type classesAsVirtualFiles = Seq[VirtualDirectory]
  
  var classes : Option[Tuple2[classesAsBytes, classesAsVirtualFiles]] = None
  var sourceFiles : Option[Seq[(String, Array[Byte])]] = None

}