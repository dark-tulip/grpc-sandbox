package kz.tansh;

import com.google.protobuf.Empty;
import kz.tansh.client.AbstractTest;
import kz.tansh.client.ResponseObserver;
import kz.tansh.proto.v15.GetAccountRequest;
import kz.tansh.proto.v15.GetAccountResponse;
import kz.tansh.proto.v15.GetAllAccountsResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class Lec03UnaryAsyncClientTest extends AbstractTest {

  @Test
  public void getAccount() {
    var request = GetAccountRequest.newBuilder()
                                   .setAccountNumber(123)
                                   .build();

    ResponseObserver<GetAccountResponse> observer = ResponseObserver.create();
    this.bankServiceStub.getAccountNumber(request, observer);

    observer.await();  // без ожидания тест не пройдет, соединение не успеет создаться

    Assertions.assertEquals(1, observer.getItems().size());
    Assertions.assertEquals(246, observer.getItems().get(0).getBalance());
    Assertions.assertNull(observer.getThrowable());
  }

  @Test
  public void getAllAccounts() {
    var observer = ResponseObserver.<GetAllAccountsResponse>create();

    this.bankServiceStub.getAllAccounts(Empty.newBuilder().build(), observer);

    observer.await();

    Assertions.assertEquals(1, observer.getItems().size());  // приходит один список в ответе
    Assertions.assertEquals(4, observer.getItems().get(0).getAccountsCount());  // в этом списке 4 аккаунта
    Assertions.assertNull(observer.getThrowable());
  }
}
