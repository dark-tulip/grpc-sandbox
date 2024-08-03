package kz.tansh;

import com.google.common.util.concurrent.Uninterruptibles;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import kz.tansh.proto.v15.*;
import kz.tansh.proto.v15.BankServiceGrpc;
import kz.tansh.repos.AccountRepository;
import kz.tansh.requestHandlers.DepositRequestHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;


@Slf4j
public class L15_BankService extends BankServiceGrpc.BankServiceImplBase {

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
    var accountNumber = request.getAccountNumber();                   // номер аккаунта
    var requestAmount = request.getAmount();                          // cумма для снятия
    var balance       = accountRepository.getAccount(accountNumber);  // текущий баланс

    if (balance.getBalance() < requestAmount) {
      responseObserver.onCompleted();  // not enough money
      return;
    }

    log.info("amount will be sent: {}", (requestAmount / 10));

    // снимаем купюрами номиналом в 10 (долларов, например)
    for (int i = 0; i < (requestAmount / 10); i++) {
      Money money = Money.newBuilder().setAmount(10).build();

      // sent 10 dollars
      responseObserver.onNext(money);

      log.info("money sent: {}", money);

      // withdraw dollars
      accountRepository.minusBalance(accountNumber, money.getAmount());

      Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
    }

    responseObserver.onCompleted();

  }

  @Override
  public StreamObserver<DepositRequest> deposit(StreamObserver<AccountBalanceResponse> responseObserver) {
    return new DepositRequestHandler(responseObserver);
  }
}
