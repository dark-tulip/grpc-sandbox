package kz.tansh;

import kz.tansh.client.AbstractTest;
import kz.tansh.client.ResponseObserver;
import kz.tansh.proto.v15.AccountBalanceResponse;
import kz.tansh.proto.v15.DepositRequest;
import kz.tansh.proto.v15.Money;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

@Slf4j
public class Lec05ClientStreamingTest extends AbstractTest {

  @Test
  public void fillDeposit() {
    var responseObserver = ResponseObserver.<AccountBalanceResponse>create();

    var requestObserver = this.bankServiceStub.deposit(responseObserver);

    // init account
    requestObserver.onNext(DepositRequest.newBuilder().setAccountNumber(1).build());

    // fill money
    IntStream.rangeClosed(1, 5)
             .mapToObj(x -> Money.newBuilder().setAmount(10).build())
             .map(x -> DepositRequest.newBuilder().setMoney(x).build())
             .forEach(requestObserver::onNext);

    // end streaming, уведомить сервер, что клиент завершил посылать запросы
    requestObserver.onCompleted();

    // ожидать
    responseObserver.await();

    log.info("total balance: {}", responseObserver.getItems().get(0).getBalance().getAmount());

    Assertions.assertEquals(1, responseObserver.getItems().size());
    Assertions.assertEquals(150, responseObserver.getItems().get(0).getBalance().getAmount());
    Assertions.assertNull(responseObserver.getThrowable());
  }
}
