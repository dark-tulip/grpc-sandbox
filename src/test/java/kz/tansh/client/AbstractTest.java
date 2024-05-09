package kz.tansh.client;

import kz.tansh.proto.v15.BankServiceGrpc;
import kz.tansh.services.BankService;
import kz.tansh.services.GrpcServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;


@Slf4j
public abstract class AbstractTest extends AbstractChannelTest {
  private final GrpcServer grpcServer = GrpcServer.create(new BankService());
  protected BankServiceGrpc.BankServiceBlockingStub blockingStub;
  protected BankServiceGrpc.BankServiceStub stub;  // async stub

  @BeforeAll
  public void setUp() {
    this.grpcServer.start();
    log.info("server started") ;

    this.blockingStub = BankServiceGrpc.newBlockingStub(channel);
    log.info("blocking stub created created") ;

    this.stub = BankServiceGrpc.newStub(channel);
    log.info("async stub created created") ;
  }

  @AfterAll
  public void stop() {
    this.grpcServer.stop();
  }

}
