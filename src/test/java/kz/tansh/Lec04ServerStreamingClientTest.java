package kz.tansh;

import kz.tansh.client.AbstractTest;
import kz.tansh.client.ResponseObserver;
import kz.tansh.proto.v15.Money;
import kz.tansh.proto.v15.WithdrawRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

@Slf4j
public class Lec04ServerStreamingClientTest extends AbstractTest {

  @Test
  public void withdrawMoneyRequestBlockingStub() {
    var request = WithdrawRequest.newBuilder()
                                 .setAccountNumber(1)
                                 .setAmount(20)
                                 .build();

    Iterator<Money> iterator = this.bankServiceBlockingStub.withdrawAccount(request);

    int cnt = 0;

    while (iterator.hasNext()) {
      log.info("received money: {}", iterator.next().getAmount());
      cnt++;
    }

    log.info("total: {}", cnt);
    Assertions.assertEquals(2, cnt);
  }


  @Test
  public void withdrawMoneyRequestAsyncStub() {
    var request = WithdrawRequest.newBuilder()
                                 .setAccountNumber(1)
                                 .setAmount(20)
                                 .build();

    var observer = ResponseObserver.<Money>create();

    this.bankServiceStub.withdrawAccount(request, observer);

    observer.await();

    log.info("{}", observer.getItems());
    Assertions.assertNull(observer.getThrowable());
    Assertions.assertEquals(2, observer.getItems().size());
    Assertions.assertEquals(10, observer.getItems().get(0).getAmount());
  }
}
