package com.github.example

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.{Seconds, StreamingContext}

object saveTweetsApp extends App{
  val ssc = new StreamingContext("local[*]", "SaveTweets", Seconds(1))
  val kafkaParams = Map(
    "bootstrap.servers" -> "localhost:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (false: java.lang.Boolean),
    "group.id" -> getClass.getSimpleName)
  val topic = List("TWEET_TOPIC_STREAM_SPARK").toSet
  val lines = KafkaUtils.createDirectStream[String,String](ssc, PreferConsistent, Subscribe[String,String](topic, kafkaParams))
  lines.map(record=> record.value().toString).print

  ssc.start()
  ssc.awaitTermination()
}
