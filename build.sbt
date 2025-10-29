name := "spark-sftp"
organization := "com.springml"

// --- Core versions ---
scalaVersion := "2.12.18"
val sparkVersion = "3.5.1"

libraryDependencies += "com.hierynomus" % "sshj" % "0.37.0"


libraryDependencies ++= Seq(
  "com.hierynomus" % "sshj" % "0.37.0",

  // optional: XML and Avro for Spark 3.x
  "com.databricks" %% "spark-xml" % "0.18.0",
  "org.apache.spark" %% "spark-avro" % sparkVersion,

  // testing
  "org.mockito" % "mockito-core" % "5.13.0" % Test,
  "org.scalatest" %% "scalatest" % "3.2.19" % Test
)

// Spark components
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion
)

ThisBuild / organization := "com.springml"
ThisBuild / version := "2.0.0-SNAPSHOT"


fork := true

javaOptions ++= Seq(
  "--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED"
)
