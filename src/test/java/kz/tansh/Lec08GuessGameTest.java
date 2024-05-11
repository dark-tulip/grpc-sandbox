package kz.tansh;

import kz.tansh.client.AbstractChannelTest;
import kz.tansh.proto.v17.GuessGameServiceGrpc;
import kz.tansh.responseHandler.GuessResponseHandlerForTest;
import kz.tansh.server.GrpcServer;
import kz.tansh.services.GuessGameService;
import org.junit.jupiter.api.*;


@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
public class Lec08GuessGameTest extends AbstractChannelTest {

  private final GrpcServer server = GrpcServer.create(new GuessGameService());

  private GuessGameServiceGrpc.GuessGameServiceStub stub;

  @BeforeAll
  public void setUp() {
    server.start();
    stub = GuessGameServiceGrpc.newStub(channel);
  }

  @RepeatedTest(5)
  public void guessNumber() {
    var responseObserver = new GuessResponseHandlerForTest();
    var requestObserver  = stub.guess(responseObserver);
    responseObserver.setGuessRequestStreamObserver(requestObserver);

    responseObserver.start();
    responseObserver.await();
  }

  @AfterAll
  public void tearDown() {
    this.server.stop();
  }
}
