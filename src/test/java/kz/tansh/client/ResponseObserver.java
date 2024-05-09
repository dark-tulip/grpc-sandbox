package kz.tansh.client;

import io.grpc.stub.StreamObserver;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@Slf4j
public class ResponseObserver<T> implements StreamObserver<T> {

  private final CountDownLatch latch;
  private final List<T>        list = Collections.synchronizedList(new ArrayList<>());
  @Getter
  private       Throwable      throwable;

  public ResponseObserver(int latch) {
    this.latch = new CountDownLatch(latch);
  }

  public static <T> ResponseObserver<T> create() {
    return new ResponseObserver<>(1);
  }

  /**
   * Через onNext() передается ответ от сервера
   * который мы сохраняем в синхронизированном списке
   */
  @Override
  public void onNext(T t) {
    log.info("Received item: {}", t);
    this.list.add(t);
    latch.countDown();
  }

  @Override
  public void onError(Throwable throwable) {
    log.info("Error occurred: {}", throwable.getMessage());
    this.throwable = throwable;
    latch.countDown();
  }

  @Override
  public void onCompleted() {
    log.info("completed");
    latch.countDown(); // not to receive any response from server
  }

  public void await() {
    try {
      latch.await(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public List<T> getItems() {
    return this.list;
  }

}
