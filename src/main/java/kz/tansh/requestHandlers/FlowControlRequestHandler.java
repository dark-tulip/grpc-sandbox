package kz.tansh.requestHandlers;

import io.grpc.stub.StreamObserver;
import kz.tansh.proto.v16.Output;
import kz.tansh.proto.v16.RequestSize;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;

/**
 * Backpressure
 * Flow Control, когда клиент медленный и сервер подстраивается под пачку запросов которую может обработать клиент
 */
@Slf4j
public class FlowControlRequestHandler implements StreamObserver<RequestSize> {

  private final StreamObserver<Output> responseObserver;

  private int emitted;

  public FlowControlRequestHandler(StreamObserver<Output> responseObserver) {
    this.responseObserver = responseObserver;
    emitted = 0;
  }

  @Override
  public void onNext(RequestSize requestSize) {
    log.info("requested from client: " + requestSize.getSize());

    int max = emitted + requestSize.getSize();

    if (max > 100) {
      max = 100;
    }

    IntStream.rangeClosed(emitted + 1, max)
             .mapToObj(num -> Output.newBuilder().setValue(num).build())
             .forEach(responseObserver::onNext);

    emitted += requestSize.getSize();

    if (emitted >= 100) {
      responseObserver.onCompleted();
    }
  }

  @Override
  public void onError(Throwable throwable) {
    log.info("Error occurred: {}", throwable.getMessage());
  }

  @Override
  public void onCompleted() {
    responseObserver.onCompleted();
  }
}
