package kz.tansh.v18;

import kz.tansh.client.AbstractChannelTest;
import kz.tansh.proto.v18.BankServiceGrpc;
import kz.tansh.server.GrpcServer;
import kz.tansh.L18_BankService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class AbstractTest extends AbstractChannelTest {
  private final GrpcServer grpcServer = GrpcServer.create(new L18_BankService());

  protected BankServiceGrpc.BankServiceStub         bankStub;
  protected BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;


  @BeforeAll
  public void setUp() {
    this.grpcServer.start();
    this.bankBlockingStub = BankServiceGrpc.newBlockingStub(channel);
    this.bankStub         = BankServiceGrpc.newStub(channel);
  }


  @AfterAll
  public void stop() {
    this.grpcServer.stop();
  }

}
