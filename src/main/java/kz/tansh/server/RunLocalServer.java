package kz.tansh.server;

import kz.tansh.L16_FlowControlService;
import kz.tansh.L15_BankService;
import kz.tansh.L17_GuessGameService;
import kz.tansh.L18_BankService;
import kz.tansh.L15_TransferService;

public class RunLocalServer {

  public static void main(String[] args) {
    GrpcServer.create(6565,
                  new L15_BankService(),
                  new L18_BankService(),
                  new L15_TransferService(),
                  new L16_FlowControlService(),
                  new L17_GuessGameService()
              )
              .start()
              .await();
  }
}
