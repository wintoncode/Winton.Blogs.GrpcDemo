import com.typesafe.sbt.packager.docker._

name := "demo-server"

libraryDependencies ++= {
  Seq(
    "com.typesafe.scala-logging" % "scala-logging_2.11" % "3.5.0",
    "ch.qos.logback" % "logback-classic" % "1.1.9",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )
}

maintainer in Docker := "s.bott@winton.com"
dockerBaseImage := "java:8-jre"
// chmods the file to executable, in case built on Windows
dockerCommands += ExecCmd("RUN", "chmod", "+x", s"${(defaultLinuxInstallLocation in Docker).value}/bin/${executableScriptName.value}")
dockerExposedPorts in Docker := Seq(11235)
