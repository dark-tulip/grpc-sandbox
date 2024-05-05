package kz.tansh.client;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import kz.tansh.proto.v15.BankServiceGrpc;
import kz.tansh.proto.v15.GetAccountRequest;
import kz.tansh.proto.v15.GetAccountResponse;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class GrpcClient {
  public static void main(String[] args) throws InterruptedException {
    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                                                  .usePlaintext()
                                                  .build();

    log.info("channel created on: " + channel.authority());

    var stub = BankServiceGrpc.newStub(channel);

    ExecutorService executors = Executors.newSingleThreadExecutor();

    // асинхронный тред еще не успевает завершиться когда Main thread завершает свою работу
    stub.getAccountNumber(
        GetAccountRequest.newBuilder().setAccountNumber(2).build(),
        new StreamObserver<>() {
          @Override
          public void onNext(GetAccountResponse getAccountResponse) {
            log.info("response from async stub: {}", getAccountResponse.getBalance());
          }
          @Override
          public void onError(Throwable throwable) {

          }
          @Override
          public void onCompleted() {
            log.info("completed async request");
          }
        }
    );

    log.info("done");

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      channel.shutdown();
      try {
        boolean test = channel.awaitTermination(100, TimeUnit.MILLISECONDS);
        log.info("is child threads completed: {}", test);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }));


//    var stubBlocking = BankServiceGrpc.newBlockingStub(channel);
//
//    GetAccountResponse response = stubBlocking.getAccountNumber(
//        GetAccountRequest.newBuilder().setAccountNumber(2).build()
//    );
//    log.info("response from blocking stub: " + response.getBalance());
  }
}
