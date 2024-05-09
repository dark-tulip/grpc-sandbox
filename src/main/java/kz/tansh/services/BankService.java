package kz.tansh.services;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import kz.tansh.proto.v15.BankServiceGrpc;
import kz.tansh.proto.v15.GetAccountRequest;
import kz.tansh.proto.v15.GetAccountResponse;
import kz.tansh.proto.v15.GetAllAccountsResponse;

import java.util.HashMap;


public class BankService extends BankServiceGrpc.BankServiceImplBase {

  private final static HashMap<Integer, GetAccountResponse> bankAccountsRepository = new HashMap<>() {{
    put(1, GetAccountResponse.newBuilder().setAccountNumber(1).setBalance(100).build());
    put(2, GetAccountResponse.newBuilder().setAccountNumber(2).setBalance(200).build());
    put(3, GetAccountResponse.newBuilder().setAccountNumber(3).setBalance(300).build());
    put(4, GetAccountResponse.newBuilder().setAccountNumber(4).setBalance(400).build());
  }};

  @Override
  public void getAccountNumber(GetAccountRequest request, StreamObserver<GetAccountResponse> responseObserver) {
    responseObserver.onNext(GetAccountResponse.newBuilder()
                                              .setAccountNumber(request.getAccountNumber())
                                              .setBalance(2 * request.getAccountNumber())
                                              .build());

    responseObserver.onCompleted();
  }

  @Override
  public void getAllAccounts(Empty request, StreamObserver<GetAllAccountsResponse> responseObserver) {
    responseObserver.onNext(GetAllAccountsResponse.newBuilder().addAllAccounts(
        bankAccountsRepository.values()
    ).build());

    responseObserver.onCompleted();
  }

}
