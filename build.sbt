name := "akka-http"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.0.5",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.5",
  "com.softwaremill.akka-http-session" %% "core" % "0.4.0",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.lihaoyi" %% "upickle" % "0.4.1"
)
        