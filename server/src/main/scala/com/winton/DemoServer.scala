package com.winton

import com.winton.GrpcServer.GrpcServerConfig
import com.winton.demo.{DemoServiceGrpc, MessageRequest, MessageResponse}
import io.grpc.ServerServiceDefinition

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object DemoServer extends App with Logging {

  implicit val ec = scala.concurrent.ExecutionContext.global
  val server = new DemoServer(11235)

  server.start()
  logger.info("Server started...")
  val done = server.whenShutdown.map(_ => logger.info("Server Stopped"))

  Await.ready(done, Duration.Inf)

}

class DemoServer (port: Int)(implicit protected val ec: ExecutionContext)
    extends DemoServiceGrpc.DemoService
        with GrpcServer.GrpcServerHost
        with Logging {

  override protected def config: GrpcServerConfig = GrpcServerConfig(port)

  override protected def serviceDefinition: ServerServiceDefinition = DemoServiceGrpc.bindService(this, ec)


  override def getMessage(request: MessageRequest): Future[MessageResponse] = {
    logger.info(s"Received: getMessage(${request.requestStr})")
    Future(MessageResponse("Hi! You just sent me " + request.requestStr))
  }

}

