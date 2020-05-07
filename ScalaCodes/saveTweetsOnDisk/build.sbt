name := "saveTweetsOnDisk"

version := "0.1"

scalaVersion := "2.12.11"

libraryDependencies += "org.apache.spark" %% "spark-core" % "3.0.0-preview2"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "3.0.0-preview2"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "3.0.0-preview2"
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.4.1"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.30"
libraryDependencies += "org.twitter4j" % "twitter4j-stream" % "4.0.7"
