package kz.tansh;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import kz.tansh.proto.v19.BankAccount;
import kz.tansh.proto.v19.BankServiceGrpc;
import kz.tansh.proto.v19.Money;
import kz.tansh.proto.v19.WithdrawRequest;
import kz.tansh.repos.AccountRepository;
import kz.tansh.validators.L19_RequestValidator;
import kz.tansh.validators.RequestValidator;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;


@Slf4j
public class L19_TrailersMetadataBankService extends BankServiceGrpc.BankServiceImplBase {

  AccountRepository accountRepository = new AccountRepository();

  @Override
  public void getAccountBalance(BankAccount request, StreamObserver<Money> responseObserver) {
    L19_RequestValidator.validateAccount(request.getAccountNumber())
                        .ifPresentOrElse(
                            responseObserver::onError,
                            () -> sendAccountBalance(request, responseObserver)
                        );
  }

  private void sendAccountBalance(BankAccount request, StreamObserver<Money> responseObserver) {
    responseObserver.onNext(Money.newBuilder()
                                 .setAmount(accountRepository.getAccount(request.getAccountNumber()).getBalance())
                                 .build());

    responseObserver.onCompleted();
  }

  @Override
  public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
    try {
      if (request.getAccountNumber() == 999) {
        /*
         * Рандомная ошибка сервера. Если не обработать глобальным exception кинет неясное сообщение
         * Operation couldn’t be completed because of an unknown error.
         * Application error processing RPC
         */
        throw new RuntimeException("Some random error");
      }

      L19_RequestValidator.validateAccount(request.getAccountNumber())
                          .or(() -> L19_RequestValidator.hasEnoughBalance(request.getAmount(), accountRepository.getAccount(request.getAccountNumber()).getBalance()))
                          .or(() -> L19_RequestValidator.isAmountDivisibleBy10(request.getAmount()))
                          .ifPresentOrElse(
                              responseObserver::onError,
                              () -> sendMoney(request, responseObserver)
                          );
    } catch (Exception e) {
      // благодаря этому блоку обработается рандомная серверная ошибка
      responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
    }
  }

  private void sendMoney(WithdrawRequest request, StreamObserver<Money> responseObserver) {
    var accountNumber = request.getAccountNumber();                   // номер аккаунта
    var requestAmount = request.getAmount();                          // cумма для снятия

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

}
