package com.winton

import com.trueaccord.scalapb.grpc.AbstractService
import io.grpc._
import com.winton.GrpcServer.GrpcServerConfig

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future, Promise}


class GrpcServer(config: GrpcServerConfig, serviceImpl: => ServerServiceDefinition)
  extends Logging { self =>

  private val server = Promise[Server]()

  def start(): Unit = {
    logger.info("Starting GrpcServer")

    val serverBuilder = ServerBuilder
      .forPort(config.port)
      //.useTransportSecurity(config.sslCertChain, config.sslPrivateKey)
      .addService(serviceImpl)
    server.success(serverBuilder.build.start)
    logger.info("Server started, listening on " + config.port)

    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit = {
        logger.info("*** shutting down gRPC server since JVM is shutting down")
        Await.result(self.stop()(scala.concurrent.ExecutionContext.global), 2.seconds)
        logger.info("*** server shut down")
      }
    })
  }

  def stop()(implicit ec: ExecutionContext): Future[Server] = {
    logger.info("GrpcServer.stop() called")
    server.future.map(_.shutdown())(ec)
  }

  def serverShutdownFuture()(implicit ec: ExecutionContext): Future[Unit] = server.future.map(_.awaitTermination())(ec)

}

object GrpcServer {

  case class GrpcServerConfig(port: Int/*, sslCertChain: File, sslPrivateKey: File*/)

  def apply(config: GrpcServerConfig, serviceImpl: => ServerServiceDefinition) = new GrpcServer(config, serviceImpl)

  trait GrpcServerHost { self: AbstractService =>

    protected implicit def ec: ExecutionContext
    protected def config: GrpcServerConfig
    protected def serviceDefinition: ServerServiceDefinition

    private lazy val grpcServer = GrpcServer(config, serviceDefinition)

    def badArgException(message: String) = Status.INVALID_ARGUMENT.withDescription(message).asException()

    def start() = grpcServer.start()
    def stop() = grpcServer.stop()
    def whenShutdown = grpcServer.serverShutdownFuture()

  }

}


