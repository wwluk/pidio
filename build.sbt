name := "pidio"

version := "0.1.0"

scalaVersion := "2.11.6"


libraryDependencies ++= {
  val akkaStreamV = "1.0-M5"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.3.9",
    "com.typesafe.akka" % "akka-stream-experimental_2.11" % akkaStreamV,
    "com.typesafe.akka" % "akka-http-experimental_2.11" % akkaStreamV,
    "com.typesafe.akka" % "akka-http-core-experimental_2.11" % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaStreamV,
    "org.scalatest" %% "scalatest" % "2.2.4" % "test"
  )
}

libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.2" % "test"
    
