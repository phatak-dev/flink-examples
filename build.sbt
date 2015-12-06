
name := "flink-examples"

version := "1.0"
scalaVersion := "2.10.4"

libraryDependencies ++= Seq("org.apache.flink" % "flink-scala" % "0.10.0",
  "org.apache.flink" % "flink-clients" % "0.10.0",
  "org.apache.flink" % "flink-streaming-scala" % "0.10.0")
    