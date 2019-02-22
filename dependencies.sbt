libraryDependencies ++= {
  val akkaV = "2.4.16"
  val akkaHttpV = "10.0.4"
  Seq(
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "org.clapper" %% "grizzled-slf4j" % "1.3.0",
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV,
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
    "org.specs2" % "specs2-core_2.11" % "3.8.8" % Test,
    "org.specs2" % "specs2-mock_2.11" % "3.8.8" % Test
  )
}

Revolver.settings