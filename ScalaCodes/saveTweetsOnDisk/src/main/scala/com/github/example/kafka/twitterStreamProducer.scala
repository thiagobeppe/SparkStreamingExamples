package com.github.example.kafka

import twitter4j.{FilterQuery, TwitterStreamFactory}
import com.github.example.utils.utils.{config,simpleStatusListener}
object twitterStreamProducer {
  var finishingLoop = true

  //Create a twitter connect
  val twitterStream = new TwitterStreamFactory(config).getInstance
  twitterStream.addListener(simpleStatusListener)

  //Create a loop to send msgs to kafka
  while(finishingLoop){
    twitterStream.filter("Rust", "Python", "Docker", "Kubernetes" )
    Thread.sleep(200000)
    finishingLoop = false
  }
  twitterStream.cleanUp
  twitterStream.shutdown
}
