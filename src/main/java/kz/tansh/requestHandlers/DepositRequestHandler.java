package kz.tansh.requestHandlers;

import io.grpc.stub.StreamObserver;
import kz.tansh.proto.v15.AccountBalanceResponse;
import kz.tansh.proto.v15.DepositRequest;
import kz.tansh.proto.v15.Money;
import kz.tansh.repos.AccountRepository;
import lombok.extern.slf4j.Slf4j;


/**
 * Принимает стрим запросов от клиента
 */
@Slf4j
public class DepositRequestHandler implements StreamObserver<DepositRequest> {

  private final AccountRepository accountRepository = new AccountRepository();
  private final StreamObserver<AccountBalanceResponse> responseStreamObserver;
  private int accountNumber;

  public DepositRequestHandler(StreamObserver<AccountBalanceResponse> responseStreamObserver) {
    this.responseStreamObserver = responseStreamObserver;
  }

  @Override
  public void onNext(DepositRequest depositRequest) {
    switch (depositRequest.getDepositCase()) {

      case ACCOUNT_NUMBER -> this.accountNumber = depositRequest.getAccountNumber();

      // instead of direct update to db you can collect all updates to the batch -
      // и в методе onCompleted() сохранить и закоммитить эту пачку
      case MONEY -> accountRepository.addBalance(accountNumber, depositRequest.getMoney().getAmount());

    }
  }

  /**
   * will be invoked when client wants to cancel request
   * Good place to rollback something
   */
  @Override
  public void onError(Throwable throwable) {
    log.info("8ALMSS2S :: client error occurred: {}", throwable.getMessage());
  }

  /**
   * Good place to commit something
   */
  @Override
  public void onCompleted() {
    AccountBalanceResponse response = AccountBalanceResponse
        .newBuilder()
        .setAccountNumber(accountNumber)
        .setBalance(Money.newBuilder().setAmount(accountRepository.getAccount(accountNumber).getBalance()))
        .build();

    responseStreamObserver.onNext(response);

    responseStreamObserver.onCompleted();
  }
}
