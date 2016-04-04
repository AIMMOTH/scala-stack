package example

import scala.reflect.io
import scala.reflect.runtime.Settings
import java.io.PrintWriter
import java.io.InputStreamReader
import scala.reflect.io.VirtualDirectory
import java.io.ByteArrayInputStream
import java.util.zip.ZipInputStream
import scala.reflect.runtime.Settings
import java.io.PrintWriter
import com.google.appengine.repackaged.org.apache.lucene.analysis.Analyzer
import java.io.InputStreamReader
import scala.reflect.io.VirtualDirectory
import java.io.ByteArrayInputStream
import java.util.zip.ZipInputStream
import java.util.zip.ZipInputStream
import java.io.ByteArrayInputStream
import scala.reflect.io.Streamable
import scala.util.Random
import scala.tools.nsc
import org.scalajs.core.tools.io.MemVirtualJSFile
import org.scalajs.core.tools.io.MemVirtualSerializedScalaJSIRFile
import org.scalajs.core.tools.io.VirtualJSFile
import org.scalajs.core.tools.io.VirtualScalaJSIRFile
import org.scalajs.core.tools.sem.Semantics
import org.scalajs.core.tools.io.WritableMemVirtualJSFile
import scala.reflect.runtime.Settings
import org.scalajs.core.tools.linker.Linker
import scala.tools.nsc.util.JavaClassPath
import scala.tools.nsc.backend.JavaPlatform
import scala.tools.nsc.util.DirectoryClassPath
import scala.tools.nsc.util.ClassPath.JavaContext
import scala.collection.mutable
import scala.tools.nsc.plugins.Plugin
import scala.tools.nsc.Settings
import java.io.Writer
import scala.tools.nsc.reporters.ConsoleReporter
import org.scalajs.core.tools.logging._
import org.slf4j.LoggerFactory

class VirtualSjsCompiler { self =>

//  val log = Logger.getLogger(classOf[VirtualSjsCompiler].getName())
  val log = LoggerFactory.getLogger(getClass)
  val sjsLogger = new Log4jLogger()
  val classPath = new VirtualClasspath
  val env = "unknown"
  val extLibs = VirtualConfig.environments.getOrElse(env, Nil)

  def inMemClassloader = {
    new ClassLoader(this.getClass.getClassLoader) {
      val classCache = mutable.Map.empty[String, Option[Class[_]]]
      override def findClass(name: String): Class[_] = {
//        log.debug("Looking for Class " + name)
        val fileName = name.replace('.', '/') + ".class"
        val res = classCache.getOrElseUpdate(
          name,
          classPath.compilerLibraries(extLibs)
            .map(_.lookupPathUnchecked(fileName, false))
            .find(_ != null).map { f =>
            val data = f.toByteArray
            this.defineClass(name, data, 0, data.length)
          }
        )
        res match {
          case None =>
//            log.debug("Not Found Class " + name)
            throw new ClassNotFoundException()
          case Some(cls) =>
//            log.debug("Found Class " + name)
            cls
        }
      }
    }
  }

  /**
    * Mixed in to make a Scala compiler run entirely in-memory,
    * loading its classpath and running macros from pre-loaded
    * in-memory files
    */
  trait InMemoryGlobal {
    g: scala.tools.nsc.Global =>
    def ctx: JavaContext
    def dirs: Vector[DirectoryClassPath]
    override lazy val plugins = List[Plugin](new org.scalajs.core.compiler.ScalaJSPlugin(this))
    override lazy val platform: ThisPlatform = new JavaPlatform {
      val global: g.type = g
      override def classPath = new JavaClassPath(dirs, ctx)
    }
  }

  /**
    * Code to initialize random bits and pieces that are needed
    * for the Scala compiler to function, common between the
    * normal and presentation compiler
    */
  def initGlobalBits(logger: String => Unit) = {
    val vd = new io.VirtualDirectory("(memory)", None)
    val jCtx = new JavaContext()
    val jDirs = classPath.compilerLibraries(extLibs).map(new DirectoryClassPath(_, jCtx)).toVector
    lazy val settings = new Settings

    settings.outputDirs.setSingleOutput(vd)
    val writer = new Writer {
      var inner = new StringBuilder()
      def write(cbuf: Array[Char], off: Int, len: Int): Unit = {
        inner.append(new String(cbuf.map(_.toByte), off, len))
      }
      def flush(): Unit = {
        logger(inner.toString)
        inner = new StringBuilder()
      }
      def close(): Unit = ()
    }
    val reporter = new ConsoleReporter(settings, scala.Console.in, new PrintWriter(writer))
    (settings, reporter, vd, jCtx, jDirs)
  }

  def compile(templateId: String, src: String, logger: String => Unit = _ => ()): Option[Seq[VirtualScalaJSIRFile]] = {

//    val template = getTemplate(templateId)
//    val fullSource = template.fullSource(src)
//    log.debug("Compiling source:\n" + fullSource)
//    val singleFile = makeFile(fullSource.getBytes("UTF-8"))

    val (settings, reporter, vd, jCtx, jDirs) = initGlobalBits(logger)
    val compiler = new nsc.Global(settings, reporter) with InMemoryGlobal {
      g =>
      def ctx = jCtx
      def dirs = jDirs
      override lazy val analyzer = new {
        val global: g.type = g
      } with Analyzer {
        val cl = inMemClassloader
        override def findMacroClassLoader() = cl
      }
    }

    val run = new compiler.Run()
    run.compile(List("Main.scala"))

    if (vd.iterator.isEmpty) None
    else {
      val things = for {
        x <- vd.iterator.to[collection.immutable.Traversable]
        if x.name.endsWith(".sjsir")
      } yield {
        val f = new MemVirtualSerializedScalaJSIRFile(x.path)
        f.content = x.toByteArray
        f: VirtualScalaJSIRFile
      }
      Some(things.toSeq)
    }
  }

  def fastOpt(userFiles: Seq[VirtualScalaJSIRFile]): VirtualJSFile =
    link(userFiles, fullOpt = false)

  def fullOpt(userFiles: Seq[VirtualScalaJSIRFile]): VirtualJSFile =
    link(userFiles, fullOpt = true)

  def link(userFiles: Seq[VirtualScalaJSIRFile],
    fullOpt: Boolean): VirtualJSFile = {
    val semantics =
      if (fullOpt) Semantics.Defaults.optimized
      else Semantics.Defaults

    val linker = Linker(
      semantics = semantics,
      withSourceMap = false,
      useClosureCompiler = fullOpt)

    val output = WritableMemVirtualJSFile("output.js")
    linker.link(classPath.linkerLibraries(extLibs) ++ userFiles, output, sjsLogger)
    output
  }

  class Log4jLogger(minLevel: Level = Level.Debug) extends Logger {

    def log(level: Level, message: =>String): Unit = if (level >= minLevel) {
      if (level == Level.Warn || level == Level.Error)
        self.log.error(message)
      else
        self.log.debug(message)
    }
    def success(message: => String): Unit = info(message)
    def trace(t: => Throwable): Unit = {
      self.log.error("Compiling error", t)
    }
  }
}