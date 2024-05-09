package kz.tansh;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import kz.tansh.client.AbstractTest;
import kz.tansh.proto.v15.GetAccountRequest;
import kz.tansh.proto.v15.GetAccountResponse;
import kz.tansh.proto.v15.GetAllAccountsResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class Lec02UnaryAsyncClientTest extends AbstractTest {

  @Test
  public void getAccount() throws InterruptedException {
    var request = GetAccountRequest.newBuilder()
                                   .setAccountNumber(123)
                                   .build();

    // чтобы успел напечататься асинхронный запрос
    CountDownLatch latch = new CountDownLatch(1);

    this.stub.getAccountNumber(request, new StreamObserver<GetAccountResponse>() {
      @Override
      public void onNext(GetAccountResponse response) {
        log.info("response: " + response);
        try {
          Assertions.assertEquals(123, response.getAccountNumber());
        }  finally {
          latch.countDown();
        }
      }

      @Override
      public void onError(Throwable throwable) {

      }

      @Override
      public void onCompleted() {

      }
    });
    latch.await();
  }

  @Test
  public void getAllAccounts() throws InterruptedException {

    var latch = new CountDownLatch(1);

    this.stub.getAllAccounts(Empty.newBuilder().build(), new StreamObserver<>() {
      @Override
      public void onNext(GetAllAccountsResponse response) {
        log.info("response: " + response);
        try {
          Assertions.assertEquals(4, response.getAccountsCount());
        } finally {
          latch.countDown();
        }
      }

      @Override
      public void onError(Throwable throwable) {

      }

      @Override
      public void onCompleted() {

      }
    });

    latch.await();
  }
}
