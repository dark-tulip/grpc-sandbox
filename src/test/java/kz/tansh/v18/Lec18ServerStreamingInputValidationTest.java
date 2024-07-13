package kz.tansh.v18;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import kz.tansh.client.ResponseObserver;
import kz.tansh.proto.v18.Money;
import kz.tansh.proto.v18.WithdrawRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;


@Slf4j
public class Lec18ServerStreamingInputValidationTest extends AbstractTest {

  /**
   * Блокирующий стаб не кидает исключение, нужно вызвать next() or hasNext()
   */
  @ParameterizedTest
  @MethodSource("inputData")
  public void blockingInputValidationTest(WithdrawRequest request, Status.Code resultCode) {
    var exception = Assertions.assertThrows(
        StatusRuntimeException.class,
        () -> bankBlockingStub.withdraw(request).hasNext()
    );

    Assertions.assertEquals(exception.getStatus().getCode(), resultCode);
  }

  @ParameterizedTest
  @MethodSource("inputData")
  public void asyncInputValidationTest(WithdrawRequest request, Status.Code resultCode) {
    ResponseObserver<Money> responseObserver = ResponseObserver.create();
    bankStub.withdraw(request, responseObserver);
    responseObserver.await();
    Assertions.assertEquals((
        (StatusRuntimeException) responseObserver.getThrowable()).getStatus().getCode(),
        resultCode
    );
  }

  private Stream<Arguments> inputData() {
    return Stream.of(
        Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(11).setAmount(1000).build(), Status.INVALID_ARGUMENT.getCode()),
        Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(10).setAmount(10000).build(), Status.INVALID_ARGUMENT.getCode()),
        Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(1200).build(), Status.INVALID_ARGUMENT.getCode())
    );
  }
}
