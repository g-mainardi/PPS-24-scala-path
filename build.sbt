name := "pps-24-scala-path"

scalaVersion := "3.3.3"
libraryDependencies ++= Seq(
  "it.unibo.alice.tuprolog" % "2p-core" % "4.1.1",
  "it.unibo.alice.tuprolog" % "2p-ui" % "4.1.1",
  "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
  "com.github.sbt" % "junit-interface" % "0.13.3",
  "org.scalatest" %% "scalatest" % "3.2.19"
)

// First compile Scala, then Java
// https://www.scala-sbt.org/1.x/docs/Java-Sources.html
Compile / compileOrder := CompileOrder.ScalaThenJava

// Plugin assembly
enablePlugins(AssemblyPlugin)

// === Executable JAR generated with sbt-assembly ===

// Main class to execute
val mainClassName = "it.unibo.ScalaPath"
assembly / mainClass := Some(mainClassName)
assembly / packageOptions += Package.ManifestAttributes(
  "Main-Class" -> mainClassName
)

// JAR result name
assembly / assemblyJarName := "pps-24-scala-path-assembly.jar"

// Ignore test on build
Test / test := {
  streams.value.log info "Skipping tests during build"
}