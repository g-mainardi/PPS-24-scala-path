name := "pps-24-scala-path"

scalaVersion := "3.3.3"
libraryDependencies += "it.unibo.alice.tuprolog" % "2p-core" % "4.1.1"
libraryDependencies += "it.unibo.alice.tuprolog" % "2p-ui" % "4.1.1"
libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
libraryDependencies += "com.github.sbt" % "junit-interface" % "0.13.3"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19"

// https://www.scala-sbt.org/1.x/docs/Java-Sources.html
Compile / compileOrder := CompileOrder.ScalaThenJava
