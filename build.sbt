val commonPlugins = Seq(JavaAppPackaging, DockerPlugin)

val projectSettings = Seq(
  organization in ThisBuild := "com.winton",
  scalaVersion in ThisBuild := "2.11.8"
)

val scalacSettings = Seq(
  scalacOptions in ThisBuild ++= Seq(
    "-target:jvm-1.8",
    "-encoding", "UTF-8",
    "-deprecation", // warning and location for usages of deprecated APIs
    "-feature", // warning and location for usages of features that should be imported explicitly
    "-unchecked", // additional warnings where generated code depends on assumptions
    "-Xlint", // recommended additional warnings
    "-Xcheckinit", // runtime error when a val is not initialized due to trait hierarchies (instead of NPE somewhere else)
    "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
    "-Ywarn-value-discard", // Warn when non-Unit expression results are unused
    "-Ywarn-inaccessible",
    "-Ywarn-dead-code"
  ),

  fork in run := true,
  connectInput in run := true
)

lazy val commonSettings = projectSettings ++ scalacSettings

lazy val root = (project in file (".")).
  settings(commonSettings: _*).
  enablePlugins(GitVersioning).
  settings(
    name := "grpc-demo",
    git.useGitDescribe := true,
    aggregate in update := false
  ).
  enablePlugins(commonPlugins: _*).
  aggregate(
    server,
    sampleClient
  )

lazy val protocol = (project in file ("protocol")).
  settings(commonSettings: _*).
  enablePlugins(commonPlugins: _*)

lazy val server = (project in file ("server")).
  settings(commonSettings: _*).
  enablePlugins(commonPlugins: _*).
  dependsOn(protocol)

lazy val sampleClient = (project in file ("sample-client")).
  settings(commonSettings: _*).
  enablePlugins(commonPlugins: _*).
  dependsOn(protocol)

