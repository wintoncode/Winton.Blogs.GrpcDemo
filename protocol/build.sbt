name := "demo-protocol"

libraryDependencies ++= {
  Seq(
    "io.grpc" % "grpc-netty" % "1.0.1",
    "io.netty" % "netty-tcnative-boringssl-static" % "1.1.33.Fork19", // prefer OpenSSL over JVM
    "com.trueaccord.scalapb" %% "scalapb-runtime-grpc" % "0.5.42"
  )
}

PB.targets in Compile := Seq(
  scalapb.gen() -> (sourceManaged in Compile).value
)


