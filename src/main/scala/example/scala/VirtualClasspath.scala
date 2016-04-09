package example.scala

import scala.collection.JavaConverters._
import java.io._
import java.util.zip.ZipInputStream
import org.scalajs.core.tools.io._
import org.slf4j.LoggerFactory
import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.reflect.io.{Streamable, VirtualDirectory}
import java.util.zip.ZipInputStream
import org.scalajs.core.tools.io._
import org.slf4j.LoggerFactory
import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.reflect.io.{Streamable, VirtualDirectory}
import com.google.common.io.ByteStreams
import java.util.zip.ZipFile
import java.util.zip.ZipEntry

/**
  * Loads the jars that make up the classpath of the scala-js-fiddle
  * compiler and re-shapes it into the correct structure to satisfy
  * scala-compile and scalajs-tools
  */
class VirtualClasspath {

  val log = LoggerFactory.getLogger(getClass)

  /**
    * The loaded files shaped for Scala-Js-Tools to use
    */
  def lib4linker(name: String, bytes : Array[Byte]) = {
    val jarFile = (new MemVirtualBinaryFile(name) with VirtualJarFile)
      .withContent(bytes)
      .withVersion(Some(name)) // unique through the lifetime of the server
    IRFileCache.IRContainer.Jar(jarFile)
  }

  /**
    * In memory cache of all the jars used in the linker.
    */
  val commonLibraries4linker =
    JarFiles.classes.get._1.map { case (name, data) => lib4linker(name, data) }

  val linkerCaches = mutable.Map.empty[List[String], Seq[IRFileCache.VirtualRelativeIRFile]]

  def linkerLibraries(extLibs: List[String]) = {
    linkerCaches.getOrElseUpdate(extLibs, {
      val loadedJars = commonLibraries4linker
//      ++ extLibs.flatMap(extLibraries4linker.get)
      val cache = (new IRFileCache).newCache
      val res = cache.cached(loadedJars)
      log.debug("Loaded scalaJSClassPath")
      res
    })
  }
}
