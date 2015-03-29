name := "pidio"

version := "1.0"

scalaVersion := "2.11.6"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "com.typesafe.akka" % "akka-stream-experimental_2.11" % "1.0-M5",
  "com.typesafe.akka" % "akka-http-experimental_2.11" % "1.0-M5",
  "com.typesafe.akka" % "akka-http-core-experimental_2.11" % "1.0-M5",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
    