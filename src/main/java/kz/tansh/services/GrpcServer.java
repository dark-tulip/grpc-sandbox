package kz.tansh.services;

import io.grpc.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;


@Slf4j
public class GrpcServer {

  private final Server server;

  private GrpcServer(Server server) {
    this.server = server;
  }

  public static GrpcServer create(BindableService... bindableServices) {
    return create(6565, bindableServices);
  }

  public static GrpcServer create(int port, BindableService... bindableServices) {
    Server server = ServerBuilder.forPort(port)
                                 .addServices(Arrays.stream(bindableServices).map(BindableService::bindService).toList())
                                 .build();

    return new GrpcServer(server);
  }

  public GrpcServer start()  {
    var services = server.getServices().stream()
                         .map(ServerServiceDefinition::getServiceDescriptor)
                         .map(ServiceDescriptor::getName)
                         .toList();
    try {
      server.start();
      log.info("Server started on port: {}, services: {}", server.getPort(), services);
      return this;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public GrpcServer await() {
    try {
      this.server.awaitTermination();
      return this;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public GrpcServer stop() {
    this.server.shutdownNow();
    return this;
  }
}
