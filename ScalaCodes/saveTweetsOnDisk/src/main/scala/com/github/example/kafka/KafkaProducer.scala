package com.github.example.kafka

import java.util.UUID.randomUUID

import com.github.example.utils.utils.{props, setupLogging, topicKafka}
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}

object KafkaProducer {
  //Properties of the producer


  val producer = new KafkaProducer[String,String](props)
  setupLogging()

  //method to send msg to the topic
  def run (status: String): Unit ={
    val record = new ProducerRecord[String,String](topicKafka,
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
