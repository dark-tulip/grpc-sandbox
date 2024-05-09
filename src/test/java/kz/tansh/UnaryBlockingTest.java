package kz.tansh;

import com.google.protobuf.Empty;
import kz.tansh.client.AbstractTest;
import kz.tansh.proto.v15.GetAccountRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UnaryBlockingTest extends AbstractTest {

  @Test
  public void blockingStub() {
    var request = GetAccountRequest.newBuilder()
        .setAccountNumber(123)
        .build();

    var response = this.blockingStub.getAccountNumber(request);

    log.info("response: " + response);
    Assertions.assertEquals(123, response.getAccountNumber());
  }

  @Test
  public void getAllAccounts() {
    var response = this.blockingStub.getAllAccounts(Empty.newBuilder().build());
    log.info("response: " + response);
    Assertions.assertEquals(4, response.getAccountsCount());
  }


}
