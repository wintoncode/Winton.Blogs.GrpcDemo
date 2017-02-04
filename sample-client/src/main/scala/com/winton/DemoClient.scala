package com.winton

import com.winton.demo.{DemoServiceGrpc, MessageRequest}
import io.grpc.{ManagedChannel, ManagedChannelBuilder}

import scala.concurrent.Await
import scala.concurrent.duration._

object DemoClient extends App with Logging {

  implicit val ec = scala.concurrent.ExecutionContext.global

  logger.info("Creating client")
  val channel: ManagedChannel = ManagedChannelBuilder.forAddress("localhost", 11235).usePlaintext(true).build
  val client = DemoServiceGrpc.stub(channel)
  logger.info("Client created")

  val request = MessageRequest("A Message!")
  logger.info(s"calling: getMessage(${request.requestStr})")
  val work = client.getMessage(request).map {
    res =>
      logger.info(s"Received: ${res.responseStr}")
  }

  Await.ready(work, 10.seconds)

}

