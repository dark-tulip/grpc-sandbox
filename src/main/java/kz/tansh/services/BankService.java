package kz.tansh.services;

import io.grpc.stub.StreamObserver;
import kz.tansh.proto.v15.BankServiceGrpc;
import kz.tansh.proto.v15.GetAccountRequest;
import kz.tansh.proto.v15.GetAccountResponse;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

  @Override
  public void getAccountNumber(GetAccountRequest request, StreamObserver<GetAccountResponse> responseObserver) {
    responseObserver.onNext(GetAccountResponse.newBuilder()
                                              .setAccountNumber(request.getAccountNumber())
                                              .setBalance(2 * request.getAccountNumber())
                                              .build());

    responseObserver.onCompleted();
  }
}
