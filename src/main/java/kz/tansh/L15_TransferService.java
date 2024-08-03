package kz.tansh;

import io.grpc.stub.StreamObserver;
import kz.tansh.proto.v15.TransferRequest;
import kz.tansh.proto.v15.TransferResponse;
import kz.tansh.proto.v15.TransferServiceGrpc;
import kz.tansh.requestHandlers.TransferRequestHandler;
import lombok.extern.slf4j.Slf4j;


/**
 * BiDirectional streaming example
 */
@Slf4j
public class L15_TransferService extends TransferServiceGrpc.TransferServiceImplBase {

  @Override
  public StreamObserver<TransferRequest> transfer(StreamObserver<TransferResponse> responseObserver) {
    return new TransferRequestHandler(responseObserver);
  }
}
