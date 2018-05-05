name := "KnoldusAssingment"

version := "0.1"

scalaVersion := "2.12.6"
libraryDependencies++=Seq(
  "org.specs2" %% "specs2-core" % "3.8.6",
  // https://mvnrepository.com/artifact/com.typesafe.scala-logging/scala-logging
  "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0",
  "org.slf4j" % "slf4j-simple" % "1.7.25" % Test

)