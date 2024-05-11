package kz.tansh.server;

import kz.tansh.L16_FlowControlService;
import kz.tansh.services.BankService;
import kz.tansh.services.TransferService;

public class RunLocalServer {

  public static void main(String[] args) {
    GrpcServer.create(6565,
                  new BankService(),
                  new TransferService(),
                  new L16_FlowControlService()
              )
              .start()
              .await();
  }
}
