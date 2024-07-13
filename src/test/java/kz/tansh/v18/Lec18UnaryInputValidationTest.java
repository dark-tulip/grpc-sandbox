package kz.tansh.v18;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import kz.tansh.client.ResponseObserver;
import kz.tansh.proto.v18.BankAccount;
import kz.tansh.proto.v18.Money;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


@Slf4j
public class Lec18UnaryInputValidationTest extends AbstractTest {

  @Test
  public void blockingInputValidationTest() {
    var request = BankAccount.newBuilder().setAccountNumber(11).build();

    try {
      bankBlockingStub.getAccountBalance(request);
    } catch (StatusRuntimeException e) {
      log.info("{}, message: {}, {}", e.getStatus(), e.getMessage(), e.getTrailers());
      log.info(e.getMessage());
    }
  }

  @Test
  public void blockingInputValidationTest2() {
    var request = BankAccount.newBuilder().setAccountNumber(11).build();

    var ex = Assertions.assertThrows(StatusRuntimeException.class,
        () -> bankBlockingStub.getAccountBalance(request)
    );

    Assertions.assertEquals(ex.getStatus().getCode(), Status.INVALID_ARGUMENT.getCode());
  }

  @Test
  public void asyncInputValidationTest() {
    var request = BankAccount.newBuilder().setAccountNumber(11).build();

    var observer = ResponseObserver.<Money>create();
    bankStub.getAccountBalance(request, observer);

    observer.await();
    Assertions.assertTrue(observer.getItems().isEmpty());
    Assertions.assertNotNull(observer.getThrowable());
    Assertions.assertEquals(((StatusRuntimeException) observer.getThrowable()).getStatus().getCode(), Status.INVALID_ARGUMENT.getCode());
  }
}
