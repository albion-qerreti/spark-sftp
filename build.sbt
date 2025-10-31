name := "spark-sftp"
organization := "com.springml"
version := "1.1.4"

scalaVersion := "2.12.18"

val sparkVersion = "3.5.0"

libraryDependencies ++= Seq(
//  Spark dependenices
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql"  % sparkVersion % "provided",
  "org.apache.spark" %% "spark-avro" % sparkVersion % "provided",
  "com.databricks" % "spark-xml_2.12" % "0.15.0",

  "com.springml" % "sftp.client" % "1.0.3",

// Dependenices for testing if testing is implemented
  "org.mockito" % "mockito-core" % "2.0.31-beta" % Test,
  "org.scalatest" %% "scalatest" % "3.0.3" % Test,
  "org.apache.avro" % "avro-mapred" % "1.7.7" % Test exclude("org.mortbay.jetty", "servlet-api")
)

resolvers ++= Seq(
  "Maven Central" at "https://repo1.maven.org/maven2/",
  "Spark Packages Repo" at "https://dl.bintray.com/spark-packages/maven"
)
