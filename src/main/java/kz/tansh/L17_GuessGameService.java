package kz.tansh;

import io.grpc.stub.StreamObserver;
import kz.tansh.proto.v17.GuessGameServiceGrpc;
import kz.tansh.proto.v17.GuessRequest;
import kz.tansh.proto.v17.GuessResponse;
import kz.tansh.requestHandlers.GuessGameRequestHandler;

public class L17_GuessGameService extends GuessGameServiceGrpc.GuessGameServiceImplBase {
  @Override
  public StreamObserver<GuessRequest> guess(StreamObserver<GuessResponse> responseObserver) {
    return new GuessGameRequestHandler(responseObserver);
  }
}
