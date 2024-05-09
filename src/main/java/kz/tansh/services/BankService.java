package kz.tansh.services;

import com.google.common.util.concurrent.Uninterruptibles;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import kz.tansh.proto.v15.*;
import kz.tansh.proto.v15.BankServiceGrpc;
import kz.tansh.repos.AccountRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;


@Slf4j
public class BankService extends BankServiceGrpc.BankServiceImplBase {

  AccountRepository accountRepository = new AccountRepository();

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
        accountRepository.getAccounts()
    ).build());

    responseObserver.onCompleted();
  }

  @Override
  public void withdrawAccount(WithdrawRequest request, StreamObserver<Money> responseObserver) {
    var accountNumber = request.getAccountNumber();             // номер аккаунта
    var amount        = request.getAmount();                           // cумма для снятия
    var balance       = accountRepository.getAccount(accountNumber);  // текущий баланс

    if (balance.getBalance() < amount) {
      responseObserver.onCompleted();  // not enough money
      return;
    }

    // снимаем купюрами номиналом в 10 (долларов, например)
    for (int i = 0; i < amount / 10; i++) {
      Money money = Money.newBuilder().setAmount(10).build();

      // sent 10 dollars
      responseObserver.onNext(money);

      // withdraw dollars
      accountRepository.withdraw(accountNumber, money.getAmount());

      log.info("money sent: {}", money);

      Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);

    }

    responseObserver.onCompleted();

  }
}
