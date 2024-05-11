package kz.tansh.requestHandlers;

import io.grpc.stub.StreamObserver;
import kz.tansh.proto.v17.GuessRequest;
import kz.tansh.proto.v17.GuessResponse;
import kz.tansh.proto.v17.GuessResult;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;


@Slf4j
public class GuessGameRequestHandler implements StreamObserver<GuessRequest> {

  private final StreamObserver<GuessResponse> guessResponseStreamObserver;
  private final int                           hiddenNumber;
  private       int                           attempts = 0;

  public GuessGameRequestHandler(StreamObserver<GuessResponse> guessResponseStreamObserver) {
    this.guessResponseStreamObserver = guessResponseStreamObserver;
    hiddenNumber                     = new Random().nextInt(1, 100);
    log.info("BGVW6FJQ ::THE HIDDEN number is: {}", hiddenNumber);
  }

  @Override
  public void onNext(GuessRequest guessRequest) {
    int guessValue = guessRequest.getValue();

    GuessResult result =  GuessResult.CORRECT;

    if (hiddenNumber < guessValue) {
      result = GuessResult.LOWER;
    } else if (hiddenNumber > guessValue) {
      result = GuessResult.HIGHER;
    }

    guessResponseStreamObserver.onNext(GuessResponse
        .newBuilder()
        .setResult(result)
        .setAttemptNumber(++attempts)
        .build()
    );

    if (GuessResult.CORRECT.equals(result)) {
      guessResponseStreamObserver.onCompleted();
    }
  }

  @Override
  public void onError(Throwable throwable) {
    log.info("error occurred: {}", throwable.getMessage());
  }

  @Override
  public void onCompleted() {
    log.info("client completed requests");
    guessResponseStreamObserver.onCompleted();
  }
}
