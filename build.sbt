enablePlugins(ScalaJSPlugin)

name := "SBT for Scala.js build"

scalaVersion := "2.11.8"

scalaSource in Compile := baseDirectory.value / "src" / "main" / "scalajs"

sourceDirectory in Compile := baseDirectory.value / "src" / "main" / "scalajs"

val jsDir = "src/main/webapp" // Output dir for JavaScript generation

crossTarget in (Compile, fastOptJS) := file(jsDir)

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.0"

// libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.5.4"