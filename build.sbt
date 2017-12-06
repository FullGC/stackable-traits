name := "StackableTraits"

version := "1.0"

scalaVersion := "2.12.4"


libraryDependencies ++= Seq(
  "org.json4s" % "json4s-native_2.12" % "3.5.0", "org.reflections" % "reflections" % "0.9.11",
  "org.json4s" % "json4s-jackson_2.12" % "3.5.0",
  "com.typesafe.akka" %% "akka-actor" % "2.4.16",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "ch.qos.logback" % "logback-classic" % "1.1.7"

)
