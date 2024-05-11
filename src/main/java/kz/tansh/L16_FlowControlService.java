package kz.tansh;


import io.grpc.stub.StreamObserver;
import kz.tansh.proto.v16.FlowControlServiceGrpc;
import kz.tansh.proto.v16.Output;
import kz.tansh.proto.v16.RequestSize;
import kz.tansh.requestHandlers.FlowControlRequestHandler;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class L16_FlowControlService extends FlowControlServiceGrpc.FlowControlServiceImplBase {

  @Override
  public StreamObserver<RequestSize> getMessages(StreamObserver<Output> responseObserver) {
    return new FlowControlRequestHandler(responseObserver);
  }


}
