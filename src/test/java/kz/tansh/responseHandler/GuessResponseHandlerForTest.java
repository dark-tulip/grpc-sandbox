package kz.tansh.responseHandler;

import io.grpc.stub.StreamObserver;
import kz.tansh.proto.v17.GuessRequest;
import kz.tansh.proto.v17.GuessResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;


@Slf4j
public class GuessResponseHandlerForTest implements StreamObserver<GuessResponse> {

  @Setter
  private       StreamObserver<GuessRequest> guessRequestStreamObserver;
  private final CountDownLatch               latch = new CountDownLatch(1);

  private int middle;
  private int higher;
  private int lower;

  @Override
  public void onNext(GuessResponse guessResponse) {
    log.info("attempt number: {}, guess result: {}", guessResponse.getAttemptNumber(), guessResponse.getResult());

    switch (guessResponse.getResult()) {
      case LOWER -> this.send(lower, middle);
      case HIGHER -> this.send(middle, higher);
      // default we can ignore because already guessed the number
    }

  }

  @Override
  public void onError(Throwable throwable) {
    latch.countDown();
  }

  @Override
  public void onCompleted() {
    latch.countDown();
    guessRequestStreamObserver.onCompleted();
  }

  private void send(int low, int high) {
    this.lower  = low;
    this.higher = high;
    this.middle = (low + high) / 2;
    log.info("client guessed: {}", middle);
    this.guessRequestStreamObserver.onNext(GuessRequest.newBuilder().setValue(middle).build());
  }

  public void start() {
    this.send(0, 100);
  }

  public void await() {
    try {
      latch.await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
