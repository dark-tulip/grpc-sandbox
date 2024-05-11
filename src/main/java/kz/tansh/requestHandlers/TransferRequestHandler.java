package kz.tansh.requestHandlers;

import io.grpc.stub.StreamObserver;
import kz.tansh.proto.v15.*;
import kz.tansh.repos.AccountRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TransferRequestHandler implements StreamObserver<TransferRequest> {

  private final StreamObserver<TransferResponse> responseObserver;
  private final AccountRepository                accountRepository = new AccountRepository();

  public TransferRequestHandler(StreamObserver<TransferResponse> responseObserver) {
    this.responseObserver = responseObserver;
  }

  @Override
  public void onNext(TransferRequest transferRequest) {
    TransferStatus status = transfer(transferRequest);

    var response = TransferResponse
        .newBuilder()
        .setAccountFrom(AccountBalanceResponse
            .newBuilder()
            .setAccountNumber(transferRequest.getAccountFrom())
            .setBalance(Money.newBuilder().setAmount(accountRepository.getAccount(transferRequest.getAccountFrom()).getBalance()).build())
        )
        .setAccountTo(AccountBalanceResponse
            .newBuilder()
            .setAccountNumber(transferRequest.getAccountTo())
            .setBalance(Money.newBuilder().setAmount(accountRepository.getAccount(transferRequest.getAccountTo()).getBalance()).build())
        )
        .setStatus(status)
        .build();

    // здесь не нужно вызывать onCompleted для сервера;
    // так как это двунаправленное соединение;
    // и сервер будет еще посылать ответы клиенту
    responseObserver.onNext(response);
  }

  @Override
  public void onError(Throwable throwable) {
    log.info("client error: {}", throwable.getMessage());
  }

  @Override
  public void onCompleted() {
    log.info("transfer completed from the client side, request stream is finished");
    responseObserver.onCompleted();  // завершаем серверный стриминг
  }

  private TransferStatus transfer(TransferRequest request) {
    var amount      = request.getAmount().getAmount();
    var fromAccount = request.getAccountFrom();
    var toAccount   = request.getAccountTo();
    var status      = TransferStatus.REJECTED;

    if (accountRepository.getAccount(fromAccount).getBalance() >= amount && fromAccount != toAccount) {
      accountRepository.minusBalance(fromAccount, amount);
      accountRepository.addBalance(toAccount, amount);
      status = TransferStatus.SUCCESS;
      log.info("success money transfer, from: {}, to: {}, amount: {}", fromAccount, toAccount, amount);
    }

    return status;
  }
}
