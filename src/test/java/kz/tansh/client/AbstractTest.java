package kz.tansh.client;

import kz.tansh.L16_FlowControlService;
import kz.tansh.proto.v15.BankServiceGrpc;
import kz.tansh.proto.v15.TransferServiceGrpc;
import kz.tansh.proto.v16.FlowControlServiceGrpc;
import kz.tansh.server.GrpcServer;
import kz.tansh.services.BankService;
import kz.tansh.services.TransferService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;


@Slf4j
public abstract class AbstractTest extends AbstractChannelTest {
  private final GrpcServer grpcServer = GrpcServer.create(
      new BankService(),
      new TransferService(),
      new L16_FlowControlService()
  );

  protected BankServiceGrpc.BankServiceBlockingStub bankServiceBlockingStub;
  protected BankServiceGrpc.BankServiceStub         bankServiceStub;  // async stub
  protected TransferServiceGrpc.TransferServiceStub transferServiceStub;  // async stub
  protected FlowControlServiceGrpc.FlowControlServiceStub flowControlServiceStub;  // async stub

  @BeforeAll
  public void setUp() {
    this.grpcServer.start();
    log.info("server started");

    this.bankServiceBlockingStub = BankServiceGrpc.newBlockingStub(channel);
    log.info("blocking stub created created");

    this.bankServiceStub = BankServiceGrpc.newStub(channel);
    log.info("async stub created created");

    this.transferServiceStub = TransferServiceGrpc.newStub(channel);
    log.info("new transfer service async stub created");

    this.flowControlServiceStub = FlowControlServiceGrpc.newStub(channel);
    log.info("flowControlServiceStub created");
  }

  @AfterAll
  public void stop() {
    this.grpcServer.stop();
  }

}
