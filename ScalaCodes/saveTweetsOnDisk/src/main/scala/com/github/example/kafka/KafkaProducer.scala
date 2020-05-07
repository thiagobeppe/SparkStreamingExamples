package com.github.example.kafka

import java.util.Properties
import java.util.UUID.randomUUID

import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import org.slf4j.LoggerFactory

object KafkaProducer {
  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer")
  props.put("ack", "1")
  props.put("max.in.flight.requests.per.connection", "5")

  val producer = new KafkaProducer[String,String](props)
  val topic = "TWEET_TOPIC_STREAM_SPARK"

  def run (status: String): Unit ={
    val record = new ProducerRecord[String,String](topic,
      randomUUID().toString,
      status)
    val metadata = producer.send(record, new Callback {
      override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
        if( exception != null) println("Error while attempting to send message!", exception)
        else println(s"The record was sent with key: ${record.key()}, value: ${record.value()}. To the partition #${metadata.partition()} and the offset #${metadata.offset()} \n")
      }
    })

  }
}
