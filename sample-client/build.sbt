name := "demo-client"

libraryDependencies ++= {
  Seq(
    "com.typesafe.scala-logging" % "scala-logging_2.11" % "3.5.0",
    "ch.qos.logback" % "logback-classic" % "1.1.9",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )
}

