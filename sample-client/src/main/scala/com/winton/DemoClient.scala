package com.winton

import com.winton.demo.{DemoServiceGrpc, MessageRequest}
import io.grpc.ManagedChannelBuilder

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try

object DemoClient extends App with Logging {

  implicit val ec = scala.concurrent.ExecutionContext.global

  val host = Try(args(0)).getOrElse("localhost")
  val port = Try(args(1).toInt).getOrElse(11235)

  logger.info("Creating client")
  val channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build
  val client = DemoServiceGrpc.stub(channel)
  logger.info("Client created")

  val request = MessageRequest("A Message!")
  logger.info(s"calling: getMessage(${request.requestStr})")
  val work = client.getMessage(request).map {
    res =>
      logger.info(s"Received: ${res.responseStr}")
  }

  Await.result(work, 10.seconds)

}

