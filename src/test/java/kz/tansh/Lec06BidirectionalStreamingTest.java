package kz.tansh;

import kz.tansh.client.AbstractTest;
import kz.tansh.client.ResponseObserver;
import kz.tansh.proto.v15.Money;
import kz.tansh.proto.v15.TransferRequest;
import kz.tansh.proto.v15.TransferResponse;
import kz.tansh.proto.v15.TransferStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@Slf4j
public class Lec06BidirectionalStreamingTest extends AbstractTest {

  @Test
  public void transfer() {
    var responseObserver = ResponseObserver.<TransferResponse>create();

    var requestObserver = this.transferServiceStub.transfer(responseObserver);

    requestObserver.onNext(TransferRequest.newBuilder()
                                          .setAccountFrom(1)
                                          .setAccountTo(2)
                                          .setAmount(Money.newBuilder().setAmount(10).build()).build());
    // закончить свои запросы
    requestObserver.onCompleted();

    // ожидать ответа
    responseObserver.await();
    log.info(responseObserver.getItems().get(0).toString());

    // завершить ответы от сервера
    responseObserver.onCompleted();
  }

  @Test
  public void transferV2() {
    var responseObserver = ResponseObserver.<TransferResponse>create();

    var requestObserver = this.transferServiceStub.transfer(responseObserver);

    var requests = List.of(
        TransferRequest.newBuilder().setAccountFrom(1).setAccountTo(2).setAmount(Money.newBuilder().setAmount(10).build()).build(),
        TransferRequest.newBuilder().setAccountFrom(1).setAccountTo(2).setAmount(Money.newBuilder().setAmount(10).build()).build(),
        TransferRequest.newBuilder().setAccountFrom(1).setAccountTo(2).setAmount(Money.newBuilder().setAmount(10).build()).build(),
        TransferRequest.newBuilder().setAccountFrom(1).setAccountTo(2).setAmount(Money.newBuilder().setAmount(10).build()).build()
    );

    requests.forEach(requestObserver::onNext);

    // закончить свои запросы
    requestObserver.onCompleted();

    // ожидать ответа
    responseObserver.await();
    log.info(responseObserver.getItems().get(0).toString());

    for (var response : responseObserver.getItems()) {
      Assertions.assertEquals(TransferStatus.SUCCESS, response.getStatus());
    }

    // завершить ответы от сервера
    responseObserver.onCompleted();

  }
}
