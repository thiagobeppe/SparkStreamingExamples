package com.github.example


import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.{Seconds, StreamingContext}
import com.github.example.utils.utils.{kafkaParams, setupLogging, topic}

object saveTweetsApp extends App{

  val ssc = new StreamingContext("local[*]", "SaveTweets", Seconds(1))
  val jsonParser: ObjectMapper = new ObjectMapper()
  setupLogging()

  val lines = KafkaUtils.createDirectStream[String,String](ssc, PreferConsistent, Subscribe[String,String](topic, kafkaParams))
  lines
    .map(record=> record.value().toString)
    .map(value => jsonParser.readValue(value, classOf[JsonNode]))
    .map(value => {
      if(value.has("retweeted_status") && value.get("retweeted_status").has("extended_tweet")) value.get("retweeted_status").get("extended_tweet").get("full_text").asText()
      else if(value.has("extended_tweet")) value.get("extended_tweet").get("full_text").asText()
      else value.get("text").asText()
    })

  var totalTweets:Long = 0


  ssc.start()
  ssc.awaitTermination()
}
