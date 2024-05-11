package kz.tansh;

import kz.tansh.client.AbstractTest;
import kz.tansh.client.ResponseObserver;
import kz.tansh.proto.v16.Output;
import kz.tansh.proto.v16.RequestSize;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class Lec07FlowControlStreamingTest extends AbstractTest {

  @Test
  public void getMessages() {
    var responseObserver = ResponseObserver.<Output>create();

    var requestObserver = this.flowControlServiceStub.getMessages(responseObserver);

    requestObserver.onNext(RequestSize.newBuilder().setSize(2).build());
    requestObserver.onNext(RequestSize.newBuilder().setSize(99).build());

    // ожидать ответа
    responseObserver.await();

    Assertions.assertEquals(100, responseObserver.getItems().size());

    responseObserver.onCompleted();
  }
}
