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
   * Reads files like "/unpacked_libs/unpacked-jar-folder/package/Class.class"
   * into byte array
   */
  private def toVirtual(tuple : (String, Array[Byte]), drop : Integer = 2) = {
    tuple match {
      case (file, b) =>
        try {
          // Drop "unpackaged_libs" and unpacked jar folder name
          val tokens = file.split("/").drop(drop)

          val dir = new VirtualDirectory(tokens.head, None)

          def r(parent : VirtualDirectory, folders : Array[String]) : VirtualDirectory = {
            if (folders.isEmpty) {
              parent
            } else {
              val p = parent.subdirectoryNamed(folders.head).asInstanceOf[VirtualDirectory]
              r(p, folders.tail)
            }
          }
          // Drop filename
          val folder = r(dir, tokens.dropRight(1).tail)

          val f = folder.fileNamed(tokens.last)

          if (f.name == "Object.class")
            log.debug(s"Sample: ${f.name} in ${f.canonicalPath}")

          val o = f.bufferedOutput
          o.write(b)
          o.close()

          dir
        } catch {
          case e : UnsupportedOperationException =>
            log.error(s"dropping $drop on $file where tokens are ${file.split("/").mkString(",")}")
            throw e
        }
    }
  }
  def unpackZips(tuple : (String, Array[Byte])) : List[VirtualDirectory] = {
    tuple match {
      case (name, bytes) =>
        val in = new ZipInputStream(new ByteArrayInputStream(bytes))
        Iterator
          .continually(in.getNextEntry)
          .takeWhile(_ != null)
          .filter(!_.isDirectory())
          .map{ e => (e.getName, Streamable.bytes(in))}
          .map(toVirtual(_, 0))
          .toList
    }
  }

  def toBytes(file : String) = (file, ByteStreams.toByteArray(getClass.getResourceAsStream(file)))

  override def contextDestroyed(context :ServletContextEvent) = {
    log.info("kthxbye");
  }

  override def contextInitialized(contextEvent : ServletContextEvent) {

    contextEvent.getServletContext match {
      case context =>

        def getJarFiles(parent : String, path : String) = {
          val folder = List(parent, path).mkString("/")

          log.info(s"Reading Jar files in $folder")

          context.getResourcePaths(folder)
            .asInstanceOf[java.util.Set[String]]
            .asScala.filter(_.endsWith(".jar")).map(_.substring(parent.length)).toSeq
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

        if (JarFiles.classes.isEmpty) {
          getCompiledFiles("/WEB-INF/classes", "unpacked_libs").map(toBytes) match {
            case bytesSeq =>
              val virtual = bytesSeq.map(toVirtual(_, 3))
              val zips = getJarFiles("/WEB-INF/classes", "libs").map(toBytes).map(unpackZips).flatten
              JarFiles.classes = Some(Tuple2(bytesSeq, virtual ++ zips))
          }
        }

        if (JarFiles.sourceFiles.isEmpty) {
          // Test!
          JarFiles.sourceFiles = Some(getCompiledFiles("/WEB-INF/classes", "example/scalajs").map(toBytes))
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