package com.github.example.utils

import java.util.Properties

import com.github.example.kafka.KafkaProducer
import org.apache.kafka.common.serialization.StringDeserializer
import twitter4j.{StallWarning, Status, StatusDeletionNotice, StatusListener, TwitterObjectFactory}

object utils {
  val config = new twitter4j.conf.ConfigurationBuilder()
    .setOAuthConsumerKey(System.getenv("TWITTER_CONSUMER_TOKEN_KEY"))
    .setOAuthConsumerSecret(System.getenv("TWITTER_CONSUMER_TOKEN_SECRET"))
    .setOAuthAccessToken(System.getenv("TWITTER_ACCESS_TOKEN_KEY"))
    .setOAuthAccessTokenSecret(System.getenv("TWITTER_ACCESS_TOKEN_SECRET"))
    .setTweetModeExtended(true)
    .setJSONStoreEnabled(true)
    .build


  def simpleStatusListener = new StatusListener() {
    def onStatus(status: Status): Unit = {
      KafkaProducer.run(TwitterObjectFactory.getRawJSON(status))    }
    def onDeletionNotice(statusDeletionNotice: StatusDeletionNotice) {}
    def onTrackLimitationNotice(numberOfLimitedStatuses: Int) {}
    def onException(ex: Exception) { ex.printStackTrace }
    def onScrubGeo(arg0: Long, arg1: Long) {}
    def onStallWarning(warning: StallWarning) {}
  }
  //Configs to Consumer in Spark Streaming
  val kafkaParams = Map(
    "bootstrap.servers" -> "localhost:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "auto.offset.reset" -> "latest",
    "enable.auto.commit" -> (false: java.lang.Boolean),
    "group.id" ->  com.github.example.saveTweetsApp.getClass.getSimpleName)

  //Topics
  val topic = List("TWEET_TOPIC_STREAM_SPARK").toSet
  val topicKafka = "TWEET_TOPIC_STREAM_SPARK"

  //Producer properties
  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer")
  props.put("ack", "1")
  props.put("max.in.flight.requests.per.connection", "5")

  def setupLogging() = {
    import org.apache.log4j.{Level, Logger}
    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.ERROR)
  }
}